package dev.nanite.naniteplugin.extensions

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import dev.nanite.naniteplugin.types.ChangeLogData
import javax.inject.Inject

abstract class NaniteExtension @Inject constructor(private val project: Project) {
    val changelog: ChangeLogData = project.extensions.create(
        "changelog",
        ChangeLogData::class.java,
        project
    )

    @get:Input
    val minecraftVersion: Property<String> = project.objects.property(String::class.java).apply {
        convention(project.properties["minecraft_version"] as String? ?: "")
    }

    // Static method to create the extension
    companion object {
        fun changelog(project: Project): ChangeLogData {
            return project.extensions.getByType(NaniteExtension::class.java).changelog
        }

        fun extension(project: Project): NaniteExtension {
            return project.extensions.getByType(NaniteExtension::class.java)
        }
    }
}
