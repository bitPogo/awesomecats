/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk.networking.plugin

import tech.antibytes.pixabay.sdk.LoggerMock
import tech.antibytes.pixabay.sdk.kmock
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.kmock.MockCommon
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs

@MockCommon(
    tech.antibytes.pixabay.sdk.ClientContract.Logger::class,
)
class LoggingConfiguratorSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils LoggingConfigurator`() {
        LoggingConfigurator() fulfils KtorPluginsContract.LoggingConfigurator::class
    }

    @Test
    @JsName("fn1")
    fun `Given configure is called with a LoggingConfig, it sets it up and just runs`() {
        // Given
        val config = Logging.Config()
        val logger: LoggerMock = kmock()

        // When
        val result = LoggingConfigurator().configure(config, logger)

        // Then
        result mustBe Unit
        config.level mustBe LogLevel.ALL

        config.logger sameAs logger
    }
}
