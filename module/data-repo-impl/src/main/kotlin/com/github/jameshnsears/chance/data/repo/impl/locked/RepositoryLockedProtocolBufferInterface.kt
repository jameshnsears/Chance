package com.github.jameshnsears.chance.data.repo.impl.locked

import com.github.jameshnsears.chance.data.domain.proto.LockedProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.locked.RepositoryLockedInterface

interface RepositoryLockedProtocolBufferInterface : RepositoryLockedInterface {
    fun mapLockedProtocolBufferIntoLockedSet(
        lockedProtocolBuffer: LockedProtocolBuffer
    ): Set<String> {
        return lockedProtocolBuffer.lockedRollIndicesList.toSet()
    }

    fun mapLockedSetIntoLockedProtocolBufferBuilder(
        lockedRollIndices: Set<String>,
        lockedProtocolBufferBuilder: LockedProtocolBuffer.Builder
    ) {
        lockedProtocolBufferBuilder.clearLockedRollIndices()
        lockedProtocolBufferBuilder.addAllLockedRollIndices(lockedRollIndices)
    }
}
