package club.someoneice.jod.core.screen

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.core.screen.prologue.PrologueScreen
import club.someoneice.jod.data.CtrlSets
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.data.SoundSet
import club.someoneice.jod.i18n.I10N
import club.someoneice.jod.i18n.I18N
import club.someoneice.jod.util.GdxColor
import com.badlogic.gdx.Input
import java.lang.Thread.sleep
import kotlin.math.max
import kotlin.math.min

class MainScreen: BaseScreen() {
    var start = false
    val color = GdxColor.WHITE.cpy().apply {
        this.a = 0.0f
    }

    override fun join() {
    }

    override fun render(delta: Float) {
        GameGlobal.initScreen()

        sleep(150)

        if (!this.start) {
            this.color.a = min(this.color.a + 0.1f, 1.0f)
        } else {
            this.color.a = max(this.color.a - 0.1f, 0.0f)
            if (this.color.a == 0.0f) {
                sleep(300)
                GameMain.INSTANCE.nextScreen(PrologueScreen())
                return
            }
        }

        this.batch.begin()
        this.font.getData().setScale(0.8f)
        this.font.color = this.color

        for (i in 0 .. 5) {
            this.font.draw(this.batch, I18N.format("mainmenu.tips${i}"),
                GameBasicInfo.WINDOWS_WIDTH / 8.0f,
                GameBasicInfo.WINDOWS_HEIGHT - (GameBasicInfo.WINDOWS_HEIGHT / 4.0f) - i * 40
            )
        }

        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean = false

    override fun keyUp(keycode: Int): Boolean {
        if (start) {
            return false
        }

        if (CtrlSets.INTERACT_KEYS.contains(keycode)) {
            SoundSet.SOUND_ENTER.play()
            start = true
            return true
        } else if (keycode == Input.Keys.L) {
            SoundSet.SOUND_NEWS.play()
            I18N.loadLanguage(I10N.getNextLanuage())
        }
        return false
    }
}
