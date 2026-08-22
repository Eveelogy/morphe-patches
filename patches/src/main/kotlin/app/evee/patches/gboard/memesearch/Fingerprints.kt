package app.evee.patches.gboard.memesearch

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

object EmoticonKeyboardViewCreatedFingerprint : Fingerprint(
    name = "eL",
    returnType = "V",
    filters = listOf(
        string("onKeyboardViewCreated() : Unexpected keyboard type %s.")
    )
)

object EmoticonKeyboardActivateFingerprint : Fingerprint(
    name = "e",
    returnType = "V",
    filters = listOf(
        string("EmoticonKeyboardM2.onActivate")
    )
)
