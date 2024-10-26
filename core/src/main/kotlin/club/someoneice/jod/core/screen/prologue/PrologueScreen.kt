package club.someoneice.jod.core.screen.prologue

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.data.CtrlSets
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.util.CharIterator
import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import club.someoneice.jod.util.ResourceUtil
import com.google.common.collect.Lists
import java.lang.Thread.sleep
import kotlin.math.max
import kotlin.math.min

class PrologueScreen: BaseScreen() {
    val texts: CharIterator = CharIterator(
        Lists.newArrayList<String>().apply {
            for (i in 0..16) this.add("prologue.$i")
        }
    )

    override fun join() {
        if (GameMain.DEBUG_MODE) {
            GameMain.debug("Game start show.")
        }
    }

    var shouldMakeAlphaLowdown = false
    var fontColor = GdxColor.LIGHT_GRAY.cpy()
    var screenColor = GdxColor.WHITE.cpy().apply {
        this.a = 0.0f
    }

    override fun render(delta: Float) {
        GameGlobal.initScreen()
        sleep(100)

        if (this.texts.end()) {
            if (this.screenColor.a < 1.0f) {
                this.screenColor.a = min(this.screenColor.a + 0.05f, 1.0f)
            } else {
                GameMain.INSTANCE.nextScreen(PrologueHospital())
            }

            this.batch.begin()
            this.batch.color = this.screenColor
            this.batch.draw(ResourceUtil.createOrGetBackground(JColor.WHITE), 0f, 0f)
            this.batch.end()

            return
        }

        if (this.shouldMakeAlphaLowdown) {
            this.fontColor.a = max(fontColor.a - 0.1f, 0.0f)
        }

        if (this.fontColor.a == 0.0f) {
            this.shouldMakeAlphaLowdown = false
            this.fontColor.a = 1.0f
            this.texts.nextLine()
            return
        }

        this.batch.begin()

        var txt = this.texts.getTextStep()
        this.font.getData().setScale(0.8f)
        this.font.color = fontColor
        this.font.draw(this.batch, txt, GameBasicInfo.WINDOWS_WIDTH / 2.0f - CharIterator.calculateLength(txt, 0.8f), (GameBasicInfo.WINDOWS_HEIGHT / 2.0f) + 8)

        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean {
        if (CtrlSets.INTERACT_KEYS.contains(keycode)) {
            this.shouldMakeAlphaLowdown = true
            return true
        }
        return false
    }
}
