/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk.fixture

import tech.antibytes.pixabay.sdk.model.PixabayItem
import tech.antibytes.pixabay.sdk.model.PixabayResponse
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture

fun PublicApi.Fixture.pixabayItemFixture(): PixabayItem {
    return PixabayItem(
        id = fixture(),
        user = fixture(),
        tags = fixture(),
        downloads = fixture(),
        likes = fixture(),
        comments = fixture(),
        preview = fixture(),
        large = fixture(),
    )
}

fun PublicApi.Fixture.pixabayResponseFixture(
    size: Int? = null,
): PixabayResponse {
    val items: MutableList<PixabayItem> = mutableListOf()
    val amountOfItems: Int = size ?: fixture<Int>(1..10)

    repeat(amountOfItems) {
        items.add(pixabayItemFixture())
    }

    return PixabayResponse(
        total = fixture(PublicApi.Sign.POSITIVE),
        items = items,
    )
}
