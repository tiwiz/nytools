package net.rob.ui

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration

object Toast {

    fun makeText(stage: Stage, message: String, displayTime: Int = 3000, fadeInDelay: Int = 500, fadeOutDelay: Int = 500, size: Double = 12.0, opacity: Double = 5.0) {
        val toastStage = Stage()
        toastStage.initOwner(stage)
        toastStage.isResizable = false
        toastStage.initStyle(StageStyle.TRANSPARENT)

        val text = Text(message)
        text.font = Font.font("Verdana", size)
        text.fill = Color.BLACK

        val root = StackPane(text)
        root.style = "-fx-background-radius: 12; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 12px;"
        root.opacity = opacity

        val scene = Scene(root)
        scene.fill = Color.TRANSPARENT
        toastStage.scene = scene
        toastStage.show()

        val fadeInTimeline = Timeline()
        val fadeInKey1 =
                KeyFrame(Duration.millis(fadeInDelay.toDouble()), KeyValue(toastStage.scene.root.opacityProperty(), 1))
        fadeInTimeline.keyFrames.add(fadeInKey1)
        fadeInTimeline.setOnFinished {
            Thread {
                try {
                    Thread.sleep(displayTime.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                val fadeOutTimeline = Timeline()
                val fadeOutKey1 =
                        KeyFrame(
                                Duration.millis(fadeOutDelay.toDouble()),
                                KeyValue(toastStage.scene.root.opacityProperty(), 0)
                        )
                fadeOutTimeline.keyFrames.add(fadeOutKey1)
                fadeOutTimeline.setOnFinished { toastStage.close() }
                fadeOutTimeline.play()
            }.start()
        }
        fadeInTimeline.play()
    }
}