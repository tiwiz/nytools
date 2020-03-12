package net.rob.controllers

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import tornadofx.*


class DeviceController : Controller() {

    private val interactor = DeviceCommandInteractor()

    fun fetchDevices(onComplete: (List<DeviceData>) -> Unit) {

        val deviceResult = interactor.fetchDevices()
        if (deviceResult is DeviceResponse.Success) {
            onComplete(deviceResult.deviceData)
        } else if (deviceResult is DeviceResponse.Failure) {
            alert(type = Alert.AlertType.ERROR,
                    header = "Device list error",
                    content = deviceResult.error,
                    buttons = *arrayOf(ButtonType.CLOSE))
        }
    }
}


