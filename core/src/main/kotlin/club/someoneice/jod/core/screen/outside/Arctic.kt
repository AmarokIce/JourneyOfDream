package club.someoneice.jod.core.screen.outside

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.data.MusicSet
import club.someoneice.jod.util.AnimationController
import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import club.someoneice.jod.util.ResourceUtil
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import club.someoneice.jod.util.toTexture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
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

        this.font.data.setScale(1.0f)
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

    var showCreators = true
    val cheeseColor = GdxColor.WHITE.cpy().apply { this.a = 0f }
    val nColor = GdxColor.WHITE.cpy().apply { this.a = 0f }
    val paperColor = GdxColor.WHITE.cpy().apply { this.a = 0f }
    val iceColor = GdxColor.WHITE.cpy().apply { this.a = 0f }
    var colorExits = false

    var step = 0
    val poemColor = GdxColor.WHITE.cpy().apply { this.a = 0f }
    val endColor = GdxColor.WHITE.cpy().apply { this.a = 0f }


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

        if (step > 40) {
            this.font.color = GdxColor.WHITE
            this.font.draw(this.batch, "The End.", 900f, 550f)
        }

        if (step >= 80) {
            this.batch.end()
            return
        }

        if (showCreators) {
            if (!colorExits) {
                this.addAlpha(this.cheeseColor, 0.1f)
                if (this.cheeseColor.a == 1.0f) {
                    this.addAlpha(nColor, 0.1f)
                }

                if (this.nColor.a == 1.0f) {
                    this.addAlpha(paperColor, 0.1f)
                }

                if (this.paperColor.a == 1.0f) {
                    this.addAlpha(iceColor, 0.1f)
                }

                if (this.iceColor.a == 1.0f) {
                    this.colorExits = true
                }
            } else if (++step >= 20) {
                this.addAlpha(this.cheeseColor, -0.1f)
                this.addAlpha(this.nColor, -0.1f)
                this.addAlpha(this.paperColor, -0.1f)
                this.addAlpha(this.iceColor, -0.1f)

                if (this.cheeseColor.a == 0.0f) {
                    this.showCreators = false
                    this.step = 0
                }
            }

            this.font.color = cheeseColor
            this.font.draw(this.batch, "音乐,音效,策划: 起司", 850f, 650f)

            this.font.color = nColor
            this.font.draw(this.batch, "主美工: 残纸", 850f, 600f)

            this.font.color = paperColor
            this.font.draw(this.batch, "美工: 吃纸", 850f, 550f)

            this.font.color = iceColor
            this.font.draw(this.batch, "程序: 初雪冰", 850f, 500f)

            this.batch.end()
            return
        }

        if (this.step < 18) {
            this.addAlpha(this.poemColor, 0.1f)
        }

        this.font.color = poemColor
        this.font.draw(this.batch, "心之所向, 烨如明光.", 900f, 600f)

        this.step++
        if (step >= 28 && step < 40) {
            this.addAlpha(this.endColor, 0.2f)
        }
        this.font.color = this.endColor
        this.font.draw(this.batch, "The End.", 900f, 550f)
        this.font.data.setScale(0.5f)
        this.font.draw(this.batch, "按下 Esc 退出游戏.", 900f, 450f)
        this.font.data.setScale(1.0f)


        if (step > 40) {
            this.addAlpha(this.poemColor, -0.1f)
            this.addAlpha(this.endColor, -0.1f)
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

    fun addAlpha(color: GdxColor, value: Float) {
        color.a = MathUtils.clamp(color.a + value, 0.0f, 1.0f)
    }
}
