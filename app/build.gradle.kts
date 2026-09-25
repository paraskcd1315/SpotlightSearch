import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.spotlight.android.application)
    alias(libs.plugins.spotlight.android.compose)
    alias(libs.plugins.spotlight.hilt)
}

android {
    namespace = "com.paraskcd.spotlightsearch"

    defaultConfig {
        applicationId = "com.paraskcd.spotlightsearch"
        versionCode = 17
        versionName = "1.2.5"
        resValue("string", "app_name", "Spotlight Search")
    }

    buildFeatures {
        resValues = true
    }

    applicationVariants.all {
        val variant = this
        outputs.all {
            val output = this as ApkVariantOutputImpl
            val appName = variant.applicationId
            val formatter = SimpleDateFormat("yyyy-MM-dd-HH'h'mm'm'")
            val timestamp = formatter.format(Date())

            output.outputFileName = "$appName-v-$timestamp.apk"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Spotlight Search Dev")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            applicationIdSuffix = ".bench"
            resValue("string", "app_name", "Spotlight Search Bench")
        }
    }
}

dependencies {
    implementation(project(":designsystem"))
    implementation(project(":sources"))
    implementation(project(":search"))
    implementation(project(":preferences"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}
