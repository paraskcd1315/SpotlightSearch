import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        pluginManager.withPlugin("com.android.library") {
            extensions.configure<LibraryExtension> { buildFeatures.compose = true }
        }
        pluginManager.withPlugin("com.android.application") {
            extensions.configure<ApplicationExtension> { buildFeatures.compose = true }
        }

        val bom = dependencies.platform(catalog.library("androidx-compose-bom"))
        dependencies.add("implementation", bom)
        dependencies.add("androidTestImplementation", bom)
        dependencies.add("implementation", catalog.library("androidx-ui"))
        dependencies.add("implementation", catalog.library("androidx-ui-graphics"))
        dependencies.add("implementation", catalog.library("androidx-ui-tooling-preview"))
        dependencies.add("implementation", catalog.library("androidx-material3"))
        dependencies.add("debugImplementation", catalog.library("androidx-ui-tooling"))
    }
}
