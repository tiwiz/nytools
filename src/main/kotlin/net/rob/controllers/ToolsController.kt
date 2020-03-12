package net.rob.controllers

import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import net.rob.commands.Command
import net.rob.commands.CommandFactory
import net.rob.commands.CommandRunner
import tornadofx.*

class ToolsController : Controller() {

    private val runner = CommandRunner()
    private val supportedTools = listOf(CommandFactory.adbInstallCheck(),
            CommandFactory.scrcpyInstallCheck())

    fun checkTools() {
        runner.runCommandsForFailures(supportedTools) {
            if (it.size == supportedTools.size) {
                warnUserNoToolsInstalledAndCloseApp()
            } else {
                warnUserSomeToolsAreMissing(it)
            }
        }
    }

    private fun warnUserNoToolsInstalledAndCloseApp() {
        val message = "This software needs ${supportedTools.joinToString { it.exec }} to be installed in order to work.\n" +
                "Please install them and rerun the app"

        alert(type = Alert.AlertType.ERROR, header = HEADER, content = message).button("OK") {
            Platform.exit()
        }
    }

    private fun warnUserSomeToolsAreMissing(missingCommands: List<Command>) {
        val message = "This software needs ${missingCommands.joinToString { it.exec }} to be installed in order to fully work.\n" +
                "Some functionalities will not work as expected."

        alert(type = Alert.AlertType.ERROR, header = HEADER, content = message, buttons = *arrayOf(ButtonType.CLOSE))
    }

    companion object {
        private const val HEADER = "MISSING TOOLS"
    }
}