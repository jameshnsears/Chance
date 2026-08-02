package com.github.jameshnsears.chance.ui.tab.setup.groups

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.ui.tab.DisplayIndexEvent
import com.github.jameshnsears.chance.ui.tab.GroupEvent
import com.github.jameshnsears.chance.ui.tab.rolls.RollsEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class GroupsAndroidViewModel(
    application: Application,
    val repositoryBag: RepositoryBagInterface,
    val repositoryGroup: RepositoryGroupInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    private val _stateFlowGroupHistory = MutableStateFlow<GroupHistory>(emptyList())
    val stateFlowGroupHistory: StateFlow<GroupHistory> = _stateFlowGroupHistory

    private val _stateFlowDiceBag = MutableStateFlow<List<Dice>>(emptyList())
    val stateFlowDiceBag: StateFlow<List<Dice>> = _stateFlowDiceBag

    private val _stateFlowNewGroup = MutableStateFlow(Group())
    val stateFlowNewGroup: StateFlow<Group> = _stateFlowNewGroup

    private val _stateFlowGroupDrafts = MutableStateFlow<Map<String, Group>>(emptyMap())
    val stateFlowGroupDrafts: StateFlow<Map<String, Group>> = _stateFlowGroupDrafts

    init {
        viewModelScope.launch {
            var initialLoad = true
            repositoryGroup.fetch().collect { groups ->
                if (initialLoad && groups.isEmpty()) {
                    // skip initial empty emission
                } else {
                    _stateFlowGroupHistory.value = groups.sortedBy { it.displayIndex }
                    initialLoad = false
                }
            }
        }

        viewModelScope.launch {
            var initialLoad = true
            repositoryBag.fetch().collect { diceBag ->
                if (initialLoad && diceBag.isEmpty()) {
                    // skip initial empty emission
                } else {
                    _stateFlowDiceBag.value = diceBag.sortedBy { it.displayIndex }
                    initialLoad = false
                }
            }
        }
    }

    fun onNameChange(group: Group, name: String) {
        updateGroup(group) { it.copy(name = name) }
    }

    fun onNotesChange(group: Group, notes: String) {
        updateGroup(group) { it.copy(notes = notes) }
    }

    fun onUuidDiceChange(group: Group, uuid: String, newQuantity: Int) {
        updateGroup(group) { currentGroup ->
            val otherUuids = currentGroup.uuidDice.filter { u -> u != uuid }
            val addedUuids = List(newQuantity) { uuid }
            currentGroup.copy(uuidDice = otherUuids + addedUuids)
        }
    }

    private fun updateGroup(group: Group, transform: (Group) -> Group) {
        if (group.uuid == _stateFlowNewGroup.value.uuid) {
            _stateFlowNewGroup.update(transform)
        } else {
            _stateFlowGroupDrafts.update { currentDrafts ->
                val currentDraft = currentDrafts[group.uuid] ?: group
                currentDrafts + (group.uuid to transform(currentDraft))
            }
        }
    }

    fun onDelete(group: Group) {
        viewModelScope.launch {
            _stateFlowGroupDrafts.update { it - group.uuid }
            val updatedHistory = _stateFlowGroupHistory.value.filter { it.uuid != group.uuid }
            repositoryGroup.store(updatedHistory)

            deleteRollHistory(group.uuid)

            GroupEvent.emit()
        }
    }

    private suspend fun deleteRollHistory(groupUuid: String) {
        val rollHistory = repositoryRoll.fetch().first()
        val updatedRollHistory = LinkedHashMap<Long, List<Roll>>()
        rollHistory.forEach { (epoch, rolls) ->
            if (rolls.none { it.uuidGroup == groupUuid }) {
                updatedRollHistory[epoch] = rolls
            }
        }
        repositoryRoll.store(updatedRollHistory)
        RollsEvent.emit()
    }

    fun onSave(group: Group) {
        viewModelScope.launch {
            val currentHistory = _stateFlowGroupHistory.value
            val draft = _stateFlowGroupDrafts.value[group.uuid]

            if (currentHistory.any { it.uuid == group.uuid }) {
                // Existing group
                if (draft != null) {
                    val originalGroup = currentHistory.first { it.uuid == group.uuid }
                    val diceChanged = draft.uuidDice.sorted() != originalGroup.uuidDice.sorted()

                    val groupToStore: Group
                    if (diceChanged) {
                        groupToStore = draft.copy(uuid = UUID.randomUUID().toString())

                        deleteRollHistory(originalGroup.uuid)
                    } else {
                        groupToStore = draft
                    }

                    val updatedHistory = currentHistory.map {
                        if (it.uuid == group.uuid) groupToStore else it
                    }
                    repositoryGroup.store(updatedHistory)
                    _stateFlowGroupDrafts.update { it - group.uuid }
                }
            } else {
                // New group
                val newGroupWithDisplayIndex = group.copy(displayIndex = currentHistory.size)
                repositoryGroup.store(currentHistory + newGroupWithDisplayIndex)
                _stateFlowNewGroup.update { Group() }
                DisplayIndexEvent.emit()
            }
            GroupEvent.emit()
        }
    }

    fun isNameValid(group: Group): Boolean {
        if (group.name.isBlank()) return false
        val otherGroups = _stateFlowGroupHistory.value.filter { it.uuid != group.uuid }
        return otherGroups.none { it.name.trim().equals(group.name.trim(), ignoreCase = true) }
    }

    fun isNotesValid(group: Group): Boolean {
        return group.notes.length <= 100
    }

    fun canSave(group: Group): Boolean {
        return isNameValid(group) && isNotesValid(group) && group.uuidDice.isNotEmpty()
    }

    fun move(fromIndex: Int, toIndex: Int, commit: Boolean = true) {
        if (fromIndex == toIndex) return

        val currentList = _stateFlowGroupHistory.value.toMutableList()
        if ((fromIndex in currentList.indices) && (toIndex in currentList.indices)) {
            val item = currentList.removeAt(fromIndex)
            currentList.add(toIndex, item)

            // Update displayIndex for all items in the list
            currentList.forEachIndexed { index, group ->
                currentList[index] = group.copy(displayIndex = index)
            }

            _stateFlowGroupHistory.value = currentList

            if (commit) {
                viewModelScope.launch {
                    repositoryGroup.store(currentList)
                    DisplayIndexEvent.emit()
                    GroupEvent.emit()
                }
            }
        }
    }

    fun moveUp(group: Group) {
        val currentList = _stateFlowGroupHistory.value
        val index = currentList.indexOfFirst { it.uuid == group.uuid }
        if (index > 0) {
            move(index, index - 1)
        }
    }

    fun moveDown(group: Group) {
        val currentList = _stateFlowGroupHistory.value
        val index = currentList.indexOfFirst { it.uuid == group.uuid }
        if (index != -1 && index < currentList.size - 1) {
            move(index, index + 1)
        }
    }
}
