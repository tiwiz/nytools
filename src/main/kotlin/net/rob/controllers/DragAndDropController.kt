package net.rob.controllers

import javafx.scene.Node
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import net.rob.commands.CommandFactory.installApk
import net.rob.commands.CommandRunner
import net.rob.viewmodels.DeviceViewModel.selectedSerial
import tornadofx.*
import java.io.File

class DragAndDropController : Controller() {

    private val runner = CommandRunner()

    private lateinit var target: Node

    fun bindTo(node: Node) {
        target = node
    }

    fun onDragOver(event: DragEvent) {
        if (event.gestureSource != target && event.dragboard.hasFiles()) {
            event.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
        }
        event.consume()
    }

    fun onDragDropped(event: DragEvent) {

        event.isDropCompleted =
                if (event.dragboard.hasFiles()) {
                    checkAndInstallAPKs(event.dragboard.files)
                    true
                } else {
                    false
                }

        event.consume()
    }

    private fun checkAndInstallAPKs(files: List<File>) {
        files.filter { it.name.endsWith(".apk") }
                .map { it.absolutePath }
                .forEach { path -> installApk(selectedSerial, path) }
    }
}