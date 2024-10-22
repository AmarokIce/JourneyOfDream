package club.someoneice.jod.tool

import club.someoneice.jod.core.I18N

class FontRenderBean {
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

    fun getText(): String {
        return I18N.format(texts[textLineIndex])
    }

    fun reset() {
        this.textLineIndex = 0
        this.textIndex = 0
    }
}


