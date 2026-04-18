package dev.nanite.naniteplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reflect.TypeOf
import dev.nanite.naniteplugin.extensions.NaniteExtension
import dev.nanite.naniteplugin.extensions.NaniteUtils
import dev.nanite.naniteplugin.tasks.CreateChangelogTask

@Suppress("unused")
class NanitePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val commonExtension = project.extensions.create(TypeOf.typeOf(NaniteExtension::class.java), "nanite", NaniteExtension::class.java, project)
        val utilsExtension = project.extensions.create(TypeOf.typeOf(NaniteUtils::class.java), "naniteUtils", NaniteUtils::class.java, project)

        project.tasks.create("createChangelog", CreateChangelogTask::class.java)
    }
}

