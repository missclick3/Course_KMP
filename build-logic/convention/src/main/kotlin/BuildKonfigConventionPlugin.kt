import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.missclick.course_kmp.convention.pathToPackageName

class BuildKonfigConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            extensions.configure<BuildKonfigExtension> {
                packageName.set(target.pathToPackageName())
                defaultConfigs {
                    val apiKey = gradleLocalProperties(rootDir, rootProject.providers)
                        .getProperty("API_KEY")
                        ?: throw IllegalStateException("no API_KEY property provided")
                    buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
                }
            }
        }
    }

}