package club.someoneice.jod.common.screen.demo

import club.someoneice.jod.api.bean.BaseScreen
import club.someoneice.jod.common.screen.prologue.PrologueScreen
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.core.I18N
import club.someoneice.jod.util.CtrlSets
import club.someoneice.jod.util.ScreenUtil
import com.badlogic.gdx.graphics.Color

class DemoMenuScreen : BaseScreen() {
    override fun join() {
    }

    override fun render(delta: Float) {
        ScreenUtil.initScreen()

        this.batch.begin()
        this.font.getData().setScale(0.8f)
        this.font.color = Color.WHITE
        this.font.draw(this.batch, "这是一个测试.下面将会使用 *中文(ZH-CN)* 显示提示.", 50.0f, 400.0f)
        this.font.draw(this.batch, "This is a debug-run. Now it'll show a tip with Chinese(ZH-CN).", 50.0f, 350.0f)

        this.font.draw(this.batch, I18N.format("mainmenu.start"), 50.0f, 250.0f)

        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (CtrlSets.INTERACT_KEYS.contains(keycode)) {
            GameMain.Companion.INSTANCE.nextScreen(PrologueScreen())
            return true
        }
        return false
    }
}
