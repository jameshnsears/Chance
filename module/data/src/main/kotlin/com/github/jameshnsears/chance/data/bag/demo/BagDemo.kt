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
                    Side(number = 2, descriptionStringsId = R.string.bag_demo_heads),
                    Side(number = 1, descriptionStringsId = R.string.bag_demo_tails)
                ),
                titleStringsId = R.string.bag_demo_heads_tails,
            ),
            Dice(
                5,
                sides = listOf(
                    Side(
                        number = 10,
                        descriptionStringsId = R.string.bag_demo_story_centurion,
                        imageDrawableId = R.drawable.demo_bag_centurion
                    ),
                    Side(
                        number = 9,
                        descriptionStringsId = R.string.bag_demo_story_crocodile,
                        imageDrawableId = R.drawable.demo_bag_crocodile
                    ),
                    Side(
                        number = 8,
                        descriptionStringsId = R.string.bag_demo_story_elephants,
                        imageDrawableId = R.drawable.demo_bag_elephants
                    ),
                    Side(
                        number = 7,
                        descriptionStringsId = R.string.bag_demo_story_hunter,
                        imageDrawableId = R.drawable.demo_bag_hunter
                    ),
                    Side(
                        number = 6,
                        descriptionStringsId = R.string.bag_demo_story_knight,
                        imageDrawableId = R.drawable.demo_bag_knight
                    ),
                    Side(
                        number = 5,
                        descriptionStringsId = R.string.bag_demo_story_lion,
                        imageDrawableId = R.drawable.demo_bag_lion
                    ),
                    Side(
                        number = 4,
                        descriptionStringsId = R.string.bag_demo_story_parrot,
                        imageDrawableId = R.drawable.demo_bag_parrot
                    ),
                    Side(
                        number = 3,
                        descriptionStringsId = R.string.bag_demo_story_pirate,
                        imageDrawableId = R.drawable.demo_bag_pirate
                    ),
                    Side(
                        number = 2,
                        descriptionStringsId = R.string.bag_demo_story_queen,
                        imageDrawableId = R.drawable.demo_bag_queen
                    ),
                    Side(
                        number = 1,
                        descriptionStringsId = R.string.bag_demo_story_spaceship,
                        imageDrawableId = R.drawable.demo_bag_spaceship
                    )
                ),
                titleStringsId = R.string.bag_demo_story,
            ),
        )
    }
}
