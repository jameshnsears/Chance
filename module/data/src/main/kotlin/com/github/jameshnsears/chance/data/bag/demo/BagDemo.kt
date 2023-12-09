package com.github.jameshnsears.chance.data.bag.demo

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

class BagDemo {
    companion object {
        val dice = listOf(
            Dice(
                1,
                sides = listOf(
                    Side(sideIndex = 2, textStringsId = R.string.demo_bag_heads),
                    Side(sideIndex = 1, textStringsId = R.string.demo_bag_tails)
                ),
                descriptionStringsId = R.string.demo_bag_heads_tails,
            ),
            Dice(
                2,
                sides = listOf(
                    Side(sideIndex = 4, unicodeCharacter = 8593),
                    Side(sideIndex = 3, unicodeCharacter = 8594),
                    Side(sideIndex = 2, unicodeCharacter = 8595),
                    Side(sideIndex = 1, unicodeCharacter = 8592)
                ),
                descriptionStringsId = R.string.demo_bag_compass
            ),
            Dice(
                3,
                descriptionStringsId = R.string.demo_bag_d6,
            ),
            Dice(
                4,
                sides = (8 downTo 1).map { index -> Side(sideIndex = index) },
                descriptionStringsId = R.string.demo_bag_d8,
            ),
            Dice(
                5,
                sides = listOf(
                    Side(
                        sideIndex = 10,
                        textStringsId = R.string.demo_bag_story_centurion,
                        imageDrawableId = R.drawable.demo_bag_centurion
                    ),
                    Side(
                        sideIndex = 9,
                        textStringsId = R.string.demo_bag_story_crocodile,
                        imageDrawableId = R.drawable.demo_bag_crocodile
                    ),
                    Side(
                        sideIndex = 8,
                        textStringsId = R.string.demo_bag_story_elephants,
                        imageDrawableId = R.drawable.demo_bag_elephants
                    ),
                    Side(
                        sideIndex = 7,
                        textStringsId = R.string.demo_bag_story_hunter,
                        imageDrawableId = R.drawable.demo_bag_hunter
                    ),
                    Side(
                        sideIndex = 6,
                        textStringsId = R.string.demo_bag_story_knight,
                        imageDrawableId = R.drawable.demo_bag_knight
                    ),
                    Side(
                        sideIndex = 5,
                        textStringsId = R.string.demo_bag_story_lion,
                        imageDrawableId = R.drawable.demo_bag_lion
                    ),
                    Side(
                        sideIndex = 4,
                        textStringsId = R.string.demo_bag_story_parrot,
                        imageDrawableId = R.drawable.demo_bag_parrot
                    ),
                    Side(
                        sideIndex = 3,
                        textStringsId = R.string.demo_bag_story_pirate,
                        imageDrawableId = R.drawable.demo_bag_pirate
                    ),
                    Side(
                        sideIndex = 2,
                        textStringsId = R.string.demo_bag_story_queen,
                        imageDrawableId = R.drawable.demo_bag_queen
                    ),
                    Side(
                        sideIndex = 1,
                        textStringsId = R.string.demo_bag_story_spaceship,
                        imageDrawableId = R.drawable.demo_bag_spaceship
                    )
                ),
                descriptionStringsId = R.string.demo_bag_story,
            ),
            Dice(
                6,
                sides = (12 downTo 1).map { index -> Side(sideIndex = index) },
                descriptionStringsId = R.string.demo_bag_d12,
            ),
            Dice(
                7,
                sides = (20 downTo 1).map { index -> Side(sideIndex = index) },
                descriptionStringsId = R.string.demo_bag_d20,
            ),
        )
    }
}
