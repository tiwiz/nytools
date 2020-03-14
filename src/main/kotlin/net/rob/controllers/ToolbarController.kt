package net.rob.controllers

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import net.rob.commands.CommandFactory.disableData
import net.rob.commands.CommandFactory.disableWifi
import net.rob.commands.CommandFactory.enableData
import net.rob.commands.CommandFactory.enableWifi
import net.rob.commands.CommandFactory.runScrcpy
import net.rob.commands.CommandRunner
import net.rob.viewmodels.DeviceViewModel.selectedSerial
import tornadofx.*

class ToolbarController : Controller() {

    private val runner = CommandRunner()

    fun enableWifi() {
        runner.runCommandWithErrorCallback(enableWifi(selectedSerial)) {
            showError(it)
        }
    }

    fun disableWifi() {
        runner.runCommandWithErrorCallback(disableWifi(selectedSerial)) {
            showError(it)
        }
    }

    fun enableData() {
        runner.runCommandWithErrorCallback(enableData(selectedSerial)) {
            showError(it)
        }
    }

    fun disableData() {
        runner.runCommandWithErrorCallback(disableData(selectedSerial)) {
            showError(it)
        }
    }

    fun runScrcpy() {
        runner.runCommandWithErrorCallback(runScrcpy(selectedSerial)) {
            showError(it)
        }
    }

    private fun showError(message: String) {
        alert(type = Alert.AlertType.ERROR, header = "Error while running command", content = message,
                buttons = *arrayOf(ButtonType.OK))
    }
}