package net.rob.viewmodels

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections.observableArrayList
import net.rob.commands.Command
import net.rob.commands.DeviceCommandInteractor.Companion.OFFLINE
import net.rob.commands.Tools
import net.rob.utils.emptyValues
import net.rob.utils.notEmptyValues
import tornadofx.*

object AppStateViewModel {
    private val availableDevices = SimpleIntegerProperty(0)
    private var unavailableTools = SimpleListProperty<Tools>()
    private var toolsAddresses : Map<Tools, String> = emptyMap()

    val adbAvailable = SimpleBooleanProperty(isAdbAvailable())

    val scrcpyAvailable = SimpleBooleanProperty(isScrcpyAvailable())

    fun updateUnavailableTools(commands: Map<Tools, String>) {
        unavailableTools = SimpleListProperty(observableArrayList(commands.emptyValues()))
        adbAvailable.set(isAdbAvailable())
        scrcpyAvailable.set(isScrcpyAvailable())

        toolsAddresses = commands
    }

    fun updateConnectedDevicesList(newDevices: List<String>) {
        availableDevices.value = newDevices.filterNot { it.contains(OFFLINE) }.count()
        adbAvailable.set(isAdbAvailable())
        scrcpyAvailable.set(isScrcpyAvailable())
    }

    private fun isAdbAvailable() = !unavailableTools.contains(Tools.ADB) && availableDevices > 0

    private fun isScrcpyAvailable() = !unavailableTools.contains(Tools.SCRCPY) && availableDevices > 0

    fun clear() = Unit

    fun pathFor(tool: Tools) = toolsAddresses[tool]
}