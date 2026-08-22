package app.evee.patches.gboard.safesearch

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

object TenorContentFilterFingerprint : Fingerprint(
    name = "<clinit>",
    returnType = "V",
    filters = listOf(
        string("tenor_content_filter_level"),
        string("medium")
    )
)

object TenorRequestFilterFingerprint : Fingerprint(
    name = "iH",
    returnType = "Lvwd;",
    filters = listOf(
        string("contentfilter"),
        string("media_filter")
    )
)
