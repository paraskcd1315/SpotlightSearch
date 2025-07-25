import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.paraskcd.spotlightsearch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.paraskcd.spotlightsearch"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    applicationVariants.all {
        val variant = this
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            val appName = variant.applicationId
            val formatter = SimpleDateFormat("yyyy-MM-dd-HH'h'mm'm'")
            val timestamp = formatter.format(Date())

            val branchName = try {
                Runtime.getRuntime()
                    .exec("git rev-parse --abbrev-ref HEAD")
                    .inputStream
                    .bufferedReader()
                    .readText()
                    .trim()
                    .replace("/", "_") // avoid invalid file names
            } catch (e: Exception) {
                "unknownBranch"
            }

            output.outputFileName = "$appName-$branchName-v-$timestamp.apk"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.appcompat:appcompat:1.7.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-compiler:2.56.2")

    // Hilt para ViewModel
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    implementation("com.google.accompanist:accompanist-drawablepainter:0.28.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")


    implementation("com.google.ai.edge.aicore:aicore:0.0.1-exp02")
}