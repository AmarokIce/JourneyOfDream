package club.someoneice.jod.data

import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps

object GameGlobal {
    val FONT = BitmapFont(Gdx.files.internal("font/font_vw/vonwaon.fnt"))
    val BATCH = SpriteBatch()

    val COLOR_BACKGROUND = GdxMaps.newObjectMap<JColor, Texture>()

    fun getGameFont(): BitmapFont {
        return FONT
    }

    fun getGameBatch(): SpriteBatch {
        return BATCH
    }

    fun dispose() {
        this.FONT.dispose()
        this.BATCH.dispose()

        SoundSet.dispose()
        MusicSet.entries.forEach(MusicSet::dispose)

        COLOR_BACKGROUND.values().forEach(Disposable::dispose)
    }

    /* Util Method */

    fun initScreen() {
        ScreenUtils.clear(GdxColor.BLACK)
    }
}
