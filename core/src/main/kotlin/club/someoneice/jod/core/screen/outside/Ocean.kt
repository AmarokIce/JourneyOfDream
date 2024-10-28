package club.someoneice.jod.core.screen.outside

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.core.actor.Cat
import club.someoneice.jod.core.actor.Corals
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.data.MusicSet
import club.someoneice.jod.util.AnimationController
import club.someoneice.jod.util.GdxColor
import club.someoneice.jod.util.JColor
import club.someoneice.jod.util.KeyInputHolder
import club.someoneice.jod.util.ResourceUtil
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import club.someoneice.jod.util.toTexture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import java.lang.Thread.sleep
import kotlin.math.max
import kotlin.math.min

class Ocean: BaseScreen() {
    val world = World(Vector2(0f, -40f), true)
    val camera = OrthographicCamera(1280f, 720f)

    val cat = Cat(Vector2(500f, 150f))
    val inputHolder = KeyInputHolder()

    val backgroundTexture = Gdx.files.internal("textures/story/ocean/ocean_background.png").toTexture()
    val groundDownTexture = Gdx.files.internal("textures/story/ocean/background_down.png").toTexture()
    val groundUpTexture = Gdx.files.internal("textures/story/ocean/background_up.png").toTexture()
    val animationGroundWare  = AnimationController(20.0f, *createTexturesArray("textures/story/ocean/ware/ground_ware_", 14))

    val catSit = Gdx.files.internal("textures/cat/cat_sit.png").toTexture()

    val corals = Corals()

    val renderer = Box2DDebugRenderer()

    override fun join() {
        this.cat.setRenderScale(5.0f)
        this.cat.setRenderOffset(-60f, -20f)
        this.cat.camera.y += 50

        // this.camera.zoom = 32f
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0f)
        this.camera.update()

        this.createBox()

        this.disposableSet.add(cat)
        this.disposableSet.add(catSit)

        this.disposableSet.add(world)
        this.disposableSet.add(backgroundTexture)
        this.disposableSet.add(animationGroundWare)
        this.disposableSet.add(groundDownTexture)
        this.disposableSet.add(groundUpTexture)
        this.disposableSet.add(corals)
    }

    fun createBox() {
        /* Create ground (Static) */

        PolygonShape().apply {
            this.setAsBox(1800f, 20f)
            world.createBody(
                BodyDef().apply {
                    this.type = BodyType.StaticBody
                    this.position.set(1150f, 100f)
                }
            ).createFixture(this, 0f)
            this.dispose()
        }

        PolygonShape().apply {
            this.setAsBox(300f, 220f)
            world.createBody(
                BodyDef().apply {
                    this.type = BodyType.StaticBody
                    this.position.set(0f, 200f)
                }
            ).createFixture(this, 0f)
            this.dispose()
        }

        /* Cat body */
        val shape = PolygonShape().apply {
            this.setAsBox(20f, 20f)
        }

        val catBody = world.createBody(BodyDef().apply {
            this.type = BodyType.DynamicBody
            this.position.set(cat.getPos().x, cat.getPos().y)
        })

        catBody.createFixture(shape, 9.8f).apply {
            this.friction = 2.0f
            this.restitution = 0.0f
        }

        this.cat.entityBody = catBody

        shape.dispose()
    }

    val joinBackgroundColor = GdxColor.WHITE.cpy()
    var start = false
    var canMove = false

    fun joinScreen() {
        sleep(100)
        this.cat.renderWith(this.batch, 1.0f, catSit)
        this.batch.color = this.joinBackgroundColor
        this.batch.draw(ResourceUtil.createOrGetBackground(JColor.WHITE), 0f, 0f)
        this.batch.color = GdxColor.WHITE
        this.joinBackgroundColor.a = max(this.joinBackgroundColor.a - 0.1f, 0f)
        this.canMove = this.joinBackgroundColor.a == 0.0f
        this.start = this.canMove

        if (start) {
            MusicSet.THE_OCEAN.play()
            MusicSet.THE_OCEAN.loop()
        }
    }

    fun toArctic() {
        this.cat.renderWith(this.batch, 1.0f, catSit)

        this.batch.color = this.joinBackgroundColor
        this.batch.draw(ResourceUtil.createOrGetBackground(JColor.WHITE), this.camera.position.x / 2f, -200f)
        this.batch.color = GdxColor.WHITE

        this.joinBackgroundColor.a = min(this.joinBackgroundColor.a + 0.1f, 1.0f)
        val music = MusicSet.THE_OCEAN.getMusic()!!
        music.volume = max(music.volume - 0.05f, 0.0f)


        if (music.volume == 0.0f) {
            GameMain.INSTANCE.nextScreen(Arctic())
        }
    }

    override fun render(delta: Float) {
        GameGlobal.initScreen()

        if (cat.getPos().y < 100) {
            this.cat.setPos(cat.getPos().x, 200f)
        }

        if (canMove) {
            this.cat.handleInput(this.inputHolder)
        }

        this.camera.position.set(cat.camera.x, cat.camera.y, 0f)
        this.camera.update()
        this.batch.setProjectionMatrix(this.camera.combined)
        this.animationGroundWare.addDelta(1.0f)

        this.batch.begin()

        this.batch.draw(this.backgroundTexture, 0f, 0f)
        this.batch.draw(this.animationGroundWare.getTexture(), 0f, 0f)
        this.batch.draw(this.groundDownTexture, 0f, 0f)
        this.corals.render(this.batch)

        if (!start) {
            joinScreen()
            this.batch.end()
            return
        }

        if (this.cat.getPos().x >= 1300) {
            sleep(200)
            toArctic()
            this.batch.end()
            return
        }

        this.cat.render(batch, 1.0f)

        this.batch.draw(this.groundUpTexture, 0f, 0f)

        this.batch.end()

        world.step(1 / 30f, 6, 2)
        if (GameMain.DEBUG_MODE) {
            this.renderer.render(this.world, this.camera.combined)
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        return inputHolder.putKey(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        if (GameMain.DEBUG_MODE && keycode == Input.Keys.L) {
            GameMain.info(this.cat.getPos().x.toString())
        }

        return inputHolder.removeKey(keycode)
    }

    override fun hide() {
        MusicSet.THE_OCEAN.stop()
        MusicSet.THE_OCEAN.dispose()
        super.hide()
    }
}
