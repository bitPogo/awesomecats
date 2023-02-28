/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.config.repositories

import tech.antibytes.gradle.dependency.helper.AntibytesRepository
import tech.antibytes.gradle.dependency.helper.AntibytesUrl

private val githubGroups = listOf(
    "tech.antibytes.gradle",
    "tech.antibytes.kfixture",
    "tech.antibytes.test-utils-kmp",
    "tech.antibytes.kmock",
    "com.michael-bull.kotlin-result",
)

object Repositories {
    val pixabayRepositories = listOf(
        AntibytesRepository(
            AntibytesUrl.DEV,
            githubGroups,
        ),
        AntibytesRepository(
            AntibytesUrl.SNAPSHOT,
            githubGroups,
        ),
        AntibytesRepository(
            AntibytesUrl.ROLLING,
            githubGroups,
        ),
    )
}
