package net.rob.controllers

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import net.rob.commands.CommandFactory
import net.rob.commands.CommandRunner
import tornadofx.*

class ToolbarController : Controller() {

    private val runner = CommandRunner()

    fun enableWifi() {
        runner.runCommandWithErrorCallback(CommandFactory.enableWifi()) {
            showError(it)
        }
    }

    fun disableWifi() {
        runner.runCommandWithErrorCallback(CommandFactory.disableWifi()) {
            showError(it)
        }
    }

    fun enableData() {
        runner.runCommandWithErrorCallback(CommandFactory.enableData()) {
            showError(it)
        }
    }

    fun disableData() {
        runner.runCommandWithErrorCallback(CommandFactory.disableData()) {
            showError(it)
        }
    }

    fun runScrcpy() {
        runner.runCommandWithErrorCallback(CommandFactory.runScrcpy()) {
            showError(it)
        }
    }

    private fun showError(message: String) {
        alert(type = Alert.AlertType.ERROR, header = "Error while running command", content = message,
                buttons = *arrayOf(ButtonType.OK))
    }
}