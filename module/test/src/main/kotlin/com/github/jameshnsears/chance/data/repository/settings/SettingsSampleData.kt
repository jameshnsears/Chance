package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.domain.Side

class SettingsSampleData {
    companion object {
        val headsTails = Settings(
            rollSelectedDice = listOf(
                Dice(
                    title = "Heads / Tails",
                    sides = listOf(
                        Side(
                            number = 2,
                            description = "Heads"
                        ),
                        Side(
                            number = 1,
                            description = "Tails"
                        )
                    ),
                )
            ),
            bagZoom = 5,
            rollZoom = 10
        )
    }
}
