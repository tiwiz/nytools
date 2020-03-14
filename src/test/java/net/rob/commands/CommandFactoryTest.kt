package net.rob.commands

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import org.junit.Test

class CommandFactoryTest {

    private val serial = "1234567890"
    private val deeplink = "test://uri"

    @Test
    fun `verify package is sent properly when present`() {
        val pkg = "net.rob.commands"

        val command = CommandFactory.sendDeeplink(serial, deeplink, pkg)

        assertThat(command.params).containsAll(serial, deeplink, pkg)
        assertThat(command.params.size).isEqualTo(10)
    }

    @Test
    fun `verify package is not sent when null`() {
        val command = CommandFactory.sendDeeplink(serial, deeplink, null)

        assertThat(command.params).containsAll(serial, deeplink)
        assertThat(command.params.size).isEqualTo(9)
    }
}