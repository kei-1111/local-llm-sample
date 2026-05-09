import com.android.build.api.dsl.ApplicationExtension
import io.github.kei_1111.local.llm.sample.configureAndroidCompose
import io.github.kei_1111.local.llm.sample.configureAndroidKotlin
import io.github.kei_1111.local.llm.sample.implementation
import io.github.kei_1111.local.llm.sample.libs
import io.github.kei_1111.local.llm.sample.versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("localllmsample.detekt")
            }

            dependencies {
                implementation(project(":core:designsystem"))
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidKotlin(this)
                configureAndroidCompose(this)

                defaultConfig.targetSdk = libs.versions("targetSdk").toInt()

                packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}