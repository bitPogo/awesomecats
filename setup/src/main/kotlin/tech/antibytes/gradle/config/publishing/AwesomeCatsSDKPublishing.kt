/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.config.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.BasePublishingConfiguration

open class AwesomeCatsSDKPublishing(
    project: Project,
) : BasePublishingConfiguration(project, "awesomecatssdk") {
    val description = "Just a demo  Multiplatform sdk."
    val url = "https://$gitHubRepositoryPath"
    val year = 2022
}
