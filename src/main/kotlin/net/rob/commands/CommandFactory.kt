package net.rob.commands

import net.rob.commands.Parameters.ACTION
import net.rob.commands.Parameters.ACTION_VIEW
import net.rob.commands.Parameters.AM
import net.rob.commands.Parameters.DATA
import net.rob.commands.Parameters.DEEPLINK
import net.rob.commands.Parameters.DEVICES
import net.rob.commands.Parameters.DISABLE
import net.rob.commands.Parameters.ENABLE
import net.rob.commands.Parameters.HELP
import net.rob.commands.Parameters.HELP_SHORT
import net.rob.commands.Parameters.INSTALL
import net.rob.commands.Parameters.LONG
import net.rob.commands.Parameters.SERIAL
import net.rob.commands.Parameters.SHELL
import net.rob.commands.Parameters.START
import net.rob.commands.Parameters.SVC
import net.rob.commands.Parameters.WIFI

private typealias ShortCommand = Pair<Tools, Array<String>>

class Command(
        val tools: Tools,
        val params: Array<String> = emptyArray(),
        val longRunning: Boolean = false) {

    constructor(input: ShortCommand,
                longRunning: Boolean = false) : this(tools = input.first, params = input.second, longRunning = longRunning)

    val exec: String
        get() = tools.path
}

private fun ShortCommand.build() = Command(this)


object CommandFactory {

    fun adbInstallCheck() = (Tools.ADB to arrayOf(HELP)).build()

    fun scrcpyInstallCheck() = (Tools.SCRCPY to arrayOf(HELP_SHORT)).build()

    fun fetchDevices() = (Tools.ADB to arrayOf(DEVICES, LONG)).build()

    fun enableWifi(serial: String) = (Tools.ADB to arrayOf(SERIAL, serial, SHELL, SVC, WIFI, ENABLE)).build()

    fun disableWifi(serial: String) = (Tools.ADB to arrayOf(SERIAL, serial, SHELL, SVC, WIFI, DISABLE)).build()

    fun enableData(serial: String) = (Tools.ADB to arrayOf(SERIAL, serial, SHELL, SVC, DATA, ENABLE)).build()

    fun disableData(serial: String) = (Tools.ADB to arrayOf(SERIAL, serial, SHELL, SVC, DATA, DISABLE)).build()

    fun runScrcpy(serial: String) = Command(Tools.SCRCPY to arrayOf(SERIAL, serial), longRunning = true)

    fun sendDeeplink(serial: String, deeplinkUri: String, andPkg: String? = null) = (Tools.ADB to
            listOfNotNull(SERIAL, serial, SHELL, AM, START, ACTION, ACTION_VIEW, DEEPLINK, deeplinkUri, andPkg).toTypedArray()
            ).build()

    fun installApk(serial: String, apkPath: String) =
            Command(Tools.ADB to arrayOf(SERIAL, serial, INSTALL, apkPath), longRunning = true)
}

object Parameters {

    const val HELP = "help"
    const val HELP_SHORT = "-h"
    const val SHELL = "shell"
    const val SVC = "svc"
    const val WIFI = "wifi"
    const val DATA = "data"
    const val ENABLE = "enable"
    const val DISABLE = "disable"

    const val AM = "am"
    const val START = "start"
    const val ACTION = "-a"
    const val ACTION_VIEW = "android.intent.action.VIEW"
    const val DEEPLINK = "-d"
    const val LONG = "-l"
    const val SERIAL = "-s"

    const val DEVICES = "devices"
    const val INSTALL = "install"
}