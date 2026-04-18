package dev.nanite.naniteplugin.extensions

import dev.nanite.naniteplugin.minecraft.Utils
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

        val modVersion = (project.property("mod_version")
            ?: throw IllegalArgumentException("Project version is not set, please set it in your build.gradle file using the mod_version property")).toString()

        if (minecraftVersion.isEmpty()) {
            throw IllegalArgumentException("Minecraft version is not set")
        }

        if (modVersion.isEmpty()) {
            throw IllegalArgumentException("Mod version is not set")
        }

        if (modVersion.startsWith(minecraftVersion)) {
            throw IllegalArgumentException("Project version should not start with the Minecraft version, it will be automatically added. Current version: $modVersion")
        }

        return Utils.createModVersion(minecraftVersion, modVersion)
    }
}
