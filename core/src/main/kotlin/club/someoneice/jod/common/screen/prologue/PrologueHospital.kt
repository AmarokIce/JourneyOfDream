package club.someoneice.jod.common.screen.prologue

import club.someoneice.jod.api.bean.BaseScreen
import club.someoneice.jod.common.screen.world.Outside
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import club.someoneice.jod.util.ScreenUtil
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import ktx.graphics.copy
import java.lang.Thread.sleep

class PrologueHospital: BaseScreen(false) {
    val music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/ward.mp3"))

    val textures: Array<Texture> = createTexturesArray("textures/story/hospitalroom/h", 11)

    val backgroundTextureWhite = ScreenUtil.createBackgroundTexture(JColor.WHITE, this.disposeableSet)
    val backgroundTextureBlack = ScreenUtil.createBackgroundTexture(JColor.BLACK, this.disposeableSet)


    override fun join() {
        this.textures.forEach(this.disposeableSet::add)
        disposeableSet.add(music)

        music.play()
        music.isLooping = true
    }

    var screenAlpha = 1.0f
    var screenStart = true
    var screenIndex = 0

    override fun render(delta: Float) {
        ScreenUtil.initScreen()
        this.batch.color = GdxColor.WHITE
        sleep(150)

        this.batch.begin()
        // this.batch.color = GdxColor.WHITE
        if (screenStart && screenAlpha >= 0.0f) {
            this.batch.draw(textures[0], 0f, 0f)

            this.batch.color = GdxColor.WHITE.copy().apply {this.a = this@PrologueHospital.screenAlpha}
            screenAlpha -= 0.05f
            batch.draw(this.backgroundTextureWhite, 0f, 0f)
            batch.color = GdxColor.WHITE

            this.batch.end()
            return
        }

        screenStart = false

        if (screenIndex < 11) {
            sleep(150)
            this.batch.draw(textures[screenIndex++], 0f, 0f)
            this.batch.end()
            return
        }

        if (screenAlpha >= 1.0f) {
            this.batch.draw(this.backgroundTextureBlack, 0.0f, 0.0f)
            this.batch.end()

            sleep(200)

            this.music.stop()
            GameMain.INSTANCE.nextScreen(Outside())
            return
        }

        this.batch.draw(textures[10], 0f, 0f)

        screenAlpha += 0.05f
        this.batch.color = GdxColor.WHITE.copy().apply {this.a = this@PrologueHospital.screenAlpha}
        this.batch.draw(this.backgroundTextureBlack, 0.0f, 0.0f)
        this.batch.color = GdxColor.WHITE

        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
}
