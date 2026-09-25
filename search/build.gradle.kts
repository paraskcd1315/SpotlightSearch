plugins {
    alias(libs.plugins.spotlight.android.library)
    alias(libs.plugins.spotlight.android.compose)
    alias(libs.plugins.spotlight.hilt)
}

android {
    namespace = "com.paraskcd.spotlightsearch.search"
}

dependencies {
    implementation(project(":designsystem"))
    implementation(project(":sources"))
    implementation(libs.kotlinx.coroutines.android)
}
