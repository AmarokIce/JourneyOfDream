package club.someoneice.jod.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

enum class MusicSet(val soundName: String) {
    A_LITTLE_ORANGE_CAT("a_little_orange_cat"),
    THE_OCEAN("the_ocean"),
    THE_OTHER_SHORE("the_other_shore"),
    THE_OTHER_SHORE_ALL("the_other_shore_all"),
    WARD("ward");

    fun createNewMusic(): Music = Gdx.audio.newMusic(Gdx.files.internal("music/${this.soundName}.mp3"))

    private var music: Music? = null

    private fun createMusic() {
        if (this.music == null) {
            this.music = this.createNewMusic()
        }
    }

    fun play() {
        createMusic()
        this.music!!.play()
    }

    fun loop() {
        createMusic()
        this.music!!.isLooping = true
    }

    fun loop(isLooping: Boolean) {
        createMusic()
        this.music!!.isLooping = isLooping
    }

    fun stop() {
        createMusic()
        this.music!!.stop()
    }

    fun getMusic(): Music? {
        return this.music
    }

    fun dispose() {
        this.music?.dispose()
        this.music = null
    }
}
