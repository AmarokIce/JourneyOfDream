package club.someoneice.jod.core.screen.outside

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.core.actor.Cat
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

class Home: BaseScreen() {
    val world = World(Vector2(0f, -40f), true)
    val camera = OrthographicCamera(1280f, 720f)

    val backgroundTexture = Gdx.files.internal("textures/story/outside/outside_background.png").toTexture()
    val groundTexture = Gdx.files.internal("textures/story/outside/ground.png").toTexture()
    val houseAddon = Gdx.files.internal("textures/story/outside/house_side.png").toTexture()

    val animationBoat  = AnimationController(20.0f, *createTexturesArray("textures/story/outside/boat/boat", 8))
    val animationDock  = AnimationController(20.0f, *createTexturesArray("textures/story/outside/dock/dock", 4))

    val cat = Cat(Vector2(500f, 170f))
    val inputHolder = KeyInputHolder()

    val renderer = Box2DDebugRenderer()

    override fun join() {
        this.cat.setRenderScale(5.0f)
        this.cat.setRenderOffset(-60f, -20f)
        this.cat.camera.y += 50

        this.camera.zoom = 32f
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0f)
        this.camera.update()

        this.createBox()

        MusicSet.A_LITTLE_ORANGE_CAT.play()
        MusicSet.A_LITTLE_ORANGE_CAT.loop()

        this.disposableSet.add(cat)
        this.disposableSet.add(animationBoat)
        this.disposableSet.add(animationDock)

        this.disposableSet.add(world)
        this.disposableSet.add(backgroundTexture)
        this.disposableSet.add(groundTexture)
    }

    fun createBox() {
        /* Create ground (Static) */

        PolygonShape().apply {
            this.setAsBox(1500f, 20f)
            world.createBody(
                BodyDef().apply {
                    this.type = BodyType.StaticBody
                    this.position.set(0f, 130f)
                }
            ).createFixture(this, 0f)
            this.dispose()
        }

        PolygonShape().apply {
            this.setAsBox(30f, 20f)
            world.createBody(
                BodyDef().apply {
                    this.type = BodyType.StaticBody
                    this.position.set(1530f, 110f)
                }
            ).createFixture(this, 0f)
            this.dispose()
        }

        PolygonShape().apply {
            this.setAsBox(120f, 20f)
            world.createBody(
                BodyDef().apply {
                    this.type = BodyType.StaticBody
                    this.position.set(1680f, 90f)
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
        this.batch.color = this.joinBackgroundColor
        this.batch.draw(ResourceUtil.createOrGetBackground(JColor.WHITE), 0f, 0f)
        this.batch.color = GdxColor.WHITE
        this.joinBackgroundColor.a = max(this.joinBackgroundColor.a - 0.1f, 0f)
        this.canMove = this.joinBackgroundColor.a == 0.0f
        this.start = this.canMove
    }

    fun toOcean() {
        this.cat.render(this.batch, 1.0f)

        this.batch.color = this.joinBackgroundColor
        this.batch.draw(ResourceUtil.createOrGetBackground(JColor.WHITE), this.camera.position.x / 2f, -60f)
        this.batch.color = GdxColor.WHITE

        this.joinBackgroundColor.a = min(this.joinBackgroundColor.a + 0.1f, 1.0f)
        val music = MusicSet.A_LITTLE_ORANGE_CAT.getMusic()!!
        music.volume = max(music.volume - 0.05f, 0.0f)

        this.batch.end()

        if (music.volume == 0.0f) {
            GameMain.INSTANCE.nextScreen(Ocean())
        }
    }

    override fun render(delta: Float) {
        GameGlobal.initScreen()
        world.step(1 / 30f, 6, 2)
        if (GameMain.DEBUG_MODE) {
            this.renderer.render(this.world, this.camera.combined)
        }

        if (canMove) {
            this.cat.handleInput(this.inputHolder)
        }

        this.camera.position.set(cat.camera.x, cat.camera.y, 0f)
        this.camera.update()
        this.batch.setProjectionMatrix(this.camera.combined)

        this.animationBoat.addDelta(1.0f)
        this.animationDock.addDelta(1.0f)

        this.batch.begin()

        this.batch.draw(this.backgroundTexture, 0f, 0f)

        this.batch.draw(animationBoat.getTexture(), 1646f, 59f)
        this.batch.draw(animationDock.getTexture(), 1612f, 24f)

        if (this.cat.getPos().x >= 1700) {
            sleep(200)
            toOcean()
            return
        }

        this.cat.render(batch, 1.0f)

        this.batch.draw(this.groundTexture, 0f, 0f)
        this.batch.draw(this.houseAddon, 0f, 0f)

        if (!start) {
            this.joinScreen()
        }

        this.batch.end()
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

    override fun resize(width: Int, height: Int) {
        this.camera.viewportWidth = 30f
        this.camera.viewportHeight = 30f * height / width
        this.camera.update()
    }

    override fun hide() {
        MusicSet.A_LITTLE_ORANGE_CAT.stop()
        super.hide()
    }
}
