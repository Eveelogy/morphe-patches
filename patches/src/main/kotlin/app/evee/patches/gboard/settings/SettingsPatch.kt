package app.evee.patches.gboard.settings

import app.morphe.patcher.patch.resourcePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD

@Suppress("unused")
val settingsPatch = resourcePatch(
    name = "Morphe Settings",
    description = "Adds a Morphe Settings category in Gboard settings to configure patch options.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    execute {
        listOf("res/xml/settings.xml", "res/xml/settings_legacy.xml").forEach { path ->
            try {
                document(path).use { doc ->
                    val root = doc.documentElement ?: return@forEach

                    val category = doc.createElement("androidx.preference.PreferenceCategory").apply {
                        setAttribute("android:title", "Morphe Settings")
                        setAttribute("android:order", "999")
                    }

                    val safeSearchPref = doc.createElement("androidx.preference.SwitchPreferenceCompat").apply {
                        setAttribute("android:title", "Disable GIF SafeSearch")
                        setAttribute("android:summary", "Allow unrestricted Tenor GIF search results")
                        setAttribute("android:key", "disable_gif_safesearch")
                        setAttribute("android:defaultValue", "true")
                    }
                    category.appendChild(safeSearchPref)

                    val memeSearchPref = doc.createElement("androidx.preference.SwitchPreferenceCompat").apply {
                        setAttribute("android:title", "Meme Search & Maker")
                        setAttribute("android:summary", "Replace emoticon tab with still image meme search & maker")
                        setAttribute("android:key", "replace_emoticon_with_meme_search")
                        setAttribute("android:defaultValue", "true")
                    }
                    category.appendChild(memeSearchPref)

                    root.appendChild(category)
                }
            } catch (ignored: Exception) {
            }
        }
    }
}
