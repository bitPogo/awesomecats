/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.pixabay.sdk.error

sealed class PixabayClientError(
    message: String? = null,
    cause: Throwable? = null,
) : Error(message, cause) {
    class NoConnection : PixabayClientError()
    class RequestError(val status: Int) : PixabayClientError()
    class RequestValidationFailure(message: String) : PixabayClientError(message)
    class ResponseTransformFailure : PixabayClientError(message = "Unexpected Response")
}
