// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt) apply false

    // Convention Plugins
    alias(libs.plugins.localllmsample.android.application) apply false
    alias(libs.plugins.localllmsample.android.feature) apply false
    alias(libs.plugins.localllmsample.android.library) apply false
    alias(libs.plugins.localllmsample.android.library.compose) apply false
    alias(libs.plugins.localllmsample.detekt) apply false
}