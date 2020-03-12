package net.rob.controllers

import net.rob.commands.CommandFactory
import net.rob.commands.CommandResult
import net.rob.commands.CommandRunner
import net.rob.controllers.DeviceResponse.Failure
import net.rob.controllers.DeviceResponse.Success

data class DeviceData(val serial: String, val name: String)
sealed class DeviceResponse {

    data class Success(val deviceData: List<DeviceData>) : DeviceResponse()
    data class Failure(val error: String) : DeviceResponse()
}

class DeviceCommandInteractor() {

    private val runner = CommandRunner()

    fun fetchDevices(): DeviceResponse {
        val result = runner.runCommand(CommandFactory.fetchDevices())
        return if (result is CommandResult.Success) {
            Success(parseSuccess(result.results))
        } else {
            Failure("Something strange happened while fetching connected devices. Restart this app and try again.")
        }
    }

    private fun parseSuccess(results: List<String>) =
            results.takeLast(results.size - 1)
                    .filterNot { it.isBlank() }
                    .map {
                        splitLineIntoModelAndSerial(it)
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