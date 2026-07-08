package com.github.jameshnsears.chance.data.domain.core.group.impl

import android.content.Context
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.GroupDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory

class GroupDataImpl(
    val context: Context?,
    bagData: BagDataInterface,
) : GroupDataInterface {
    override val groupHistory: GroupHistory = emptyList(
    )
}
