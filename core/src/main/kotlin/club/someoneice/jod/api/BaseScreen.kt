package club.someoneice.jod.api

import club.someoneice.jod.data.GameGlobal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.github.czyzby.kiwi.util.gdx.asset.Disposables
import ktx.collections.GdxSet
import java.util.Objects

abstract class BaseScreen(val shouldLicense: Boolean) : ScreenAdapter(), InputProcessor, Disposable {
    protected val font: BitmapFont = GameGlobal.getGameFont() // The font for game.
    protected val batch: SpriteBatch = GameGlobal.getGameBatch() // The batch for sprite.

    protected val disposableSet: GdxSet<Disposable?> = GdxSet()

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
        disposableSet.filter(Objects::nonNull).forEach(Disposables::disposeOf)
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
