package club.someoneice.jod.core.screen.prologue

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.data.CtrlSets
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.data.MusicSet
import club.someoneice.jod.util.CharIterator
import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import club.someoneice.jod.util.ResourceUtil
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import com.badlogic.gdx.graphics.Texture
import java.lang.Thread.sleep
import kotlin.math.max

class PrologueHospital: BaseScreen() {
    val textures: Array<Texture> = createTexturesArray("textures/story/hospitalroom/h", 11)

    val backgroundWhite = ResourceUtil.createOrGetBackground(JColor.WHITE)

    /* Join Screen */
    val colorJoinScreen: GdxColor = GdxColor.WHITE.cpy()

    /* Anim Ctrl */
    val colorAnimScreen: GdxColor = GdxColor.WHITE.cpy()
    var animIndex: Int = 0

    /* Plot helper */
    val plotHelperHospitalRoom = CharIterator("prologue.hospital")

    override fun join() {
        this.textures.forEach(this.disposableSet::add)
    }

    override fun render(delta: Float) {
        GameGlobal.initScreen()

        sleep(200)

        this.batch.color = GdxColor.WHITE
        this.batch.begin()

        if (this.colorJoinScreen.a != 0.0f) {
            this.batch.draw(textures[0], 0f, 0f)
            this.batch.color = this.colorJoinScreen
            this.batch.draw(this.backgroundWhite, 0f, 0f)
            this.colorJoinScreen.a = max(this.colorJoinScreen.a - 0.1f, 0.0f)
            this.batch.end()
            return
        }

        this.batch.color = this.colorAnimScreen
        this.batch.draw(textures[animIndex], 0f, 0f)
        if (this.animIndex < 10) {
            this.animIndex++
            this.batch.end()
            return
        }

        if (!this.plotHelperHospitalRoom.end()) {
            var txt = this.plotHelperHospitalRoom.getTextStep()
            this.font.getData().setScale(0.8f)
            this.font.color = this.colorAnimScreen
            this.font.draw(this.batch, txt, 610f - CharIterator.calculateLength(txt, 0.8f), 320f + 8f)
            this.batch.end()
            return
        }

        MusicSet.WARD.play()
        MusicSet.WARD.loop()

        this.colorAnimScreen.a = max(this.colorAnimScreen.a - 0.1f, 0.0f)
        if (this.colorAnimScreen.a == 0.0f) {
            GameMain.INSTANCE.nextScreen(PrologueDream())
        }

        this.batch.end()
    }

    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean {
        if (this.animIndex == 10
            && CtrlSets.INTERACT_KEYS.contains(keycode)
            && !this.plotHelperHospitalRoom.end())
        {
            this.plotHelperHospitalRoom.nextLine()
            return true
        }
        return false
    }
}
