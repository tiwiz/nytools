package net.rob.controllers

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import net.rob.commands.DeviceCommandInteractor
import net.rob.commands.DeviceResponse
import net.rob.viewmodels.DeviceViewModel
import tornadofx.*


class DeviceController : Controller() {

    private val interactor = DeviceCommandInteractor()

    fun fetchDevices(onComplete: () -> Unit) {

        val deviceResult = interactor.fetchDevices()
        if (deviceResult is DeviceResponse.Success) {
            DeviceViewModel.updateDeviceList(deviceResult.deviceData)
            onComplete()
        } else if (deviceResult is DeviceResponse.Failure) {
            alert(type = Alert.AlertType.ERROR,
                    header = "Device list error",
                    content = deviceResult.error,
                    buttons = *arrayOf(ButtonType.CLOSE))
        }
    }
}


