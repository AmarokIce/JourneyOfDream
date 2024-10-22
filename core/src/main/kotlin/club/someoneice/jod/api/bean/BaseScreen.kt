package club.someoneice.jod.api.bean

import club.someoneice.jod.util.ResourceUtil
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import ktx.collections.GdxSet
import java.util.Objects

abstract class BaseScreen(val shouldLicense: Boolean) : ScreenAdapter(), InputProcessor, Disposable {
    protected val font: BitmapFont = ResourceUtil.getGameFont() // The font for game.
    protected val batch: SpriteBatch = ResourceUtil.getGameBatch() // The batch for sprite.

    protected val disposeableSet: GdxSet<Disposable?> = GdxSet()

    constructor() : this(true)

    override fun show() {
        Gdx.input.inputProcessor = if (this.shouldLicense) this else null
        join()
    }

    abstract fun join()

    override fun hide() {
        this.dispose()
    }

    override fun dispose() {
        disposeableSet.filter(Objects::nonNull).forEach(ResourceUtil::setDispose)
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}
