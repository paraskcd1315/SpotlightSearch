plugins {
    alias(libs.plugins.spotlight.android.library)
    alias(libs.plugins.spotlight.android.compose)
    alias(libs.plugins.spotlight.hilt)
}

android {
    namespace = "com.paraskcd.spotlightsearch.preferences"
}

dependencies {
    implementation(project(":designsystem"))
    implementation(project(":sources"))
    implementation(project(":search"))
    implementation(libs.kotlinx.coroutines.android)
}
