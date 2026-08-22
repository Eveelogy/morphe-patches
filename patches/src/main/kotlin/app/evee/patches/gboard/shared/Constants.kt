package app.evee.patches.gboard.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val GBOARD_PACKAGE_NAME = "com.google.android.inputmethod.latin"
    const val GBOARD_CLONED_PACKAGE_NAME = "com.google.android.inputmethod.latin.morphe"
    const val EXTENSION_CLASS_GBOARD = "Lapp/evee/extension/gboard/GboardSettings;"

    val COMPATIBILITY_GBOARD = Compatibility(
        name = "Gboard",
        packageName = GBOARD_PACKAGE_NAME,
        apkFileType = ApkFileType.APK,
        appIconColor = 0x4285F4,
        targets = listOf(
            AppTarget(
                version = "18.0.3",
                isExperimental = false
            ),
            AppTarget(
                version = null,
                isExperimental = true
            )
        )
    )
}
