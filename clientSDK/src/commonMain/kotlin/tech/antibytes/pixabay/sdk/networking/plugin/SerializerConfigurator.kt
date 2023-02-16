/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk.networking.plugin

import tech.antibytes.pixabay.sdk.serialization.JsonConfiguratorContract
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal class SerializerConfigurator : KtorPluginsContract.SerializerConfigurator {
    override fun configure(
        pluginConfiguration: ContentNegotiation.Config,
        subConfiguration: JsonConfiguratorContract,
    ) {
        pluginConfiguration.json(
            json = Json { subConfiguration.configure(this) },
        )
    }
}
