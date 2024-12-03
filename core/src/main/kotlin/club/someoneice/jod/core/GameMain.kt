package club.someoneice.jod.core

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.core.GameMain.ArchitectureOS
import club.someoneice.jod.core.screen.MainScreen
import club.someoneice.jod.core.screen.outside.Home
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.i18n.I18N
import club.someoneice.jod.util.GdxColor
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx

class GameMain(val arch: ArchitectureOS) : Game() {
    @Suppress("unused")
    companion object {
        lateinit var INSTANCE: GameMain
        const val DEBUG_MODE = false

        fun info(str: String) {
            Gdx.app.log("journey of dream", str)
        }

        fun debug(str: String) {
            Gdx.app.debug("journey of dream", str)
        }

        fun error(str: String) {
            Gdx.app.error("journey of dream", str)
        }
    }

    init {
        INSTANCE = this
    }

    /**
     * @return Return true if architecture is LWJGL.
     */
    @Deprecated("Deprecated")
    fun isDesktopSide(): Boolean {
        return this.arch == ArchitectureOS.LWJGL
    }


    override fun create() {
        init()

        info("Show first screen.")
        this.setScreen(MainScreen())
    }

    fun init() {
        info("Init game language...")
        I18N.loadLanguage("zh_cn")

        info("End. Now start game...")
    }

    fun nextScreen(next: BaseScreen?) {
        GameGlobal.BATCH.color = GdxColor.WHITE.cpy()
        GameGlobal.FONT.color = GdxColor.WHITE.cpy()
        this.setScreen(next)
    }

    override fun dispose() {
        super.dispose()
        GameGlobal.dispose()
    }

    enum class ArchitectureOS {
        LWJGL,
        WebGL
    }

    /* === Demo Screen === */
    /*
    private class DemoScreen : BaseScreen(false) {
        // val hello: String = ("Hello World! Love from " + (if (INSTANCE.canSaveFile()) "LWJGL" else "TeaVM") + "!")

        var plus = true
        var alpha = 0.0f

        override fun join() {
        }

        override fun render(delta: Float) {
            ScreenUtil.initScreen()
            sleep(100)
            ScreenUtil.setBackgroundColor(this.batch!!, GdxColor.WHITE.apply { this.a = Math.clamp(this@DemoScreen.alpha, 0.0f, 1.0f) }.asJColor())
            val ps = if (plus) 0.1f else -0.1f
            alpha += ps
            if (alpha <= 0f) {
                plus = true
            } else if (alpha >= 1f) {
                plus = false
            }
        }

        override fun keyDown(keycode: Int): Boolean {
            return false
        }

        override fun keyUp(keycode: Int): Boolean {
            return false
        }
    }
     */
}
