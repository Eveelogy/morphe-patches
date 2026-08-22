package app.evee.patches.gboard.clone

import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD
import app.evee.patches.gboard.shared.Constants.GBOARD_CLONED_PACKAGE_NAME
import app.evee.patches.gboard.shared.Constants.GBOARD_PACKAGE_NAME
import org.w3c.dom.Element

@Suppress("unused")
val cloneGboardPatch = resourcePatch(
    name = "Clone Gboard",
    description = "Allows Gboard to be installed alongside the official app.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    val clonedPackage by stringOption(
        key = "clonedPackageName",
        title = "Cloned package name",
        description = "The unique package name for the cloned Gboard app.",
        default = GBOARD_CLONED_PACKAGE_NAME
    )

    execute {
        val targetPackage = clonedPackage ?: GBOARD_CLONED_PACKAGE_NAME
        val manifestDoc = document("AndroidManifest.xml")
        val manifestElement = manifestDoc.documentElement

        // Update package name
        manifestElement.setAttribute("package", targetPackage)

        // Update all provider authorities and permissions in the manifest
        val nodeList = manifestElement.getElementsByTagName("*")
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node is Element) {
                // Update authorities
                if (node.hasAttribute("android:authorities")) {
                    val auth = node.getAttribute("android:authorities")
                    node.setAttribute(
                        "android:authorities",
                        auth.replace(GBOARD_PACKAGE_NAME, targetPackage)
                    )
                }

                // Update custom permissions defined or used
                if (node.hasAttribute("android:name")) {
                    val nameAttr = node.getAttribute("android:name")
                    if (nameAttr.startsWith(GBOARD_PACKAGE_NAME)) {
                        node.setAttribute(
                            "android:name",
                            nameAttr.replace(GBOARD_PACKAGE_NAME, targetPackage)
                        )
                    }
                }

                if (node.hasAttribute("android:permission")) {
                    val permAttr = node.getAttribute("android:permission")
                    if (permAttr.startsWith(GBOARD_PACKAGE_NAME)) {
                        node.setAttribute(
                            "android:permission",
                            permAttr.replace(GBOARD_PACKAGE_NAME, targetPackage)
                        )
                    }
                }
            }
        }
    }
}
