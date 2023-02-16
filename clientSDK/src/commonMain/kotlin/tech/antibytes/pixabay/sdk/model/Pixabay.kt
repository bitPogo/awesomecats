/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixabayResponse(
    @SerialName("totalHits")
    val total: Int,
    @SerialName("hits")
    val items: List<PixabayItem>,
)

@Serializable
data class PixabayItem(
    val id: Long,
    val user: String,
    val tags: String,
    val downloads: UInt,
    val likes: UInt,
    val comments: UInt,
    @SerialName("previewURL")
    val preview: String,
    @SerialName("webformatURL")
    val large: String,
)
