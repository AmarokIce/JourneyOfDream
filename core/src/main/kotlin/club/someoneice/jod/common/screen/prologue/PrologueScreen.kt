package club.someoneice.jod.common.screen.prologue

import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.api.bean.BaseScreen
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.tool.FontRenderBean
import club.someoneice.jod.tool.StringCharIterator
import club.someoneice.jod.util.CtrlSets
import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import club.someoneice.jod.util.ScreenUtil
import com.google.common.collect.Lists
import ktx.graphics.copy
import java.lang.Thread.sleep

class PrologueScreen: BaseScreen() {
    val texts: FontRenderBean = FontRenderBean(
        Lists.newArrayList<String>().apply {
            for (i in 0..16) this.add("prologue.$i")
        }
    )

    val backgroundTexture = ScreenUtil.createBackgroundTexture(JColor.WHITE, this.disposeableSet)

    override fun join() {
        if (GameMain.DEBUG_MODE) {
            GameMain.debug("Game start show.")
        }
    }

    var shouldMakeAplhaLowdown = false
    var alpha = 1.0f
    var screenAlpha = 0.0f

    override fun render(delta: Float) {
        ScreenUtil.initScreen()
        sleep(100)

        if (this.texts.end()) {
            if (this.screenAlpha < 1.0f) {
                this.screenAlpha += 0.05f
            } else {
                GameMain.INSTANCE.nextScreen(PrologueHospital())
            }

            this.batch.begin()
            this.batch.color = GdxColor.WHITE.copy().apply { this.a = this@PrologueScreen.screenAlpha }
            this.batch.draw(this.backgroundTexture, 0f, 0f)
            this.batch.color = GdxColor.WHITE
            this.batch.end()

            return
        }

        if (this.shouldMakeAplhaLowdown) {
            this.alpha -= 0.25f
        }

        if (this.alpha < 0.0f) {
            this.shouldMakeAplhaLowdown = false
            this.alpha = 1.0f

            this.texts.nextLine()

            return
        }

        this.batch.begin()

        var txt = texts.getTextStep()
        this.font.getData().setScale(0.8f)
        this.font.color = GdxColor.LIGHT_GRAY.copy().apply { this.a = this@PrologueScreen.alpha }
        this.font.draw(this.batch, txt, GameBasicInfo.WINDOWS_WIDTH / 2.0f - StringCharIterator.calculateLength(txt, 0.8f), (GameBasicInfo.WINDOWS_HEIGHT / 2.0f) + 8)

        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (CtrlSets.INTERACT_KEYS.contains(keycode)) {
            this.shouldMakeAplhaLowdown = true
            return true
        }

        return false
    }
}
