package net.rob.commands

private typealias ShortCommand = Pair<Tools, Array<String>>

class Command(
        private val tools: Tools,
        val params: Array<String> = emptyArray(),
        val longRunning: Boolean = false) {

    constructor(input: ShortCommand,
                longRunning: Boolean = false) : this(tools = input.first, params = input.second, longRunning = longRunning)

    val exec: String
        get() = tools.path
}

private fun ShortCommand.build() = Command(this)


object CommandFactory {

    private const val HELP = "help"
    private const val HELP_SHORT = "-h"
    private const val SHELL = "shell"
    private const val SVC = "svc"
    private const val WIFI = "wifi"
    private const val DATA = "data"
    private const val ENABLE = "enable"
    private const val DISABLE = "disable"

    private const val AM = "am"
    private const val START = "start"
    private const val ACTION = "-a"
    private const val ACTION_VIEW = "android.intent.action.VIEW"
    private const val DEEPLINK = "-d"
    private const val LONG = "-l"

    private const val DEVICES = "devices"

    fun adbInstallCheck() = (Tools.ADB to arrayOf(HELP)).build()

    fun scrcpyInstallCheck() = (Tools.SCRCPY to arrayOf(HELP_SHORT)).build()

    fun fetchDevices() = (Tools.ADB to arrayOf(DEVICES, LONG)).build()

    fun enableWifi() = (Tools.ADB to arrayOf(SHELL, SVC, WIFI, ENABLE)).build()

    fun disableWifi() = (Tools.ADB to arrayOf(SHELL, SVC, WIFI, DISABLE)).build()

    fun enableData() = (Tools.ADB to arrayOf(SHELL, SVC, DATA, ENABLE)).build()

    fun disableData() = (Tools.ADB to arrayOf(SHELL, SVC, DATA, DISABLE)).build()

    fun runScrcpy() = Command(Tools.SCRCPY to emptyArray(), longRunning = true)

    fun sendDeeplink(deeplinkUri: String) = (Tools.ADB to arrayOf(SHELL, AM, START, ACTION,
            ACTION_VIEW, DEEPLINK, deeplinkUri)).build()
}