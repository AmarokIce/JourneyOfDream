package club.someoneice.jod.util

import club.someoneice.jod.api.GameBasicInfo
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import ktx.collections.GdxSet
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

typealias GdxColor = com.badlogic.gdx.graphics.Color
typealias JColor = java.awt.Color

fun GdxColor.asJColor(): JColor {
    return JColor(this.r, this.g, this.b, this.a)
}

fun JColor.asGdxColor(): GdxColor {
    return GdxColor(255.0f / this.rgb, 255.0f / this.green, 255.0f / this.blue, 255.0f / this.alpha)
}

object ScreenUtil {
    fun initScreen() {
        ScreenUtils.clear(GdxColor.BLACK)
    }

    /**
     * Render the background. All resources in this function are temp files.
     */
    fun createBackgroundTexture(color: JColor, disposeableArray: GdxSet<Disposable?>): Texture {
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

        disposeableArray.add(pixmap)
        disposeableArray.add(texture)

        return texture
    }
}
