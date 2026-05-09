import io.github.kei_1111.local.llm.sample.implementation
import io.github.kei_1111.local.llm.sample.library
import io.github.kei_1111.local.llm.sample.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("localllmsample.android.library.compose")
            }

            dependencies {
                implementation(project(":core:designsystem"))
                implementation(project(":core:ui"))

                implementation(libs.library("androidx.material3"))
                implementation(libs.library("koin.compose.viewmodel"))
                implementation(libs.library("androidx.navigation.compose"))
            }
        }
    }
}