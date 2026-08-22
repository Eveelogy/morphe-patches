package app.evee.patches.gboard.rename

import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD
import org.w3c.dom.Element

@Suppress("unused")
val renameGboardPatch = resourcePatch(
    name = "Rename Gboard",
    description = "Changes the display name of Gboard in the keyboard switcher and settings.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    val customName by stringOption(
        key = "appName",
        title = "Keyboard name",
        description = "The display label for Gboard in system settings.",
        default = "Gboard (Morphe)"
    )

    execute {
        val appName = customName ?: "Gboard (Morphe)"

        document("AndroidManifest.xml").use { manifestDoc ->
            val appElements = manifestDoc.getElementsByTagName("application")
            if (appElements.length > 0) {
                val appElement = appElements.item(0) as? Element
                appElement?.setAttribute("android:label", appName)
            }

            val serviceNodes = manifestDoc.getElementsByTagName("service")
            for (i in 0 until serviceNodes.length) {
                val sNode = serviceNodes.item(i) as? Element
                if (sNode?.getAttribute("android:name") == "com.android.inputmethod.latin.LatinIME") {
                    sNode.setAttribute("android:label", appName)
                }
            }

            val activityNodes = manifestDoc.getElementsByTagName("activity")
            for (i in 0 until activityNodes.length) {
                val aNode = activityNodes.item(i) as? Element
                if (aNode?.getAttribute("android:name")?.contains("LauncherActivity") == true) {
                    aNode.setAttribute("android:label", appName)
                }
            }
        }
    }
}
