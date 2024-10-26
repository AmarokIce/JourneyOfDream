package club.someoneice.jod.data

import com.badlogic.gdx.Gdx

object SoundSet {
    val SOUND_BACK = Gdx.audio.newSound(Gdx.files.internal("sound/back.ogg"))
    val SOUND_DISABLE = Gdx.audio.newSound(Gdx.files.internal("sound/disable.ogg"))
    val SOUND_ENTER = Gdx.audio.newSound(Gdx.files.internal("sound/enter.ogg"))
    val SOUND_FUNNY = Gdx.audio.newSound(Gdx.files.internal("sound/funny.ogg"))
    val SOUND_GREAT = Gdx.audio.newSound(Gdx.files.internal("sound/great.ogg"))
    val SOUND_NEWS = Gdx.audio.newSound(Gdx.files.internal("sound/news.ogg"))
    val SOUND_OPEN = Gdx.audio.newSound(Gdx.files.internal("sound/open.ogg"))

    internal fun dispose() {
        SOUND_BACK.dispose()
        SOUND_DISABLE.dispose()
        SOUND_ENTER.dispose()
        SOUND_FUNNY.dispose()
        SOUND_GREAT.dispose()
        SOUND_NEWS.dispose()
        SOUND_OPEN.dispose()
    }
}
