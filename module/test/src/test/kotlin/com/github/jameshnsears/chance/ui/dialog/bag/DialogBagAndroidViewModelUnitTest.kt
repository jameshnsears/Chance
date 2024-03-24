package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DialogBagAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun dialogBagRollCardExplodeAfterChangeInDiceSides() = runTest {
        val originalDice = SampleBag.d12

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(mutableListOf(SampleBag.d12))

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            mockk<Application>(),
            repositoryBag,
            originalDice,
            originalDice.sides[0]
        )

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
            .stateFlowCardRoll.value
            .rollExplodeAvailableValues.size == 12
        )

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceSidesSize("8")

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
                .stateFlowCardRoll.value
                .rollExplodeAvailableValues.size == 8
        )

        /*
SharedFlow is a type of Flow that allows multiple subscribers to receive values produced by a flow producer.
Unlike regular Flows, SharedFlows do not have a single owner, meaning that multiple collectors can collect
values from a SharedFlow at the same time.


// EventBus.kt
object EventBus {

    private val _events = MutableSharedFlow<Event>()

    val events: SharedFlow<Event> = _events

    fun emitEvent(event: Event) {
        _events.tryEmit(event)
    }
}

// Event.kt (data class for events)
data class Event(val type: String, val data: Any?)

//////////////////////////////////////////////////

// SomeViewModel.kt
class SomeViewModel(val viewModelScope: CoroutineScope) {

    init {
        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event.type) {
                    "EVENT_TYPE_1" -> {
                        // Handle event type 1
                        processData(event.data)
                    }
                    "EVENT_TYPE_2" -> {
                        // Handle event type 2
                        updateData(event.data)
                    }
                }
            }
        }
    }

    private fun processData(data: Any?) {
        // Handle data related to event type 1
    }

    private fun updateData(data: Any?) {
        // Handle data related to event type 2
    }

    // Function to trigger event from this ViewModel
    fun triggerEvent(type: String, data: Any? = null) {
        EventBus.emitEvent(Event(type, data))
    }
}

//////////////////////////////////////////////////

// AnotherViewModel.kt
class AnotherViewModel(val viewModelScope: CoroutineScope) {

    init {
        viewModelScope.launch {
            EventBus.events.collect { event ->
                // Handle all events here (optional)
            }
        }
    }

    // Function to trigger event from this ViewModel
    fun triggerAnotherEvent(type: String, data: Any? = null) {
        EventBus.emitEvent(Event(type, data))
    }
}
         */

        fail("todo -- use a flow to emit diceSidesSize that cardRollViewModel collects?")
    }

    @Test
    fun dialogBagSaveAfterModifyingAllCards() = runTest {
        // need to check saved to repository!
        fail("todo")
    }

    @Test
    fun dialogBagSaveNotPossibleAsTitleNotUnique() = runTest {
        fail("todo")
    }

    @Test
    fun dialogBagSaveWithCloneTicked() = runTest {
        fail("todo")
    }

    @Test
    fun dialogBagSaveWithDeleteTicked() = runTest {
        fail("todo + include androidTest for all of the above")
    }
}
