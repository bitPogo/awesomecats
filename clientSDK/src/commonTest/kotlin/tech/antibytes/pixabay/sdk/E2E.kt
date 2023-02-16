/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk

import com.github.michaelbull.result.unwrap
import tech.antibytes.pixabay.sdk.test.config.TestConfig
import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.isNot
import kotlin.test.Ignore

class E2E {
    @Test
    @Ignore
    @JsName("fn0")
    fun `It fetches Images`() = runBlockingTestWithTimeout(5000) {
        // Given
        val client = PixabayClient.getInstance(
            apiToken = TestConfig.apiKey,
            logger = LoggerStub(),
            connection = { true },
        )

        // When
        val response = client.fetchImages(
            query = "yellow flower",
            page = 1u,
        )

        // Then
        response.unwrap() isNot null
        response.unwrap().total isNot 0
        response.unwrap().items.size isNot 0
    }
}
