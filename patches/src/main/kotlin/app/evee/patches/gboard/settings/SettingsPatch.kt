package app.evee.patches.gboard.settings

import app.morphe.patcher.patch.resourcePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD

@Suppress("unused")
val settingsPatch = resourcePatch(
    name = "Morphe Settings",
    description = "Adds a dedicated Morphe Settings sub-menu in Gboard settings.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    execute {
        listOf("res/xml/settings.xml", "res/xml/settings_legacy.xml").forEach { path ->
            try {
                document(path).use { doc ->
                    val root = doc.documentElement ?: return@forEach

                    val category = doc.createElement("androidx.preference.PreferenceCategory").apply {
                        setAttribute("android:order", "999")
                    }

                    val headerPref = doc.createElement("com.google.android.libraries.inputmethod.settings.widget.HeaderPreference").apply {
                        setAttribute("android:title", "Morphe Settings")
                        setAttribute("android:key", "morphe_settings")
                        setAttribute("android:fragment", "app.evee.extension.gboard.MorpheSettingsFragment")
                        setAttribute("android:persistent", "false")
                        setAttribute("android:icon", "?attr/_0_resource_name_obfuscated_res_0x7f0401c1")
                    }
                    category.appendChild(headerPref)

                    root.appendChild(category)
                }
            } catch (ignored: Exception) {
            }
        }
    }
}
