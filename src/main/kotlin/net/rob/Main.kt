package net.rob

import javafx.application.Application.launch
import javafx.stage.Stage
import net.rob.ui.MainView
import net.rob.ui.Style
import tornadofx.*

class NYTools : App(MainView::class, Style::class) {
    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    launch(NYTools::class.java)
}