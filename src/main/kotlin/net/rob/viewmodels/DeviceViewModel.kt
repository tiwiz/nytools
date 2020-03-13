package net.rob.viewmodels

import net.rob.controllers.DeviceData
import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.forEach
import kotlin.collections.mutableMapOf
import kotlin.collections.set


object DeviceViewModel  {

    private val deviceList: MutableMap<String, DeviceData> = mutableMapOf()

    fun updateDeviceList(newDevices: List<DeviceData>) {
        clear()
        newDevices.forEach {
            deviceList["${it.serial} (${it.name})"] = it
        }
    }

    fun getDeviceListForUi() = deviceList.keys.toList()

    fun findDeviceBy(uiKey: String) = deviceList[uiKey]

    fun clear() {
        deviceList.keys.forEach { deviceList.remove(it) }
    }
}