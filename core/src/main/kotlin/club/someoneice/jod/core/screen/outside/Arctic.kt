package club.someoneice.jod.core.screen.outside

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.data.MusicSet
import club.someoneice.jod.util.AnimationController
import club.someoneice.jod.util.CharIterator
import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import club.someoneice.jod.util.ResourceUtil
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import club.someoneice.jod.util.toTexture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import java.lang.Thread.sleep
import kotlin.math.max

class Arctic: BaseScreen() {
    val backgroundTexture = Gdx.files.internal("textures/story/arctic/arctic.png").toTexture()

    val catSit = Gdx.files.internal("textures/cat/cat_sit.png").toTexture()
    val catWillSleep = Gdx.files.internal("textures/cat/cat_sleep.png").toTexture()

    val catSleep = AnimationController(5.0f, *createTexturesArray("textures/cat/sleep/cat_sleep_", 6))

    val camera = OrthographicCamera(1280f, 720f)

    override fun join() {
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0f)
        this.camera.update()

        MusicSet.THE_OTHER_SHORE_ALL.play()

        this.disposableSet.add(catSit)
        this.disposableSet.add(catWillSleep)
        this.disposableSet.add(catSleep)
    }

    val joinBackgroundColor = GdxColor.WHITE.cpy()
    var start = false

    fun joinScreen() {
        sleep(300)
        this.batch.color = this.joinBackgroundColor
        this.batch.draw(ResourceUtil.createOrGetBackground(JColor.WHITE), 0f, 0f)
        this.batch.color = GdxColor.WHITE

        this.joinBackgroundColor.a = max(this.joinBackgroundColor.a - 0.1f, 0f)
        this.start = this.joinBackgroundColor.a == 0.0f
    }

    var willSleep = 0
    var step = 0

    override fun render(pDelta: Float) {
        GameGlobal.initScreen()

        this.camera.update()
        this.batch.setProjectionMatrix(this.camera.combined)

        this.batch.begin()
        this.batch.draw(backgroundTexture, 0f, 0f)

        if (!start) {
            this.batch.draw(this.catSit, 1050f, 110f, this.catSit.width * 5f, this.catSit.height * 5f)
            this.joinScreen()

            this.batch.end()
            return
        }

        if (this.camera.position.x < 1050f) {
            sleep(10)
            this.batch.draw(this.catSit, 1050f, 110f, this.catSit.width * 5f, this.catSit.height * 5f)
            this.camera.position.x += 1.0f
            this.batch.end()
            return
        }

        if (willSleep < 60) {
            sleep(200)
            this.batch.draw(this.catWillSleep, 1050f, 110f, this.catSit.width * 7f, this.catSit.height * 7f)
            willSleep++
            this.batch.end()
            return
        }

        this.catSleep.addDelta(1.0f)
        this.batch.draw(this.catSleep.getTexture(), 1050f, 110f, this.catSit.width * 7f, this.catSit.height * 7f)
        sleep(300)

        step++
        if (step < 15) {
            this.batch.end()
            return
        }

        if (step < 30) {
            this.font.data.setScale(1.5f)
            this.font.draw(this.batch, "感谢游玩", 900f, 528f)
            this.font.draw(this.batch, "现在可以按下Esc退出", 772f, 436f)
            this.batch.end()
            return
        }

        if (step < 80) {
            this.font.data.setScale(1.0f)
            this.font.draw(this.batch, "策划,音效: 起司", 850f, 650f)
            this.font.draw(this.batch, "主美工: 残纸", 850f, 600f)
            this.font.draw(this.batch, "美工: 吃纸", 850f, 550f)
            this.font.draw(this.batch, "程序: 初雪冰", 850f, 500f)
        }

        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if(this.step >= 15) {
            if (keycode == Input.Keys.ESCAPE) {
                Gdx.app.exit()
            }
        }
        return false
    }
}
