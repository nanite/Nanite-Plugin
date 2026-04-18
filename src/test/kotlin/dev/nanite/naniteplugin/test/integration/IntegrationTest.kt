package dev.nanite.naniteplugin.test.integration

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.intellij.lang.annotations.Language
import java.io.File

/**
 * Taken from https://github.com/modmuss50/mod-publish-plugin/blob/main/src/test/kotlin/me/modmuss50/mpp/test/IntegrationTest.kt
 * License: MIT (https://github.com/modmuss50/mod-publish-plugin/blob/main/LICENSE)
 * Thanks to modmuss50 for the code
 */
interface IntegrationTest {
    companion object {
        @Language("gradle")
        val kotlinHeader = """
            plugins {
                java
                id("dev.nanite.nanite-plugin")
            }
            
            // Injected empty task with a doLast to print the version
            tasks.register("emptyTask") {
                doLast { }
            }
        """.trimIndent()

        @Language("gradle")
        val groovyHeader = """
            plugins {
                id 'java'
                id 'dev.nanite.nanite-plugin'
            }
            
            // Injected empty task with a doLast to print the version
            tasks.register('emptyTask') {
                doLast { }
            }
        """.trimIndent()
    }

    fun gradleTest(groovy: Boolean = false): TestBuilder {
        return TestBuilder(groovy)
    }

    fun getOutPath(): String {
        return "build/intergation_test"
    }

    class TestBuilder(groovy: Boolean) {
        private val runner = GradleRunner.create()
            .withPluginClasspath()
            .forwardOutput()
            .withDebug(true)

        private val gradleHome: File
        private val projectDir: File
        private val buildScript: File
        private val gradleSettings: File
        private var arguments = ArrayList<String>()
        private val gradleProperties: File

        init {
            val testDir = File("build/intergation_test")
            val ext = if (groovy) { "" } else { ".kts" }
            gradleHome = File(testDir, "home")
            projectDir = File(testDir, "project")
            buildScript = File(projectDir, "build.gradle$ext")
            gradleSettings = File(projectDir, "settings.gradle$ext")
            gradleProperties = File(projectDir, "gradle.properties")

            projectDir.mkdirs()

            // Clean up
            File(projectDir, "build.gradle").delete()
            File(projectDir, "build.gradle.kts").delete()
            File(projectDir, "settings.gradle").delete()
            File(projectDir, "settings.gradle.kts").delete()
            File(projectDir, "gradle.properties").delete()

            buildScript(if (groovy) groovyHeader else kotlinHeader)

            gradleSettings.writeText("rootProject.name = \"nanite-example\"")

            runner.withProjectDir(projectDir)
            argument("--gradle-user-home", gradleHome.absolutePath)
            argument("--stacktrace")
            argument("--warning-mode", "fail")
            argument("clean")
        }

        // Appends to an existing buildscript
        fun buildScript(@Language("gradle") script: String): TestBuilder {
            buildScript.appendText(script + "\n")
            return this
        }

        fun subProject(name: String, @Language("gradle") script: String): TestBuilder {
            val subProjectDir = File(projectDir, name)

            if (subProjectDir.exists()) {
                subProjectDir.deleteRecursively()
            }

            subProjectDir.mkdirs()

            val subBuildScript = File(subProjectDir, "build.gradle.kts")
            subBuildScript.appendText(kotlinHeader + "\n")
            subBuildScript.appendText(script)

            gradleSettings.appendText("\ninclude(\"$name\")")

            return this
        }

        fun addProperty(key: String, value: String): TestBuilder {
            gradleProperties.appendText("$key=$value\n")
            return this
        }

        fun argument(vararg args: String) {
            arguments.addAll(args)
        }

        fun run(task: String): BuildResult {
            argument(task)
            runner.withArguments(arguments)
            return runner.run()
        }
    }
}
