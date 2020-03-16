package net.rob.ui

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import net.rob.controllers.*
import net.rob.viewmodels.DeviceViewModel.deviceListForUi
import net.rob.viewmodels.DeviceViewModel.selectDevice
import net.rob.viewmodels.AppStateViewModel.adbAvailable
import net.rob.viewmodels.AppStateViewModel.scrcpyAvailable
import net.rob.viewmodels.AppStateViewModel.updateConnectedDevicesList
import tornadofx.*

class MainView : View(title = "NYTools") {


    private val leftPane: LeftPane by inject()
    private val rightPane: RightPane by inject()

    override val root = hbox {

        add(leftPane)

        add(rightPane)

    }
}

class LeftPane : View() {

    private val toolsController: ToolsController by inject()

    private val toolbar: ToolbarView by inject()
    private val deeplinkView: DeeplinkView by inject()
    private val deviceView: DeviceView by inject()

    override val root = vbox {

        toolsController.checkTools()

        paddingAll = 8.0
        spacing = 4.0

        fieldset("Device select") {
            addClass(Style.frame)
            add(deviceView)
        }

        fieldset("Quick tools") {
            addClass(Style.frame)
            add(toolbar)
        }

        fieldset("Deeplink") {
            addClass(Style.frame)
            add(deeplinkView)
        }
    }
}

class ToolbarView : View() {

    private val controller: ToolbarController by inject()

    override val root = hbox(spacing = 16.0) {

        setMinSize(400.0, 48.0)
        alignment = Pos.CENTER

        button(graphic = imageview("images/enable_wifi.png")) {
            tooltip("Enable Wifi")
            addClass(Style.flatButton)
            enableWhen(adbAvailable)
        }.setOnAction {
            controller.enableWifi()
        }

        button(graphic = imageview("images/disable_wifi.png")) {
            tooltip("Disable Wifi")
            addClass(Style.flatButton)
            enableWhen(adbAvailable)
        }.setOnAction {
            controller.disableWifi()
        }

        button(graphic = imageview("images/enable_data.png")) {
            tooltip("Enable Mobile Data")
            addClass(Style.flatButton)
            enableWhen(adbAvailable)
        }.setOnAction {
            controller.enableData()
        }

        button(graphic = imageview("images/disable_data.png")) {
            tooltip("Disable Mobile Data")
            addClass(Style.flatButton)
            enableWhen(adbAvailable)
        }.setOnAction {
            controller.disableData()
        }

        button(graphic = imageview("images/scrcpy.png")) {
            tooltip("Mirror device")
            addClass(Style.flatButton)
            enableWhen(scrcpyAvailable)
        }.setOnAction {
            controller.runScrcpy()
        }
    }
}

class DeeplinkView : View() {

    private var deeplinkField: TextField by singleAssign()
    private var packageField: TextField by singleAssign()

    private val deeplinkController: DeeplinkController by inject()

    private val pkg = SimpleStringProperty(this, "targetPackage", config.string("targetPackage"))

    override val root =
            vbox {
                hbox {

                    alignment = Pos.CENTER_LEFT

                    textfield {

                        setMinSize(300.0, 48.0)

                        deeplinkField = this

                        promptText = "Insert URL or URI"

                        addClass(Style.flatTextField)
                    }

                    button(graphic = imageview("images/send.png")) {
                        tooltip("Send deeplink command to device")
                        addClass(Style.flatButton)
                        enableWhen(adbAvailable)
                    }.setOnAction {
                        config.set("targetPackage" to pkg.value)
                        config.save()
                        deeplinkController.sendDeeplinkToDevice(deeplinkField.text, pkg.value)
                    }

                    button(graphic = imageview("images/delete.png")) {
                        tooltip("Clear deeplink field")
                        addClass(Style.flatButton)
                    }.setOnAction {
                        deeplinkField.clear()
                    }
                }

                textfield(pkg) {

                    setMinSize(300.0, 48.0)

                    promptText = "Target package or leave empty for broadcast"

                    addClass(Style.flatTextField)

                    packageField = this
                }
            }
}


class DeviceView : View() {

    private val deviceController: DeviceController by inject()
    private val selectedCity = SimpleStringProperty()

    private var deviceComboBox: ComboBox<String> by singleAssign()

    override val root = hbox {

        setMinSize(400.0, 48.0)

        combobox<String>(selectedCity) {
            deviceComboBox = this
            addClass(Style.dropList)

            setMinSize(200.0, 48.0)
        }

        button(graphic = imageview("images/refresh.png")) {
            tooltip("Reload devices list")
            addClass(Style.flatButton)
        }.setOnAction {
            loadDevices()
        }
    }

    init {

        loadDevices()

        selectedCity.onChange { key ->
            key?.let {
                selectDevice(it)
            }
        }
    }

    private fun loadDevices() {
        deviceController.fetchDevices {
            deviceComboBox.items = deviceListForUi.toObservableList()
            deviceComboBox.selectionModel.selectFirst()

            updateConnectedDevicesList(deviceListForUi)

            /**
             * Workaround for ComboBox behaviour that doesn't trigger the selection properly
             * when only one item is present
             */
            if (deviceListForUi.isNotEmpty()) {
                selectDevice(deviceListForUi.first())
            }
        }
    }

    private fun List<String>.toObservableList() = FXCollections.observableArrayList(this)
}

class RightPane : View() {

    private val dragAndDropController: DragAndDropController by inject()

    override val root = vbox {

        enableWhen(adbAvailable)

        paddingAll = 8.0
        spacing = 4.0

        dragAndDropController.bindTo(this)

        fieldset("Install APK") {
            addClass(Style.frame)

            vbox {

                setMinSize(300.0, 254.0)

                alignment = Pos.CENTER

                label("Drop or paste an APK to install\nit on the selected device") {
                    alignment = Pos.CENTER
                }
            }
        }

        setOnDragOver { dragAndDropController.onDragOver(it) }

        setOnDragDropped {
            dragAndDropController.onDragDropped(it)
        }
    }
}