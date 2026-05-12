plugins {
    alias(libs.plugins.localllmsample.android.library)
}

android {
    namespace = "io.github.kei_1111.local.llm.sample.core.llm"
}

dependencies {
    implementation(libs.mlkit.genai.prompt)
    implementation(libs.koin.android)
}
