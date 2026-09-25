import com.android.build.api.dsl.TestExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("com.android.test")
        pluginManager.apply("org.jetbrains.kotlin.android")

        extensions.configure<TestExtension> {
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
        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(SpotlightSdk.JVM_TARGET)
            }
        }
    }
}
