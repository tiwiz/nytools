package net.rob.viewmodels

import net.rob.commands.DeviceData
import kotlin.collections.set


object DeviceViewModel  {

    private val deviceList: MutableMap<String, DeviceData> = mutableMapOf()
    private lateinit var selectedDevice: DeviceData

    fun updateDeviceList(newDevices: List<DeviceData>) {
        clear()
        newDevices.forEach {
            deviceList["${it.serial} (${it.name})"] = it
        }
    }

    val deviceListForUi
            get() = deviceList.keys.toList()

    fun selectDevice(uiKey: String) {
        selectedDevice = deviceList[uiKey]!!
    }

    val selectedSerial : String
            get() = selectedDevice.serial

    fun clear() {
        deviceList.keys.forEach { deviceList.remove(it) }
    }
}