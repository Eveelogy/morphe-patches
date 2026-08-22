package app.evee.patches.gboard.memesearch

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.string

object EmoticonExtensionFingerprint : Fingerprint(
    name = "fG",
    returnType = "V",
    filters = listOf(
        string("EmoticonExtension.onCreate"),
        literal(0x7f17005d)
    )
)
