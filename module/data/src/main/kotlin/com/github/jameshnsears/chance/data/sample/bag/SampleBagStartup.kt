package com.github.jameshnsears.chance.data.sample.bag

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side

class SampleBagStartup {
    companion object {
        val diceHeadsTails = Dice(
            sides = listOf(
                Side(
                    number = 2,
                    numberColour = "FF272B22",
                    descriptionStringsId = R.string.default_bag_heads,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 1,
                    numberColour = "FF272B22",
                    descriptionStringsId = R.string.default_bag_tails,
                    descriptionColour = "FF000000",
                ),
            ),
            titleStringsId = R.string.default_bag_heads_tails,
            colour = "FFE1B530",
            selected = false,
            multiplier = true,
            multiplierValue = 2,
            explode = true,
            explodeWhen = "=",
            explodeValue = 2,
            modifyScore = true,
            modifyScoreValue = 1
        )

        val diceStory = Dice(
            sides = listOf(
                Side(
                    number = 10,
                    descriptionStringsId = R.string.default_bag_story_centurion,
                    imageDrawableId = R.drawable.demo_bag_centurion,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 9,
                    descriptionStringsId = R.string.default_bag_story_crocodile,
                    imageDrawableId = R.drawable.demo_bag_crocodile,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 8,
                    descriptionStringsId = R.string.default_bag_story_elephants,
                    imageDrawableId = R.drawable.demo_bag_elephants,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 7,
                    descriptionStringsId = R.string.default_bag_story_hunter,
                    imageDrawableId = R.drawable.demo_bag_hunter,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 6,
                    descriptionStringsId = R.string.default_bag_story_knight,
                    imageDrawableId = R.drawable.demo_bag_knight,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 5,
                    descriptionStringsId = R.string.default_bag_story_lion,
                    imageDrawableId = R.drawable.demo_bag_lion,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 4,
                    descriptionStringsId = R.string.default_bag_story_parrot,
                    imageDrawableId = R.drawable.demo_bag_parrot,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 3,
                    descriptionStringsId = R.string.default_bag_story_pirate,
                    imageDrawableId = R.drawable.demo_bag_pirate,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 2,
                    descriptionStringsId = R.string.default_bag_story_queen,
                    imageDrawableId = R.drawable.demo_bag_queen,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 1,
                    descriptionStringsId = R.string.default_bag_story_spaceship,
                    imageDrawableId = R.drawable.demo_bag_spaceship,
                    descriptionColour = "FF000000",
                ),
            ),
            titleStringsId = R.string.default_bag_story,
            colour = "FF000000",
            selected = true,
            multiplier = true,
            multiplierValue = 3,
            explode = true,
            explodeWhen = "=",
            explodeValue = 3
        )

        val d6 = Dice(
            sides = (6 downTo 1).map { index -> Side(number = index) },
            title = "d6",
            multiplier = true,
            multiplierValue = 4,
        )

        val allDice = mutableListOf(
            diceHeadsTails,
            diceStory,
            d6
        )
    }
}
