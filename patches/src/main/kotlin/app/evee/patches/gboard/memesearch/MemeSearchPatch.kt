package app.evee.patches.gboard.memesearch

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD

@Suppress("unused")
val memeSearchPatch = bytecodePatch(
    name = "Replace emoticons with meme search",
    description = "Replaces the emoticon keyboard tab with a meme and static image search panel.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    execute {
        EmoticonNavbarFingerprint.method.replaceInstructions(
            0,
            """
                new-instance v0, Ljjc;
                const/4 v1, 0x1
                invoke-direct {v0, v1}, Ljjc;-><init>(I)V
                invoke-virtual {v0, p1, p2, p3, p4}, Ljjc;->a(Landroid/content/Context;Lpqu;Landroid/view/inputmethod/EditorInfo;Z)Lvow;
                move-result-object p0
                return-object p0
            """
        )
    }
}
