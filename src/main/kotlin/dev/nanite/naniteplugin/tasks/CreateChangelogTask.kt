package dev.nanite.naniteplugin.tasks

import dev.nanite.naniteplugin.extensions.NaniteExtension
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import kotlin.io.forEachLine
import kotlin.text.toRegex

abstract class CreateChangelogTask @Inject constructor() : NaniteTask() {
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        outputFile.convention(
            project.rootProject.layout.buildDirectory.file("generated-changelog.md")
        )
    }

    @TaskAction
    fun createChangelog() {
        // Pull the changelog settings from the plugin extension
        val data = NaniteExtension.changelog(project)

        // Get the changelog text or try the file value
        val changelogFile = data.file.asFile
        if (!changelogFile.get().exists()) {
            throw IllegalArgumentException("Changelog file does not exist: ${changelogFile.get().path}")
        }

        val version = data.version.getOrElse(project.version.toString())
        val versionPattern = data.versionPattern.get()

        var versionData = ""
        var isReading = false
        changelogFile.get().forEachLine { line ->
            if (line.matches(versionPattern.toRegex())) {
                isReading = version in line
                if (!isReading) {
                    return@forEachLine
                }
            }

            if (isReading) {
                versionData += line + "\n"
            }
        }

        val finalChangelog = versionData.trim().takeIf { it.isNotEmpty() }
            ?: data.fallbackValue.get()

        val output = outputFile.asFile.get()
        output.parentFile.mkdirs()
        output.writeText(finalChangelog)
    }
}
