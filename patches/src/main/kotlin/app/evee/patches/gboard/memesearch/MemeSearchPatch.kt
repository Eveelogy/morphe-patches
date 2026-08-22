package app.evee.patches.gboard.memesearch

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
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
        val xmlResInstructionIndex = EmoticonExtensionFingerprint.instructionMatches[1].index

        EmoticonExtensionFingerprint.method.replaceInstruction(
            xmlResInstructionIndex,
            """
                invoke-static {}, $EXTENSION_CLASS_GBOARD->isMemeSearchEnabled()Z
                move-result v0
                if-eqz v0, :cond_emoticon
                const v0, 0x7f170062
                goto :goto_init
                :cond_emoticon
                const v0, 0x7f17005d
                :goto_init
            """
        )
    }
}
