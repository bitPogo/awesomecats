/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk.networking

import tech.antibytes.pixabay.sdk.error.PixabayClientError
import tech.antibytes.pixabay.sdk.networking.plugin.KtorPluginsContract
import io.ktor.client.plugins.ResponseException

internal class HttpErrorMapper : KtorPluginsContract.ErrorMapper {
    private fun wrapError(error: Throwable): Throwable {
        return if (error is ResponseException) {
            PixabayClientError.RequestError(error.response.status.value)
        } else {
            error
        }
    }

    override fun mapAndThrow(error: Throwable) {
        throw wrapError(error)
    }
}
