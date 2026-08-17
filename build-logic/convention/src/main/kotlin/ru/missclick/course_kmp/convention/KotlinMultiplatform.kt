package ru.missclick.course_kmp.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform() {
    configureAndroidLibraryTarget()

    extensions.configure<KotlinMultiplatformExtension> {
        extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
            namespace = this@configureKotlinMultiplatform.pathToPackageName()
            compileSdk = libs.findVersion("project-compile-sdk").get().toString().toInt()
            minSdk = libs.findVersion("project-min-sdk").get().toString().toInt()
        }

        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = this@configureKotlinMultiplatform.pathToFrameworkName()
                isStatic = true
            }
        }

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        }
    }
}
