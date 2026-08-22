package app.evee.patches.gboard.memesearch

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD
import app.evee.patches.gboard.shared.Constants.EXTENSION_CLASS_GBOARD

@Suppress("unused")
val memeSearchPatch = bytecodePatch(
    name = "Replace emoticons with meme search",
    description = "Replaces the emoticon keyboard tab with a meme and static image search panel.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    extendWith("extensions/extension.mpe")

    execute {
        EmoticonNavbarFingerprint.method.replaceInstructions(
            0,
            """
                invoke-static {p0, p1, p2, p3, p4}, $EXTENSION_CLASS_GBOARD->getEmoticonNavbarItem(Ljava/lang/Object;Landroid/content/Context;Ljava/lang/Object;Landroid/view/inputmethod/EditorInfo;Z)Ljava/lang/Object;
                move-result-object p0
                check-cast p0, Lvow;
                return-object p0
            """
        )
    }
}
