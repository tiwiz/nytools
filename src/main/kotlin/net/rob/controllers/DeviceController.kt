package net.rob.controllers

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import net.rob.commands.CommandFactory
import net.rob.commands.CommandResult
import net.rob.commands.CommandRunner
import tornadofx.*

data class DeviceData(val serial: String, val name: String)


class DeviceController : Controller() {

    private val runner = CommandRunner()

    fun fetchDevices(onComplete: (List<DeviceData>) -> Unit) {
        runner.runCommand(CommandFactory.fetchDevices()) {
            if (it is CommandResult.Success) {
                parseSuccess(it.results) { l ->
                    onComplete(l)
                }
            } else {
                val message = "Something strange happened while fetching connected devices. Restart this app and try again."

                alert(type = Alert.AlertType.ERROR, header = "Device list error", content = message, buttons = *arrayOf(ButtonType.CLOSE))
            }
        }
    }

    private fun parseSuccess(results: List<String>, onComplete: (List<DeviceData>) -> Unit) {
        val parsedItems = results.takeLast(results.size - 1)
                .filterNot { it.isBlank() }
                .map {
                    splitLineIntoModelAndSerial(it)
                }

        onComplete(parsedItems)
    }

    private fun splitLineIntoModelAndSerial(line: String): DeviceData {
        val (serial, model) = line.split(" ")
                .mapIndexed { index, s -> if (index != 0 && !s.contains("model")) "" else s }
                .filterNot { it.isBlank() }

        return DeviceData(serial, model.clean())
    }

    private fun String.clean(): String {
        val (_, model) = split(":")

        return model.replace("_", " ")
    }
}


