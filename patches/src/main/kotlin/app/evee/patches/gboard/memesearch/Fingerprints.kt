package app.evee.patches.gboard.memesearch

import app.morphe.patcher.Fingerprint

object EmoticonNavbarFingerprint : Fingerprint(
    definingClass = "Lizq;",
    name = "a",
    returnType = "Lvow;",
    parameters = listOf(
        "Landroid/content/Context;",
        "Lpqu;",
        "Landroid/view/inputmethod/EditorInfo;",
        "Z"
    )
)
