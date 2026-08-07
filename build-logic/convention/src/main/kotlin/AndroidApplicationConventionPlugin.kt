import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.missclick.course_kmp.convention.configureKotlinAndroid
import ru.missclick.course_kmp.convention.libs

class AndroidApplicationConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension>() {
                namespace = "ru.missclick.course_kmp"

                defaultConfig {
                    applicationId = libs.findVersion("project-application-id").get().toString()
                    targetSdk = libs.findVersion("project-target-sdk").get().toString().toInt()
                    versionCode = libs.findVersion("project-version-code").get().toString().toInt()
                    versionName = libs.findVersion("project-version-name").get().toString()
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
                buildFeatures {
                    compose = true
                }

                configureKotlinAndroid(this)
            }
        }
    }
}