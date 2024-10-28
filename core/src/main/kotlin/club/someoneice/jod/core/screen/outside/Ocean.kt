package club.someoneice.jod.core.screen.outside

import club.someoneice.jod.api.BaseScreen
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.core.actor.Cat
import club.someoneice.jod.core.actor.Corals
import club.someoneice.jod.data.GameGlobal
import club.someoneice.jod.data.MusicSet
import club.someoneice.jod.util.AnimationController
import club.someoneice.jod.util.KeyInputHolder
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

class Ocean: BaseScreen() {
    val world = World(Vector2(0f, -40f), true)
    val camera = OrthographicCamera(1280f, 720f)

    val cat = Cat(Vector2(500f, 150f))
    val inputHolder = KeyInputHolder()

    val backgroundTexture = Gdx.files.internal("textures/story/ocean/ocean_background.png").toTexture()
    val groundDownTexture = Gdx.files.internal("textures/story/ocean/background_down.png").toTexture()
    val groundUpTexture = Gdx.files.internal("textures/story/ocean/background_up.png").toTexture()
    val animationGroundWare  = AnimationController(20.0f, *createTexturesArray("textures/story/ocean/ware/ground_ware_", 14))

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

        MusicSet.THE_OCEAN.play()
        MusicSet.THE_OCEAN.loop()

        this.disposableSet.add(cat)

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
            this.setAsBox(820f, 20f)
            world.createBody(
                BodyDef().apply {
                    this.type = BodyType.StaticBody
                    this.position.set(1150f, 100f)
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
        GameGlobal.initScreen()
        world.step(1 / 30f, 6, 2)
        if (GameMain.DEBUG_MODE) {
            this.renderer.render(this.world, this.camera.combined)
        }

        this.cat.handleInput(this.inputHolder)

        this.camera.position.set(cat.camera.x, cat.camera.y, 0f)
        this.camera.update()
        this.batch.setProjectionMatrix(this.camera.combined)
        this.animationGroundWare.addDelta(1.0f)

        this.batch.begin()

        this.batch.draw(this.backgroundTexture, 0f, 0f)
        this.batch.draw(this.animationGroundWare.getTexture(), 0f, 0f)
        this.batch.draw(this.groundDownTexture, 0f, 0f)
        this.corals.render(this.batch)

        this.cat.render(batch, 1.0f)

        this.batch.draw(this.groundUpTexture, 0f, 0f)


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
}
