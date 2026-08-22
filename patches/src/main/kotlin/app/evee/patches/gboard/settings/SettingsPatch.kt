package app.evee.patches.gboard.settings

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD
import app.evee.patches.gboard.shared.Constants.EXTENSION_CLASS_GBOARD

@Suppress("unused")
val settingsPatch = bytecodePatch(
    name = "Morphe Settings",
    description = "Adds a Morphe Settings category in Gboard settings to configure patch options.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    extendWith("extensions/extension.mpe")

    execute {
        PreferenceHeaderFragmentFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p0}, $EXTENSION_CLASS_GBOARD->addMorphePreferenceCategory(Ljava/lang/Object;)V
            """
        )
    }
}
