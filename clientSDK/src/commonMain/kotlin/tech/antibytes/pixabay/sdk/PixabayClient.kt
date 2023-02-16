/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import tech.antibytes.pixabay.sdk.ClientContract.ENDPOINT
import tech.antibytes.pixabay.sdk.ClientContract.HOST
import tech.antibytes.pixabay.sdk.ClientContract.ITEMS_PER_PAGE
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import com.github.michaelbull.result.Result
import tech.antibytes.pixabay.sdk.error.PixabayClientError
import tech.antibytes.pixabay.sdk.model.PixabayResponse
import tech.antibytes.pixabay.sdk.networking.*
import tech.antibytes.pixabay.sdk.networking.ClientConfigurator
import tech.antibytes.pixabay.sdk.networking.HttpErrorMapper
import tech.antibytes.pixabay.sdk.networking.NetworkingContract
import tech.antibytes.pixabay.sdk.networking.RequestBuilder
import tech.antibytes.pixabay.sdk.networking.plugin.LoggingConfigurator
import tech.antibytes.pixabay.sdk.networking.plugin.ResponseValidatorConfigurator
import tech.antibytes.pixabay.sdk.networking.plugin.SerializerConfigurator
import tech.antibytes.pixabay.sdk.networking.receive
import tech.antibytes.pixabay.sdk.serialization.JsonConfigurator
import kotlinx.serialization.json.Json

class PixabayClient internal constructor(
    private val token: String,
    private val requestBuilder: NetworkingContract.RequestBuilderFactory,
    private val connectivityManager: ClientContract.ConnectivityManager,
) : ClientContract.Client {
    private suspend fun guardTransaction(
        action: suspend () -> Result<PixabayResponse, PixabayClientError>,
    ): Result<PixabayResponse, PixabayClientError> {
        return if (!connectivityManager.hasConnection()) {
            Err(PixabayClientError.NoConnection())
        } else {
            action()
        }
    }

    private suspend fun fetchImagesFromApi(
        query: String,
        page: UShort,
    ): PixabayResponse {
        return requestBuilder
            .create()
            .setParameter(
                mapOf(
                    "key" to token,
                    "q" to query,
                    "page" to page.toString(),
                    "per_page" to ITEMS_PER_PAGE,
                ),
            ).prepare(
                path = ENDPOINT,
            ).receive()
    }

    override suspend fun fetchImages(
        query: String,
        page: UShort,
    ): Result<PixabayResponse, PixabayClientError> = guardTransaction {
        try {
            Ok(fetchImagesFromApi(query, page))
        } catch (e: PixabayClientError) {
            Err(e)
        }
    }

    companion object : ClientContract.ClientFactory {
        private fun initPlugins(
            logger: ClientContract.Logger,
        ): Set<NetworkingContract.Plugin<in Any, in Any?>> {
            val jsonConfig = JsonConfigurator()
            Json { jsonConfig.configure(this) }

            @Suppress("UNCHECKED_CAST")
            return setOf(
                NetworkingContract.Plugin(
                    ContentNegotiation,
                    SerializerConfigurator(),
                    jsonConfig,
                ) as NetworkingContract.Plugin<in Any, in Any?>,
                NetworkingContract.Plugin(
                    Logging,
                    LoggingConfigurator(),
                    logger,
                ) as NetworkingContract.Plugin<in Any, in Any?>,
                NetworkingContract.Plugin(
                    HttpCallValidator,
                    ResponseValidatorConfigurator(),
                    HttpErrorMapper(),
                ) as NetworkingContract.Plugin<in Any, in Any?>,
            )
        }

        private fun initRequestBuilder(
            logger: ClientContract.Logger,
        ): NetworkingContract.RequestBuilderFactory {
            return RequestBuilder.Factory(
                client = HttpClient().config {
                    ClientConfigurator.configure(
                        this,
                        initPlugins(
                            logger
                        ),
                    )
                },
                host = HOST,
            )
        }

        override fun getInstance(
            apiToken: String,
            logger: ClientContract.Logger,
            connection: ClientContract.ConnectivityManager,
        ): ClientContract.Client {
            return PixabayClient(
                token = apiToken,
                requestBuilder = initRequestBuilder(
                    logger
                ),
                connectivityManager = connection,
            )
        }
    }
}
