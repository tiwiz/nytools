package net.rob.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.rob.viewmodels.AppStateViewModel.pathFor
import org.buildobjects.process.ExternalProcessFailureException
import org.buildobjects.process.ProcBuilder
import org.buildobjects.process.StartupException
import tornadofx.*
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader

sealed class CommandResult {
    class Success(val results: List<String> = emptyList()) : CommandResult()
    class Failure(val error: String) : CommandResult()
    object Loading : CommandResult()
}

class CommandRunner {

    fun findTools(): Map<Tools, String> {
        val lines = File(CONFIG_FILE).useLines { it.toList() }
        return lines.map {
            val (tool, path) = it.split("=")
            Tools.from(tool) to path
        }.toMap()
    }

    fun runCommand(command: Command, callback: (CommandResult) -> Unit = {}) =
            if (command.longRunning) {
                runLongRunningCommand(command, callback)
                CommandResult.Loading
            } else {
                runNormalCommand(command)
            }

    private fun builder(command: Command) =
            ProcBuilder(pathFor(command.tools))
                    .withArgs(*command.params).apply {
                        if (command.longRunning) {
                            withNoTimeout()
                        }
                    }

    private fun runNormalCommand(command: Command) =
            try {
                process(builder(command).run().outputBytes)
            } catch (e: ExternalProcessFailureException) {
                CommandResult.Failure(e.stderr)
            } catch (e: StartupException) {
                CommandResult.Failure(e.message ?: "${command.exec} failure")
            }

    private fun process(outputBytes: ByteArray): CommandResult.Success =
            CommandResult.Success(
                    with(BufferedReader(InputStreamReader(ByteArrayInputStream(outputBytes)))) {
                        readLines()
                    })

    private fun runLongRunningCommand(command: Command, callback: (CommandResult) -> Unit = {}) {
        GlobalScope.launch(Dispatchers.Default) {
            try {
                builder(command)
                        .withNoTimeout()
                        .run()
                returnResultOnMainThreadCallback(callback, CommandResult.Success())
            } catch (e: ExternalProcessFailureException) {
                returnResultOnMainThreadCallback(callback, CommandResult.Failure(e.stderr))
            } catch (e: StartupException) {
                returnResultOnMainThreadCallback(callback, CommandResult.Failure(e.message
                        ?: "${command.exec} failure"))
            }
        }.start()
    }

    private suspend fun returnResultOnMainThreadCallback(callback: (CommandResult) -> Unit, result: CommandResult) {
        withContext(Dispatchers.Main) {
            callback(result)
        }
    }

    fun runCommandWithErrorCallback(command: Command, onError: (String) -> Unit) {
        val result = runCommand(command)

        if (result is CommandResult.Failure) {
            onError(result.error)
        }
    }

    companion object {
        private const val CONFIG_FILE = "nytools.config"
    }
}
