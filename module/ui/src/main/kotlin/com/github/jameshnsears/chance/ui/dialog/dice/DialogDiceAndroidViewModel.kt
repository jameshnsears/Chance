package com.github.jameshnsears.chance.ui.dialog.dice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceService
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceState
import com.github.jameshnsears.chance.ui.dialog.dice.card.face.CardSideService
import com.github.jameshnsears.chance.ui.dialog.dice.card.face.CardSideState
import com.github.jameshnsears.chance.ui.dialog.dice.card.roll.CardRollService
import com.github.jameshnsears.chance.ui.dialog.dice.card.roll.CardRollState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DialogDiceAndroidViewModel(
    application: Application,
    val repositoryBag: RepositoryBagInterface,
    val dice: Dice,
    val side: Side,
) : AndroidViewModel(application) {
    val repositoryRoll = RepositoryFactory(application).repositoryRoll
    val repositoryGroup = RepositoryFactory(application).repositoryGroup

    val dialogDiceService = DialogDiceService(
        repositoryBag,
        repositoryRoll,
        repositoryGroup
    )

    val cardDiceService = CardDiceService(
        repositoryBag,
        dice
    )

    val cardSideService = CardSideService(
        application,
        side
    )

    val cardRollService = CardRollService(
        dice
    )

    fun refresh() {
        viewModelScope.launch {
            val diceFromRepo = repositoryBag.fetch().first().find { it.uuid == dice.uuid }
            if (diceFromRepo != null) {
                cardDiceService.refresh(diceFromRepo)
                cardRollService.refresh(diceFromRepo)
                val sideFromRepo = diceFromRepo.sides.find { it.uuid == side.uuid }
                if (sideFromRepo != null) {
                    cardSideService.refresh(sideFromRepo)
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            dialogDiceService.delete(dice)
            DialogDiceCloseEvent.emit()
        }
    }

    fun clone(cardDiceState: CardDiceState, cardRollState: CardRollState) {
        viewModelScope.launch {
            dialogDiceService.clone(dice, cardDiceState, cardRollState)
            DialogDiceCloseEvent.emit()
        }
    }

    fun save(cardDiceState: CardDiceState, cardRollState: CardRollState, cardSideState: CardSideState) {
        viewModelScope.launch {
            dialogDiceService.save(dice, side, cardDiceState, cardRollState, cardSideState)
            DialogDiceCloseEvent.emit()
        }
    }
}
