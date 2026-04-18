package dev.nanite.naniteplugin.types

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import java.util.regex.Pattern
import javax.inject.Inject

abstract class ChangeLogData @Inject constructor(project: Project) {
    @get:InputFile
    val file: RegularFileProperty = project.objects.fileProperty()

    @get:Input
    val version: Property<String> = project.objects.property(String::class.java)

    @get:Input
    val fallbackValue: Property<String> = project.objects.property(String::class.java)

    @get:Input
    val versionPattern: Property<Pattern> = project.objects.property(Pattern::class.java)

    init {
        versionPattern.convention(Regex("## \\[[^]]+]").toPattern())
        fallbackValue.convention("No changelog data found")
    }
}
