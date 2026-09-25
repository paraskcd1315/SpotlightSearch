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
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.accompanist.drawablepainter)
    implementation(libs.okhttp)
    implementation(libs.mlkit.translate)
    implementation(libs.mlkit.language.id)
    implementation(libs.symspellkt)
    implementation(libs.symspellkt.fdic.android)
    implementation(libs.burnoutcrew.reorderable)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}
