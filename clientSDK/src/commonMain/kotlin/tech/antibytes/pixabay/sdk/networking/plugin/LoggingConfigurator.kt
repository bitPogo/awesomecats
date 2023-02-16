/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk.networking.plugin

import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

internal class LoggingConfigurator : KtorPluginsContract.LoggingConfigurator {
    override fun configure(pluginConfiguration: Logging.Config, subConfiguration: tech.antibytes.pixabay.sdk.ClientContract.Logger) {
        pluginConfiguration.logger = subConfiguration
        pluginConfiguration.level = LogLevel.ALL
    }
}
