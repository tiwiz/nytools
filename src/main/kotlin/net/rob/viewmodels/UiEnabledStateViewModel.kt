package net.rob.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.collections.FXCollections.observableArrayList
import net.rob.commands.Command
import net.rob.commands.DeviceCommandInteractor.Companion.OFFLINE
import net.rob.commands.Tools
import tornadofx.*

object UiEnabledStateViewModel {
    private var unavailableTools = SimpleListProperty<Tools>()
    private val availableDevices = SimpleIntegerProperty(0)

    val adbAvailable = SimpleBooleanProperty(isAdbAvailable())

    val scrcpyAvailable = SimpleBooleanProperty(isScrcpyAvailable())

    fun updateUnavailableTools(commands: List<Command>) {
        unavailableTools = SimpleListProperty(observableArrayList(commands.map { it.tools }))

        adbAvailable.set(isAdbAvailable())
        scrcpyAvailable.set(isScrcpyAvailable())
    }

    fun updateConnectedDevicesList(newDevices: List<String>) {
        availableDevices.value = newDevices.filterNot { it.contains(OFFLINE) }.count()
        adbAvailable.set(isAdbAvailable())
    }

    private fun isAdbAvailable() = !unavailableTools.contains(Tools.ADB) && availableDevices > 0

    private fun isScrcpyAvailable() = !unavailableTools.contains(Tools.SCRCPY)

    fun clear() = Unit

}