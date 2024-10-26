package club.someoneice.jod.util

import club.someoneice.jod.i18n.I18N
import com.badlogic.gdx.math.MathUtils
import java.util.regex.Pattern

class CharIterator {
    private val texts: Array<String>

    private var textLineIndex = 0
    private var textIndex = 0

    constructor(vararg texts: String) {
        this.texts = texts as Array<String>
    }

    constructor(texts: MutableList<String>) {
        this.texts = texts.toTypedArray<String>()
    }

    fun getTextStep(): String {
        val str = this.getText()
        if (textIndex > str.length) {
            return str
        }

        val charArray = str.toCharArray().copyOfRange(0, textIndex++)
        return String(charArray)
    }

    fun finishPrintLine(): Boolean {
        val str = this.getText()
        return textIndex == str.length
    }

    fun end(): Boolean {
        return this.textLineIndex == this.texts.size
    }

    fun nextLine() {
        this.textLineIndex += 1
        this.textIndex = 0
    }

    fun getText(): String = I18N.format(texts[textLineIndex])
    fun getTextAt(index: Int): String = I18N.format(this.texts[index])
    fun getLineAt(): Int = this.textLineIndex

    fun reset() {
        this.textLineIndex = 0
        this.textIndex = 0
    }

    companion object {
        fun calculateLength(str: String, scale: Float = 1.0f): Int {
            val leg = MathUtils.floor(5 * scale)
            val letterLeg = MathUtils.floor(16 * scale)

            var counter = 0
            Pattern.compile("[.,!?\\-+(){}\\[\\]:;<>~`]")
                .matcher(str)
                .takeIf { it.find() }
                ?.run { counter++ }

            return counter * leg + (str.length - counter) * letterLeg
        }
    }
}
