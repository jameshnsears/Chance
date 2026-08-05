package com.github.jameshnsears.chance.ui.tab.rolls

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

class RollsSelectionHelper(
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryGroup: RepositoryGroupInterface
) {
    private val mutex = Mutex()

    fun markDiceAsSelected(
        dice: Dice,
        selected: Boolean,
        diceBag: DiceBag,
        scope: CoroutineScope
    ) {
        Timber.d("uuid=${dice.uuid}; selected=${selected}")

        scope.launch {
            mutex.withLock {
                val updatedDiceBag: DiceBag = mutableListOf()

                diceBag.forEach {
                    val existingDice = it.copy()

                    if (dice.uuid == it.uuid) {
                        existingDice.selected = selected
                    }

                    updatedDiceBag.add(existingDice)
                }

                repositoryBag.store(updatedDiceBag)
            }
        }
    }

    fun markGroupAsSelected(
        group: Group,
        groupHistory: GroupHistory,
        scope: CoroutineScope
    ) {
        Timber.d("group.uuid=${group.uuid}")

        scope.launch {
            mutex.withLock {
                val updatedHistory = groupHistory.map {
                    if (it.uuid == group.uuid) it.copy(selected = !it.selected) else it
                }
                repositoryGroup.store(updatedHistory)
            }
        }
    }
}
