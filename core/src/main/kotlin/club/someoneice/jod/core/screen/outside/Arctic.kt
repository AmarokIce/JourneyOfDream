package club.someoneice.jod.core.screen.outside

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.util.toTexture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera

class Arctic: BaseScreen() {
    val backgroundTexture = Gdx.files.internal("textures/story/arctic/arctic.png").toTexture()

    val catSit = Gdx.files.internal("textures/cat/cat_sit.png").toTexture()
    val catSleep = Gdx.files.internal("textures/cat/cat_sleep.png").toTexture()

    val camera = OrthographicCamera(1280f, 720f)

    override fun join() {
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0f)
        this.camera.update()
    }

    fun joinScreen() {

    }

    override fun render(delta: Float) {
        GameGlobal.initScreen()

        this.camera.update()
        this.batch.setProjectionMatrix(this.camera.combined)

        this.batch.begin()
        this.batch.draw(backgroundTexture, 0f, 0f)
        this.batch.draw(this.catSit, 1150f, 110f, this.catSit.width * 5f, this.catSit.height * 5f)
        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }
}
