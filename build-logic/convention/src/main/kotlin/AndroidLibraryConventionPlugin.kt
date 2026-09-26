import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.android")

        extensions.configure<LibraryExtension> {
            compileSdk = SpotlightSdk.COMPILE
            defaultConfig {
                minSdk = SpotlightSdk.MIN
                testInstrumentationRunner = SpotlightSdk.TEST_RUNNER
            }
            compileOptions {
                sourceCompatibility = SpotlightSdk.JAVA
                targetCompatibility = SpotlightSdk.JAVA
            }
        }
        configureKotlinAndroid()
    }
}
