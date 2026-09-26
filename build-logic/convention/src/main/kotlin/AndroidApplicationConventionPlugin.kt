import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("com.android.application")
        pluginManager.apply("org.jetbrains.kotlin.android")

        extensions.configure<ApplicationExtension> {
            compileSdk = SpotlightSdk.COMPILE
            defaultConfig {
                minSdk = SpotlightSdk.MIN
                targetSdk = SpotlightSdk.TARGET
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
