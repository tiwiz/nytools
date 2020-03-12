package net.rob.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.buildobjects.process.ExternalProcessFailureException
import org.buildobjects.process.ProcBuilder
import org.buildobjects.process.StartupException
import java.io.BufferedReader
import java.io.InputStreamReader

sealed class CommandResult {
    class Success(val results: List<String> = emptyList()) : CommandResult()
    class Failure(val error: String) : CommandResult()
}

class CommandRunner {

    fun runCommand(command: Command, callback: (CommandResult) -> Unit = {}) {
        if(command.longRunning) {
            runLongRunningCommand(command, callback)
        } else {
            runNormalCommand(command, callback)
        }
    }

    private fun builder(command: Command, callback: (CommandResult) -> Unit) =
            ProcBuilder(command.exec)
                    .withArgs(*command.params)
                    .withOutputConsumer {
                        val reader = BufferedReader(InputStreamReader(it))
                        callback(
                                CommandResult.Success(reader.readLines())
                        )
                        reader.close()
                    }.apply {
                        if (command.longRunning) {
                            withNoTimeout()
                        }
                    }

    private fun runNormalCommand(command: Command, callback: (CommandResult) -> Unit = {}) {
        try {
            builder(command, callback).run()
        } catch (e: ExternalProcessFailureException) {
            callback(CommandResult.Failure(e.stderr))
        } catch (e: StartupException) {
            callback(CommandResult.Failure(e.message ?: "${command.exec} failure"))
        }
    }

    private fun runLongRunningCommand(command: Command, callback: (CommandResult) -> Unit = {}) {
        GlobalScope.launch(Dispatchers.Default) {
            try {
                builder(command, callback)
                        .withNoTimeout()
                        .run()
            } catch (e: ExternalProcessFailureException) {
                withContext(Dispatchers.Main) {
                    callback(CommandResult.Failure(e.stderr))
                }
            } catch (e: StartupException) {
                withContext(Dispatchers.Main) {
                    callback(CommandResult.Failure(e.message ?: "${command.exec} failure"))
                }
            }
        }.start()
    }

    fun runCommandWithErrorCallback(command: Command, onError: (String) -> Unit) {
        runCommand(command) {
            if (it is CommandResult.Failure) {
                onError(it.error)
            }
        }
    }

    fun runCommandsForFailures(commands: List<Command>, failureCallback: (List<Command>) -> Unit) {
        val failureList = mutableListOf<Command>()

        commands.forEach { command ->
            runCommandWithErrorCallback(command) {
                failureList.add(command)
            }
        }

        if (failureList.size > 0) {
            failureCallback(failureList)
        }
    }
}