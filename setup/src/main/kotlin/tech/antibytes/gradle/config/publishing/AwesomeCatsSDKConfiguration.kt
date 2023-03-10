/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.config.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration

class AwesomeCatsSDKConfiguration(project: Project) : PublishingBase() {
    val publishing = Publishing(project)

    class Publishing(project: Project) : AwesomeCatsSDKPublishing(project) {
        val packageConfiguration = PackageConfiguration(
            pom = PomConfiguration(
                name = "AwesomeCatsSDK",
                description = description,
                year = year,
                url = url,
            ),
            developers = listOf(developer),
            license = license,
            scm = sourceControl,
        )
    }
}
