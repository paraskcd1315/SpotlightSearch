plugins {
    alias(libs.plugins.spotlight.android.library)
    alias(libs.plugins.spotlight.hilt)
}

android {
    namespace = "com.paraskcd.spotlightsearch.sources"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
