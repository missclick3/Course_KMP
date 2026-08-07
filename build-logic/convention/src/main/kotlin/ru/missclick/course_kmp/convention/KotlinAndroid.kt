package ru.missclick.course_kmp.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension
) {
    with(commonExtension) {
        compileSdk = libs.findVersion("project-compile-sdk").get().toString().toInt()

        defaultConfig.minSdk = libs.findVersion("project-min-sdk").get().toString().toInt()

        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17
        compileOptions.isCoreLibraryDesugaringEnabled = true

        configureKotlin()

        dependencies {
            add(
                "coreLibraryDesugaring",
                libs.findLibrary("android-desugar-jdk-libs").get()
            )
        }
    }
}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
    }
}