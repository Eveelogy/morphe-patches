package app.evee.patches.gboard.settings

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal

object PreferenceHeaderFragmentFingerprint : Fingerprint(
    definingClass = "Lcom/google/android/libraries/inputmethod/preferencewidgets/PreferenceHeaderFragment;",
    name = "aC",
    returnType = "V",
    filters = listOf(
        literal(0x7f140b50)
    )
)
