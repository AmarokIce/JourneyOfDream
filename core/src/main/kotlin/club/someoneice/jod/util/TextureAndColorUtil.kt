package club.someoneice.jod.util

import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.data.GameGlobal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import ktx.collections.GdxSet
import ktx.collections.getOrPut
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/* Common */
typealias GdxColor = com.badlogic.gdx.graphics.Color
typealias JColor = java.awt.Color

fun GdxColor.asJColor(): JColor {
    return JColor(this.r, this.g, this.b, this.a)
}

fun JColor.asGdxColor(): GdxColor {
    return GdxColor(255.0f / this.rgb, 255.0f / this.green, 255.0f / this.blue, 255.0f / this.alpha)
}

fun FileHandle.toTexture(): Texture = Texture(this)

object ResourceUtil {
    /**
     * Create a solid color textures, it usually created for a background.
     */
    fun createBackgroundTexture(color: JColor, disposeableArray: GdxSet<Disposable?>?): Texture {
        val image = BufferedImage(GameBasicInfo.WINDOWS_WIDTH, GameBasicInfo.WINDOWS_HEIGHT, BufferedImage.TYPE_INT_RGB)
        val g2d = image.createGraphics()
        g2d.color = color
        g2d.fillRect(0, 0, image.width, image.height)
        g2d.dispose()

        val output = ByteArrayOutputStream()
        ImageIO.write(image, "png", output)
        val bytes = output.toByteArray()
        output.close()

        val pixmap = Pixmap(bytes, 0, bytes.size)
        val texture = Texture(pixmap)

        disposeableArray?.add(pixmap)
        disposeableArray?.add(texture)

        return texture
    }

    /**
     * Create an array with a textures set with coiled name.
     */
    fun createTexturesArray(path: String, length: Int): Array<Texture> {
        val list: ArrayList<Texture> = ArrayList(length)
        for (i in 1 .. length) {
            list.add(Gdx.files.internal("${path}${i}.png").toTexture())
        }
        return list.toTypedArray()
    }

    /**
     * Create or get a background by [createBackgroundTexture], <br>
     * reducing duplicate creation can effectively improve performance. <br>
     * It will save to [GameGlobal.COLOR_BACKGROUND], and dispose by it.
     */
    fun createOrGetBackground(color: JColor): Texture = GameGlobal.COLOR_BACKGROUND.getOrPut(color) {
        createBackgroundTexture(color, null)
    }
}
