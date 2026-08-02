package com.github.jameshnsears.chance.data.repo.impl

import java.security.MessageDigest

interface RepositoryProtocolBufferImageCache {
    companion object {
        const val EPOCH_IMAGE_CACHE = -1L
        const val IMAGE_REF_PREFIX = "REF:"
    }

    fun sha256(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
