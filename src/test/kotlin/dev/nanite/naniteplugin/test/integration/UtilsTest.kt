package dev.nanite.naniteplugin.test.integration

import kotlin.test.Test
import kotlin.test.assertTrue

class UtilsTest : IntegrationTest {
    @Test
    fun `test correct version string creation`() {
        val runner = gradleTest()
            .buildScript(
                """
                    version = "4"
                    
                    nanite {
                        minecraftVersion.set("1.21.1")
                    }
                    
                    println("MOD_VERSION=" + naniteUtils.createModVersion())
                """.trimIndent()
            ).run("emptyTask")

        val resultText = runner.output

        assertTrue(resultText.contains("MOD_VERSION=21.1.4"))
    }

    @Test
    fun `test correct version from project versions`() {
        val runner = gradleTest()
            .buildScript(
                """
                    version = "4"
                    
                    nanite {}
                    
                    println("MOD_VERSION=" + naniteUtils.createModVersion())
                """.trimIndent()
            )
            .addProperty("minecraft_version", "1.21.1")
            .run("emptyTask")

        val resultText = runner.output

        assertTrue(resultText.contains("MOD_VERSION=21.1.4"))
    }

    @Test
    fun `test correct version with non-standard semver minecraft version`() {
        val runner = gradleTest()
            .buildScript(
                """
                    version = "5"
                    
                    nanite {
                        minecraftVersion.set("1.21")
                    }
                    
                    println("MOD_VERSION=" + naniteUtils.createModVersion())
                """.trimIndent()
            )
            .run("emptyTask")

        val resultText = runner.output

        assertTrue(resultText.contains("MOD_VERSION=21.0.5"))
    }
}
