package dev.nanite.naniteplugin.minecraft

object Utils {
    /**
     * Creates a "semver" like version from the Minecraft Version combined with the
     * project version assuming the project version omits the Minecraft Version
     */
    fun createModVersion(mcVersion: String, projectVersion: String): String {
        var mcVersionParts: List<String>

        val versionParts = mcVersion.split(".")
        if (versionParts[0] != "1") {
            // We're on the new 26+ year schema
            mcVersionParts = versionParts.let {
                // Is the version just something like 26.1
                if (it.size == 2) {
                    it + "0"
                } else {
                    it
                }
            }
        } else {
            mcVersionParts = versionParts.let {
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
        }

        // Finally, add the mod version to the end
        return mcVersionParts.joinToString(".") + "." + projectVersion
    }
}
