import com.android.build.api.artifact.SingleArtifact
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.spotlight.android.application)
    alias(libs.plugins.spotlight.android.compose)
    alias(libs.plugins.spotlight.hilt)
}

val benchSuffix = ".bench"

android {
    namespace = "com.paraskcd.spotlightsearch"

    defaultConfig {
        applicationId = "com.paraskcd.spotlightsearch"
        versionCode = 19
        versionName = "2.1.0"
        resValue("string", "app_name", "Spotlight Search")
    }

    buildFeatures {
        resValues = true
    }

    androidResources {
        generateLocaleConfig = true
        localeFilters += listOf("en", "es", "de", "fr", "nl", "ca", "it", "pt", "sv", "ru", "hi", "ur")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Spotlight Search Dev")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            applicationIdSuffix = benchSuffix
            resValue("string", "app_name", "Spotlight Search Bench")
        }
        create("nonMinified") {
            initWith(getByName("benchmark"))
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

androidComponents {
    onVariants { variant ->
        val taskSuffix = variant.name.replaceFirstChar { it.uppercase() }
        val apkFolder = variant.artifacts.get(SingleArtifact.APK)
        val loader = variant.artifacts.getBuiltArtifactsLoader()
        val appId = variant.applicationId
        val namedFolder = layout.buildDirectory.dir("outputs/named-apk/${variant.name}")
        val copyTask = tasks.register("copyNamed${taskSuffix}Apk") {
            inputs.files(apkFolder)
            outputs.dir(namedFolder)
            doLast {
                val timestamp = SimpleDateFormat("yyyy-MM-dd-HH'h'mm'm'").format(Date())
                val target = namedFolder.get().asFile
                loader.load(apkFolder.get())?.elements?.forEach { element ->
                    file(element.outputFile).copyTo(target.resolve("${appId.get()}-v-$timestamp.apk"), overwrite = true)
                }
            }
        }
        tasks.matching { it.name == "assemble$taskSuffix" }.configureEach { finalizedBy(copyTask) }
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
    implementation(libs.androidx.profileinstaller)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    ksp(libs.androidx.hilt.compiler)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}
