package dev.nanite.naniteplugin.tasks

import org.gradle.api.DefaultTask
import javax.inject.Inject

abstract class NaniteTask @Inject constructor() : DefaultTask() {
    init {
        group = "nanite"
    }
}
