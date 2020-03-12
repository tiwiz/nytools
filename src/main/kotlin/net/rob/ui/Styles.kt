package net.rob.ui

import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.paint.Color
import tornadofx.*

class Style : Stylesheet() {

    companion object {
        val flatButton by cssclass()
        val frame by cssclass()
        val flatTextField by cssclass()

        const val LIGHTGRAY = "#e0e0e0"
        const val DARKBLUE = "#1976D2"
    }

    init {
        flatButton {
            borderStyle += BorderStrokeStyle.NONE
            borderColor += box(Color.TRANSPARENT)
            backgroundColor += Color.TRANSPARENT
            size = Dimension(24.toDouble(), Dimension.LinearUnits.px)
        }

        frame {
            borderColor += box(c(LIGHTGRAY))
            padding = box(Dimension(4.0, Dimension.LinearUnits.px))
            borderRadius += box(Dimension(4.0, Dimension.LinearUnits.px))
        }

        flatTextField {
            borderColor += box(top = Color.TRANSPARENT,
                    right = Color.TRANSPARENT,
                    left = Color.TRANSPARENT,
                    bottom = c(DARKBLUE))

            backgroundColor += Color.TRANSPARENT
        }
    }
}