/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk

import tech.antibytes.pixabay.sdk.error.PixabayClientError
import tech.antibytes.pixabay.sdk.model.PixabayResponse
import com.github.michaelbull.result.Result

object ClientContract {
    fun interface ConnectivityManager {
        fun hasConnection(): Boolean
    }

    interface Logger : io.ktor.client.plugins.logging.Logger {
        fun info(message: String)
        fun warn(message: String)
        fun error(exception: Throwable, message: String?)
    }

    interface Client {
        /**
         * Fetches an image from Pixabay
         * @param query - query string e.g. wounderful flower or cats
         * @param page - id of the result page, note the the start index is 1
         * @return Result with a PixabayResponse on success and PixabayClientError on error
         */
        suspend fun fetchImages(
            query: String,
            page: UShort,
        ): Result<PixabayResponse, PixabayClientError>
    }

    interface ClientFactory {
        /**
         * Builds an Client
         * @param apiToken - your Pixabay API token
         * @param logger - a logger implemented by you to keep track of things
         * @param connection - a barrier to not be sure the client is connected to the internet
         * @return Client
         */
        fun getInstance(
            apiToken: String,
            logger: Logger,
            connection: ConnectivityManager,
        ): Client
    }

    internal val ENDPOINT = listOf("api/")
    internal const val HOST = "pixabay.com"
    internal const val ITEMS_PER_PAGE = "200"
}
