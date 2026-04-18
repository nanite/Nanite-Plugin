package dev.nanite.naniteplugin.extensions

import org.gradle.api.Project

abstract class NaniteUtils(private val project: Project) {
    /**
     * Takes in a suffix of the mod version, then uses the projects Minecraft version to create a full mod version.
     *
     * This handles the weird edge case that Minecraft creates with its inconsistent versioning. When ever we see a
     * version without a third number, we automatically add a 0 to the end.
     */
    fun createModVersion(): String {
        val ext = NaniteExtension.extension(project)
        val minecraftVersion = ext.minecraftVersion.get()

        val version = project.version.toString()

        if (minecraftVersion.isEmpty()) {
            throw IllegalArgumentException("Minecraft version is not set")
        }

        if (version.isEmpty()) {
            throw IllegalArgumentException("Mod version is not set")
        }

        val mcVersionParts = minecraftVersion.split(".").let {
            // Remove the first part of the version
            it.subList(1, it.size)
        }.let {
            // Add a 0 to the end if the version is only 1 part
            if (it.size == 1) {
                it + "0"
            } else {
                it
            }
        }

        // Finally, add the mod version to the end
        return mcVersionParts.joinToString(".") + "." + version
    }
}
