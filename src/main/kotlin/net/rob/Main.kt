package net.rob

import javafx.application.Application.launch
import javafx.scene.image.Image
import javafx.stage.Stage
import net.rob.ui.MainView
import net.rob.ui.Style
import net.rob.viewmodels.DeviceViewModel.clear
import tornadofx.*

class NYTools : App(Image("images/app_icon_one_color.png"), MainView::class, Style::class) {
    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }

    override fun stop() {
        clear()
        super.stop()
    }
}

fun main() {
    launch(NYTools::class.java)
}