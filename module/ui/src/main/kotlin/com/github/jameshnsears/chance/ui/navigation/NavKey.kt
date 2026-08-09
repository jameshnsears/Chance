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

    @Serializable
    data class DialogDice(val diceUuid: String, val sideUuid: String) : ChanceNavKey

    @Serializable
    data object DialogSettings : ChanceNavKey

    @Serializable
    data object DialogGroup : ChanceNavKey

    @Serializable
    data class DialogConfirm(val diceUuid: String) : ChanceNavKey
}
