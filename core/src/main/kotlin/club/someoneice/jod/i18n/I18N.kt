package club.someoneice.jod.i18n

import club.someoneice.json.JSON
import club.someoneice.json.Pair
import com.badlogic.gdx.Gdx
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps

object I18N {
    private val basicLanguage: MutableMap<String, String>
    private val handleLanguage: MutableMap<String, String>

    init {
        val builder = ImmutableMap.builder<String, String>()
        val file = Gdx.files.internal("lang/zh_cn.json")
        val map = JSON.json5.parse(file.readString()).asMapNodeOrEmpty()
        map.stream()
            .map { it -> Pair(it.getKey(), it.getValue().toString()) }
            .forEach { it -> builder.put(it.key, it.value) }

        basicLanguage = builder.build()
        handleLanguage = Maps.newLinkedHashMap<String, String>()
    }

    fun loadLanguage(lang: String) {
        handleLanguage.putAll(basicLanguage)
        val file = Gdx.files.internal("lang/${lang}.json")
        val map = JSON.json5.parse(file.readString()).asMapNodeOrEmpty()
        map.stream()
            .map { it -> Pair<String, String>(it.getKey(), it.getValue().toString()) }
            .forEach { it -> handleLanguage.put(it.getKey(), it.getValue()) }
    }

    fun format(key: String, vararg str: String): String {
        return (handleLanguage[key] ?: basicLanguage[key] ?: key).format(*str)
    }
}
