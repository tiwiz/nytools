package net.rob.utils

import net.rob.commands.Tools

fun Map<Tools, String>.emptyValues() : List<Tools> =
        this.filter {
            it.value.isBlank() || it.value.isEmpty()
        }.keys.toList()