package app.evee.patches.gboard.memesearch

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD
import app.evee.patches.gboard.shared.Constants.EXTENSION_CLASS_MEME_VIEW

@Suppress("unused")
val memeSearchPatch = bytecodePatch(
    name = "Meme Search & Maker",
    description = "Replaces the emoticon keyboard with an Imgflip meme search and on-device meme creator.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    extendWith("extensions/extension.mpe")

    execute {
        // 1. Hook onKeyboardViewCreated to attach MemeKeyboardView
        EmoticonKeyboardViewCreatedFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p0, p1, p2}, $EXTENSION_CLASS_MEME_VIEW->onKeyboardViewCreated(Ljava/lang/Object;Landroid/view/View;Ljava/lang/Object;)V
            """
        )

        // 2. Hook onActivate to refresh context and memes
        EmoticonKeyboardActivateFingerprint.method.addInstructions(
            0,
            """
                invoke-static {p0, p1, p2}, $EXTENSION_CLASS_MEME_VIEW->onActivate(Ljava/lang/Object;Landroid/view/inputmethod/EditorInfo;Ljava/lang/Object;)V
            """
        )
    }
}
