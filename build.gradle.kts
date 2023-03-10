/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.config.repositories.Repositories.pixabayRepositories
import tech.antibytes.gradle.dependency.helper.addCustomRepositories
import tech.antibytes.gradle.dependency.helper.ensureKotlinVersion
import tech.antibytes.gradle.config.publishing.AwesomeCatsSDKPublishing
import java.util.Properties

plugins {
    id("tech.antibytes.gradle.setup")

    alias(antibytesCatalog.plugins.gradle.antibytes.dependencyHelper)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
}

fun Project.loadLocalProperties() {
    val localProperties = file("$rootDir/local.properties")

    if (localProperties.isFile) {
        @Suppress("UNCHECKED_CAST")
        val properties = Properties().apply {
            load(localProperties.reader(Charsets.UTF_8))
        }
        properties.remove("sdk.dir")

        ext["localProperties"] = properties
    }
}

val publishing = AwesomeCatsSDKPublishing(project)

antibytesPublishing {
    versioning.set(publishing.versioning)
    repositories.set(publishing.repositories)
}

allprojects {
    loadLocalProperties()
    repositories {
        mavenCentral()
        google()
        jcenter()
        addCustomRepositories(pixabayRepositories)
    }
    ensureKotlinVersion()
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.5.1"
    distributionType = Wrapper.DistributionType.ALL
}
