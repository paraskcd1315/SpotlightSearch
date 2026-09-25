import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object SpotlightSdk {
    const val COMPILE = 36
    const val MIN = 34
    const val TARGET = 36
    val JAVA = JavaVersion.VERSION_11
    val JVM_TARGET = JvmTarget.JVM_11
    const val TEST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
}
