package dev.nanite.naniteplugin.test.minecraft

import dev.nanite.naniteplugin.minecraft.Utils
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {
    @Test
    fun `test correct version string creation`() {
        val modVersion = "5-beta"
        val resolveMap = mapOf(
            "1.20.4" to "20.4",
            "1.1.10" to "1.10",
            "26.1" to "26.1.0",
            "26.1.2" to "26.1.2",
        )

        for (entry in resolveMap) {
            val result = Utils.createModVersion(entry.key, modVersion)
            val expectedVersion = entry.value + "." + modVersion

            assertEquals(expectedVersion, result, "Failed to create correct mod version for Minecraft version ${entry.key}")
        }
    }
}
