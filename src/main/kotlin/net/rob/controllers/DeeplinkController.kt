package net.rob.controllers

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import net.rob.commands.CommandFactory
import net.rob.commands.CommandRunner
import tornadofx.*

class DeeplinkController : Controller() {

    private val runner = CommandRunner()

    fun sendDeeplinkToDevice(deeplinkUri: String) {
        if (deeplinkUri.isNotBlank()) {
            runner.runCommandWithErrorCallback(CommandFactory.sendDeeplink(deeplinkUri)) {
                showError(it)
            }
        } else {
            showError("Deeplink should not be empty")
        }
    }

    private fun showError(message: String) {
        alert(type = Alert.AlertType.ERROR, header = "Deeplink error", content = message,
                buttons = *arrayOf(ButtonType.CANCEL))
    }
}