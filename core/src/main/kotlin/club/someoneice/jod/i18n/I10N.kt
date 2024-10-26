package club.someoneice.jod.i18n

object I10N {
    val languages = arrayOf(
        "zh_cn", "en_us"
    )

    var indexOfLanguage : Int = 0

    fun getNextLanuage(): String {
        if (++indexOfLanguage >= languages.size) {
            indexOfLanguage = 0
        }

        return languages[indexOfLanguage]
    }
}
