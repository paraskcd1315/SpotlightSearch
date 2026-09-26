plugins {
    alias(libs.plugins.spotlight.android.library)
    alias(libs.plugins.spotlight.android.compose)
    alias(libs.plugins.spotlight.hilt)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.paraskcd.spotlightsearch.preferences"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(project(":designsystem"))
    implementation(project(":sources"))
    implementation(project(":search"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.reorderable)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
