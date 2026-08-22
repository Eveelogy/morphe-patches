package app.evee.patches.gboard.safesearch

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD
import app.evee.patches.gboard.shared.Constants.EXTENSION_CLASS_GBOARD

@Suppress("unused")
val disableSafeSearchPatch = bytecodePatch(
    name = "Disable GIF SafeSearch",
    description = "Disables content filtering on Tenor GIF search in real-time.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    extendWith("extensions/extension.mpe")

    execute {
        val mediumInstructionIndex = TenorContentFilterFingerprint.instructionMatches[1].index
        TenorContentFilterFingerprint.method.replaceInstruction(
            mediumInstructionIndex,
            """
                const-string v1, "off"
            """
        )

        val contentFilterInstructionIndex = TenorRequestFilterFingerprint.instructionMatches[0].index
        TenorRequestFilterFingerprint.method.replaceInstruction(
            contentFilterInstructionIndex - 1,
            """
                invoke-static {}, $EXTENSION_CLASS_GBOARD->getContentFilterLevel()Ljava/lang/String;
                move-result-object v1
            """
        )
    }
}
