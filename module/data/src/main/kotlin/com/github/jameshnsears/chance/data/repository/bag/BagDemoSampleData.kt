package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

class BagDemoSampleData {
    companion object {
        val diceHeadsTails = Dice(
            sides = listOf(
                Side(
                    number = 2,
                    colour = "FF272B22",
                    descriptionStringsId = R.string.bag_demo_heads,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 1,
                    colour = "FF272B22",
                    descriptionStringsId = R.string.bag_demo_tails,
                    descriptionColour = "FF000000",
                ),
            ),
            titleStringsId = R.string.bag_demo_heads_tails,
            colour = "FFE1B530",
        )

        val diceStory = Dice(
            sides = listOf(
                Side(
                    number = 10,
                    descriptionStringsId = R.string.bag_demo_story_centurion,
                    imageDrawableId = R.drawable.demo_bag_centurion,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 9,
                    descriptionStringsId = R.string.bag_demo_story_crocodile,
                    imageDrawableId = R.drawable.demo_bag_crocodile,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 8,
                    descriptionStringsId = R.string.bag_demo_story_elephants,
                    imageDrawableId = R.drawable.demo_bag_elephants,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 7,
                    descriptionStringsId = R.string.bag_demo_story_hunter,
                    imageDrawableId = R.drawable.demo_bag_hunter,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 6,
                    descriptionStringsId = R.string.bag_demo_story_knight,
                    imageDrawableId = R.drawable.demo_bag_knight,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 5,
                    descriptionStringsId = R.string.bag_demo_story_lion,
                    imageDrawableId = R.drawable.demo_bag_lion,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 4,
                    descriptionStringsId = R.string.bag_demo_story_parrot,
                    imageDrawableId = R.drawable.demo_bag_parrot,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 3,
                    descriptionStringsId = R.string.bag_demo_story_pirate,
                    imageDrawableId = R.drawable.demo_bag_pirate,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 2,
                    descriptionStringsId = R.string.bag_demo_story_queen,
                    imageDrawableId = R.drawable.demo_bag_queen,
                    descriptionColour = "FF000000",
                ),
                Side(
                    number = 1,
                    descriptionStringsId = R.string.bag_demo_story_spaceship,
                    imageDrawableId = R.drawable.demo_bag_spaceship,
                    descriptionColour = "FF000000",
                ),
            ),
            titleStringsId = R.string.bag_demo_story,
            colour = "FF000000",
        )

        val allDice = listOf(
            diceHeadsTails,
            diceStory,
        )
    }
}
