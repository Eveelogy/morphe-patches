package app.evee.patches.gboard.memesearch

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
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
        val constClassIndex = EmoticonCorpusFingerprint.instructionMatches[2].index

        EmoticonCorpusFingerprint.method.replaceInstruction(
            constClassIndex,
            """
                const-class v1, Lcom/google/android/apps/inputmethod/libs/expression/extension/IGifKeyboardExtension;
            """
        )
    }
}
