package net.rob.controllers

import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import net.rob.commands.*
import net.rob.utils.emptyValues
import net.rob.viewmodels.AppStateViewModel.updateUnavailableTools
import tornadofx.*

class ToolsController : Controller() {

    private val runner = CommandRunner()
    private val supportedTools = listOf(Tools.ADB, Tools.SCRCPY)

    fun checkTools() {
        val tools = runner.findTools()

        updateUnavailableTools(tools)

        val failures = tools.emptyValues()

        if (failures.size == supportedTools.size) {
            warnUserNoToolsInstalledAndCloseApp()
        } else if (failures.isNotEmpty()) {
            warnUserSomeToolsAreMissing(failures)
        }
    }

    private fun warnUserNoToolsInstalledAndCloseApp() {
        val message = "This software needs ${supportedTools.joinToString { it.path }} to be installed in order to work.\n" +
                "Please install them and rerun the app"

        alert(type = Alert.AlertType.ERROR, header = HEADER, content = message).button("OK") {
            Platform.exit()
        }
    }

    private fun warnUserSomeToolsAreMissing(missingCommands: List<Tools>) {
        val message = "This software needs ${missingCommands.joinToString { it.path }} to be installed in order to fully work.\n" +
                "Some functionalities will not work as expected."

        alert(type = Alert.AlertType.ERROR, header = HEADER, content = message, buttons = *arrayOf(ButtonType.CLOSE))
    }

    companion object {
        private const val HEADER = "MISSING TOOLS"
    }
}