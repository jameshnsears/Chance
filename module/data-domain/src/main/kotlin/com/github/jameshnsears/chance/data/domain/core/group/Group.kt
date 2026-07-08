package com.github.jameshnsears.chance.data.domain.core.group

import java.util.UUID

data class Group(
    val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val uuidDice: List<String> = emptyList(),
    val notes: String = "",
    val selected: Boolean = false,
    val displayIndex: Int = 0,
)

