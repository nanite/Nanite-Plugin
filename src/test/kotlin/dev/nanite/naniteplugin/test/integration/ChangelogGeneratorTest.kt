package dev.nanite.naniteplugin.test.integration

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChangelogGeneratorTest : IntegrationTest {
    @BeforeTest
    fun setup() {
        val outputPath = getOutPath() + "/project"
        // Make the dir if needed
        File(outputPath).mkdirs()

        // Move all the changelogs to the output path
        val changelogs = listOf("changelog-basic.md", "changelog-requires-splitting.md", "changelog-no-valid-target.md")
        changelogs.forEach {
            val changelogDataUrl = javaClass.getResource("/$it")
            val changelogData = changelogDataUrl?.file ?: throw Exception("Failed to load changelog data for $it")
            val fileText = File(changelogData).readText()

            // Copy the file to the output path
            File("$outputPath/$it").writeText(fileText)
        }
    }

    @Test
    fun `test single version changelog generation`() {
        val result = createBaseGradleTask("changelog-basic.md")
        val fileOutput = result.output

        assertEquals(TaskOutcome.SUCCESS, result.task(":createChangelogTest")?.outcome)
//        assertTrue(resultText.contains("[1.0.0]"))
    }

    @Test
    fun `test snipping changelog generation`() {
        val result = createBaseGradleTask("changelog-requires-splitting.md")
        val resultText = result.output

        assertEquals(TaskOutcome.SUCCESS, result.task(":createChangelogTest")?.outcome)
        assertTrue(resultText.contains("[1.0.0]"))
        assertTrue(!resultText.contains("[0.1.0]"))
    }

    @Test
    fun `test fallback changelog generation`() {
        val result = createBaseGradleTask("changelog-no-valid-target.md", true)
        val resultText = result.output

        assertEquals(TaskOutcome.SUCCESS, result.task(":createChangelogTest")?.outcome)
        assertTrue(!resultText.contains("[1.0.0]"))
        assertTrue(!resultText.contains("[0.1.0]"))
        assertTrue(resultText.contains("No changelog data found"))
    }

    private fun createBaseGradleTask(changelogLocation: String, includeAllArgs: Boolean = false): BuildResult {
        val extraArgs = """
            versionPattern.set(Regex("## \\[[^]]+]").toPattern())
            fallbackValue.set("No changelog data found")
        """.trimIndent()

        return gradleTest()
            .buildScript(
                """                    
                    nanite {
                        changelog {
                            
                        }
                    }
                   
                """.trimIndent()
            ).run("emptyTask")
    }
}
