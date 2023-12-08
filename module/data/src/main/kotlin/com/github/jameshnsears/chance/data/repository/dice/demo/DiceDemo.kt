package com.github.jameshnsears.chance.data.repository.dice.demo

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

class DiceDemo {
    companion object {
        val dice = listOf(
            Dice(
                1,
                sides = listOf(
                    Side(sideIndex = 2, text = "Heads"),
                    Side(sideIndex = 1, text = "Tails")
                ),
                description = "Heads / Tails",
            ),
            Dice(
                2,
            ),
            Dice(
                3,
                sides = listOf(
                    Side(sideIndex = 10, textStringsId = R.string.demo_dice_centurion, imageDrawableId = R.drawable.demo_dice_centurion),
                    Side(sideIndex = 9, textStringsId = R.string.demo_dice_crocodile, imageDrawableId = R.drawable.demo_dice_crocodile),
                    Side(sideIndex = 8, textStringsId = R.string.demo_dice_elephants, imageDrawableId = R.drawable.demo_dice_elephants),
                    Side(sideIndex = 7, textStringsId = R.string.demo_dice_hunter, imageDrawableId = R.drawable.demo_dice_hunter),
                    Side(sideIndex = 6, textStringsId = R.string.demo_dice_knight, imageDrawableId = R.drawable.demo_dice_knight),
                    Side(sideIndex = 5, textStringsId = R.string.demo_dice_lion, imageDrawableId = R.drawable.demo_dice_lion),
                    Side(sideIndex = 4, textStringsId = R.string.demo_dice_parrot, imageDrawableId = R.drawable.demo_dice_parrot),
                    Side(sideIndex = 3, textStringsId = R.string.demo_dice_pirate, imageDrawableId = R.drawable.demo_dice_pirate),
                    Side(sideIndex = 2, textStringsId = R.string.demo_dice_queen, imageDrawableId = R.drawable.demo_dice_queen),
                    Side(sideIndex = 1, textStringsId = R.string.demo_dice_spaceship, imageDrawableId = R.drawable.demo_dice_spaceship)
                ),
                description = "Story",
            ),
            Dice(
                4,
                sides = listOf(
                    Side(sideIndex = 4, text = "North", character = 8593),
                    Side(sideIndex = 3, text = "East", character = 8594),
                    Side(sideIndex = 2, text = "South", character = 8595),
                    Side(sideIndex = 1, text = "West", character = 8592)
                ),
                description = "Compass",
            ),
        )
    }
}
