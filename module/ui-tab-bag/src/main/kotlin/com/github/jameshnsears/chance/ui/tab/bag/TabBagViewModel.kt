package com.github.jameshnsears.chance.ui.tab.bag

import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface

open class TabBagViewModel(
    val bagRepository: BagRepositoryInterface,
) : ViewModel() {
}
