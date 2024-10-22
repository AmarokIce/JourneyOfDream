package club.someoneice.jod.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import org.jetbrains.annotations.Nullable

object ResourceUtil {
    val FONT = BitmapFont(Gdx.files.internal("font/font_vw/vonwaon.fnt"))
    val BATCH = SpriteBatch()

    fun getGameFont(): BitmapFont {
        return FONT
    }

    fun getGameBatch(): SpriteBatch {
        return BATCH
    }

    fun createTexturesArray(path: String, length: Int): Array<Texture> {
        val list: ArrayList<Texture> = ArrayList(length)
        for (i in 1 .. length) {
            list.add(Gdx.files.internal("${path}${i}.png").toTexture())
        }
        return list.toTypedArray()
    }

    @Nullable
    fun setDispose(disposableObject: Disposable?): Any? {
        disposableObject?.dispose()
        return null
    }

    fun FileHandle.toTexture(): Texture = Texture(this)
}
