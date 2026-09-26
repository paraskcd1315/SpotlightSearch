plugins {
    alias(libs.plugins.spotlight.android.library)
    alias(libs.plugins.spotlight.android.compose)
}

android {
    namespace = "com.paraskcd.spotlightsearch.designsystem"
}

dependencies {
    api(libs.haze)
    api(libs.haze.blur)
    api(libs.icons.lucide)
    implementation(libs.androidx.activity.compose)
}
