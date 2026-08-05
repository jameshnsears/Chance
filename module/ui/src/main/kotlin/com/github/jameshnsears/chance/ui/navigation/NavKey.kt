package com.github.jameshnsears.chance.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ChanceNavKey : NavKey {
    @Serializable
    data object SetupDice : ChanceNavKey

    @Serializable
    data object SetupGroups : ChanceNavKey

    @Serializable
    data object Roll : ChanceNavKey
}
