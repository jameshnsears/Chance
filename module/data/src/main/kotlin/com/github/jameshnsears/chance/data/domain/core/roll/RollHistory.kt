package com.github.jameshnsears.chance.data.domain.core.roll

// a RollHistory is effectively a history of a timed sequence of rolled dice
typealias RollHistory = LinkedHashMap<Long, List<Roll>>
