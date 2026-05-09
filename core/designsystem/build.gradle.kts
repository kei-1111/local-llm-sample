plugins {
    alias(libs.plugins.localllmsample.android.library.compose)
}

android {
    namespace = "io.github.kei_1111.local.llm.sample.core.designsystem"
}

dependencies {
    implementation(libs.androidx.material3)
}