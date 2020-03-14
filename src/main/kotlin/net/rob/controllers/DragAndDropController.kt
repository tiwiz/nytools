package net.rob.controllers

import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.VBox
import net.rob.commands.CommandFactory.installApk
import net.rob.commands.CommandResult
import net.rob.commands.CommandResult.Failure
import net.rob.commands.CommandResult.Success
import net.rob.commands.CommandRunner
import net.rob.ui.Toast
import net.rob.viewmodels.DeviceViewModel.selectedSerial
import tornadofx.*
import java.io.File

class DragAndDropController : Controller() {

    private val runner = CommandRunner()

    private lateinit var target: VBox

    fun bindTo(vbox: VBox) {
        target = vbox
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
                .forEach { file -> runner.runCommand(
                        installApk(selectedSerial, file.absolutePath)
                ) {
                    onInstallResult(it, file)
                }}
    }

    private fun onInstallResult(result: CommandResult, file: File) {
        val message = if(result is Success) {
            "${file.name} was installed successfully"
        } else {
            "${file.name} was not installed: ${(result as Failure).error}"
        }

        Toast.makeText(primaryStage, message)
    }
}