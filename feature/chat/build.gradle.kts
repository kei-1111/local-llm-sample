plugins {
    alias(libs.plugins.localllmsample.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.kei_1111.local.llm.sample.feature.chat"
}

dependencies {
    implementation(projects.core.llm)
    implementation(libs.kotlinx.serialization.json)
}
