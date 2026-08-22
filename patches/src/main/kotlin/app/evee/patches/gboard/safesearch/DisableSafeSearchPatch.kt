package app.evee.patches.gboard.safesearch

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.evee.patches.gboard.shared.Constants.COMPATIBILITY_GBOARD

@Suppress("unused")
val disableSafeSearchPatch = bytecodePatch(
    name = "Disable GIF SafeSearch",
    description = "Disables content filtering on Tenor GIF search.",
    default = true
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    execute {
        val mediumInstructionIndex = TenorContentFilterFingerprint.instructionMatches[1].index
        TenorContentFilterFingerprint.method.replaceInstruction(
            mediumInstructionIndex,
            """
                const-string v1, "off"
            """
        )
    }
}
