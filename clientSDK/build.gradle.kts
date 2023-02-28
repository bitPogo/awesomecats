/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.configuration.runtime.AntiBytesTestConfigurationTask
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import tech.antibytes.gradle.config.publishing.AwesomeCatsSDKConfiguration
import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.setupAndroidTest
import java.util.Properties
import tech.antibytes.gradle.configuration.sourcesets.appleWithLegacy

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)

    id(antibytesCatalog.plugins.kotlin.parcelize.get().pluginId)

    alias(libs.plugins.kmock)

    // Serialization
    alias(antibytesCatalog.plugins.kotlinx.serialization)
}

val publishingConfiguration = AwesomeCatsSDKConfiguration(project)
group = publishingConfiguration.group

antibytesPublishing {
    packaging.set(publishingConfiguration.publishing.packageConfiguration)
    repositories.set(publishingConfiguration.publishing.repositories)
    versioning.set(publishingConfiguration.publishing.versioning)
}

kotlin {
    android()

    jvm()

    js(IR) {
        compilations {
            this.forEach {
                it.compileKotlinTask.kotlinOptions.sourceMap = true
                it.compileKotlinTask.kotlinOptions.metaInfo = true

                if (it.name == "main") {
                    it.compileKotlinTask.kotlinOptions.main = "call"
                }
            }
        }

        browser {
            testTask {
                useKarma {
                    useChromeHeadlessNoSandbox()
                }
            }
        }
    }

    appleWithLegacy()
    linuxX64()
    ensureAppleDeviceCompatibility()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.serialization.InternalSerializationApi")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
                implementation(antibytesCatalog.common.kotlinx.coroutines.core)
                implementation(antibytesCatalog.common.ktor.client.core)
                implementation(antibytesCatalog.common.ktor.client.logging)
                implementation(antibytesCatalog.common.ktor.client.contentNegotiation)
                implementation(antibytesCatalog.common.ktor.client.json)
                implementation(antibytesCatalog.common.ktor.serialization.json)

                implementation(antibytesCatalog.common.kotlinx.serialization.core)
                implementation(antibytesCatalog.common.kotlinx.serialization.json)
                implementation(libs.kotlinResult)

            }
        }
        val commonTest by getting {
            kotlin.srcDir("${buildDir.absolutePath.trimEnd('/')}/generated/antibytes/commonTest/kotlin")

            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.common.test.ktor.client.mockClient)

                implementation(libs.kmock)
                implementation(libs.kfixture)
                implementation(libs.testUtils.core)
                implementation(libs.testUtils.annotations)
                implementation(libs.testUtils.coroutine)
                implementation(libs.testUtils.ktor)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)
                implementation(antibytesCatalog.android.ktor.client)
                implementation(antibytesCatalog.jvm.ktor.client.okhttp)
            }
        }

        setupAndroidTest()
        val androidTest by getting {
            dependencies {
                implementation(antibytesCatalog.android.test.junit.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
                implementation(antibytesCatalog.android.test.ktx)
                implementation(antibytesCatalog.android.test.robolectric)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk)
                implementation(antibytesCatalog.jvm.ktor.client.core)
                implementation(antibytesCatalog.jvm.ktor.client.okhttp)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.junit.junit4)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
                implementation(antibytesCatalog.js.ktor.client.core)
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)
            }
        }

        val appleMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.ktor.client.cio)
            }
        }

        val linuxX64Main by getting {
            dependencies {
                implementation(antibytesCatalog.common.ktor.client.cio)
            }
        }
    }
}

val moduleSpace = "tech.antibytes.pixabay.sdk"

android {
    namespace = moduleSpace

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kmock {
    rootPackage = moduleSpace
    freezeOnDefault = false
    allowInterfaces = true
}

tasks.withType(Test::class.java) {
    testLogging {
        events(FAILED)
    }
}

val apiKey: String = if(project.ext.has("localProperties")) {
    (project.ext["localProperties"] as? Properties)?.get("gpr.pixabay.apikey").toString()
} else {
    project.properties["gpr.pixabay.apikey"].toString()
}

val provideTestConfig: Task by tasks.creating(AntiBytesTestConfigurationTask::class) {
    packageName.set("$moduleSpace.test.config")
    this.stringFields.set(
        mapOf(
            "projectDir" to projectDir.toPath().toAbsolutePath().toString(),
            "apiKey" to apiKey,
        )
    )
}

tasks.withType(KotlinCompile::class.java) {
    if (this.name.contains("Test")) {
        this.dependsOn(provideTestConfig)
    }
}
