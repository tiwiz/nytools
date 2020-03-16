package net.rob.commands

enum class Tools(val path: String) {
    ADB("ADB"),
    SCRCPY("SCRCPY");

    companion object {
        fun from(tools: String) =
                if (tools == "ADB") {
                    ADB
                } else {
                    SCRCPY
                }
    }
}