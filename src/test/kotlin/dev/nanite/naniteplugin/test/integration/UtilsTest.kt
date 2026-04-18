package dev.nanite.naniteplugin.test.integration

import kotlin.test.Test
import kotlin.test.assertTrue

class UtilsTest : IntegrationTest {
    @Test
    fun `test correct version string creation`() {
        val runner = gradleTest()
            .buildScript(
                """                   
                    nanite {
                        minecraftVersion.set("1.21.1")
                    }
                    
                    println("MOD_VERSION=" + naniteUtils.createModVersion())
                """.trimIndent()
            )
                .addProperty("mod_version", "4")
                .run("emptyTask")

        val resultText = runner.output

        assertTrue(resultText.contains("MOD_VERSION=21.1.4"))
    }

    @Test
    fun `test correct version with modern minecraft versioning`() {
        val runner = gradleTest()
            .buildScript(
                """                   
                    nanite {
                        minecraftVersion.set("26.1")
                    }
                    
                    println("MOD_VERSION=" + naniteUtils.createModVersion())
                """.trimIndent()
            )
            .addProperty("mod_version", "5")
            .run("emptyTask")

        val resultText = runner.output

        assertTrue(resultText.contains("MOD_VERSION=26.1.0.5"))
    }
}
