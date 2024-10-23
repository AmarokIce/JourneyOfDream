package club.someoneice.jod.common.screen.world

import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.api.bean.BaseScreen
import club.someoneice.jod.common.actor.Cat
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.tool.AnimationController
import club.someoneice.jod.tool.KeyInputHolder
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import club.someoneice.jod.util.ResourceUtil.toTexture
import club.someoneice.jod.util.ScreenUtil
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World

class Outside: BaseScreen() {
    val world = World(Vector2(0f, -40f), true)
    val camera = OrthographicCamera(1280f, 720f)

    val backgroundTexture = Gdx.files.internal("textures/story/outside/outside_background.png").toTexture()
    val groundTexture = Gdx.files.internal("textures/story/outside/ground.png").toTexture()

    val animationBoat  = AnimationController(20.0f, *createTexturesArray("textures/story/outside/boat/boat", 8))
    val animationDock  = AnimationController(20.0f, *createTexturesArray("textures/story/outside/dock/dock", 4))

    val cat = Cat(Vector2(500f, 170f))
    val inputHolder = KeyInputHolder()

    val music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/a_little_orange_cat.mp3"))

    val renderer = Box2DDebugRenderer()

    override fun join() {
        this.cat.setRenderScale(5.0f)
        this.cat.setRenderOffset(-60f, -20f)
        this.cat.camera.y += 50

        this.camera.zoom = 32f
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0f)
        this.camera.update()

        this.createBox()

        this.music.play()
        this.music.isLooping

        this.disposeableSet.add(cat)
        this.disposeableSet.add(animationBoat)
        this.disposeableSet.add(animationDock)

        this.disposeableSet.add(world)
        this.disposeableSet.add(backgroundTexture)
        this.disposeableSet.add(groundTexture)
        this.disposeableSet.add(music)
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

    override fun render(delta: Float) {
        ScreenUtil.initScreen()

        this.cat.handleInput(this.inputHolder)

        this.camera.position.set(cat.camera.x, cat.camera.y, 0f)
        this.camera.update()
        this.batch.setProjectionMatrix(this.camera.combined)

        this.animationBoat.addDelta(1.0f)
        this.animationDock.addDelta(1.0f)

        this.batch.begin()

        this.batch.draw(this.backgroundTexture, 0f, 0f)

        this.batch.draw(animationBoat.getTexture(), 1646f, 59f)
        this.batch.draw(animationDock.getTexture(), 1612f, 24f)

        this.cat.render(batch, 1.0f)

        this.batch.draw(this.groundTexture, 0f, 0f)

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
        return inputHolder.removeKey(keycode)
    }

    override fun resize(width: Int, height: Int) {
        this.camera.viewportWidth = 30f
        this.camera.viewportHeight = 30f * height / width
        this.camera.update()
    }

    override fun hide() {
        this.music.stop()
        super.hide()
    }
}
