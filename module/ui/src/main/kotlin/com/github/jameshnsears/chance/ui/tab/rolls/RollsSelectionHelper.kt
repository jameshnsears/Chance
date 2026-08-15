package com.github.jameshnsears.chance.ui.tab.rolls

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class RollsSelectionHelper(
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryGroup: RepositoryGroupInterface
) {
    fun markDiceAsSelected(
        dice: Dice,
        selected: Boolean,
        scope: CoroutineScope
    ) {
        Timber.d("uuid=${dice.uuid}; selected=${selected}")

        scope.launch {
            repositoryBag.store(dice.uuid, selected)
        }
    }

    fun markGroupAsSelected(
        group: Group,
        selected: Boolean,
        scope: CoroutineScope
    ) {
        Timber.d("group.uuid=${group.uuid}; selected=$selected")

        scope.launch {
            repositoryGroup.store(group.uuid, selected)
        }
    }
}
