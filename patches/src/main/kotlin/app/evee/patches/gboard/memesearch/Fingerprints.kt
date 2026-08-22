package app.evee.patches.gboard.memesearch

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

object EmoticonCorpusFingerprint : Fingerprint(
    name = "<clinit>",
    returnType = "V",
    filters = listOf(
        literal(0x7f140478),
        literal(0x7f080536),
        opcode(Opcode.CONST_CLASS)
    )
)
