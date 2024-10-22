package club.someoneice.jod.tool

import java.util.regex.Pattern
import kotlin.math.floor

object StringCharIterator {
    fun calculateLength(str: String, scale: Float = 1.0f): Int {
        val leg = floor(6 * scale).toInt()
        val letterLeg = floor(16 * scale).toInt()

        var counter = 0
        Pattern.compile("[.,!?\\-+(){}\\[\\]:;<>~`]")
            .matcher(str)
            .takeIf { it.find() }
            ?.run { counter++ }

        return counter * leg + (str.length - counter) * letterLeg
    }
}
