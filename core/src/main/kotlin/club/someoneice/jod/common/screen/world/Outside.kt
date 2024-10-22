package club.someoneice.jod.common.screen.world

import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.api.bean.BaseScreen
import club.someoneice.jod.common.actor.CatEntity
import club.someoneice.jod.tool.KeyInputHolder
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

    val groundTexture = Gdx.files.internal("textures/outside/home.png").toTexture()

    val cat = CatEntity(Vector2(GameBasicInfo.WINDOWS_WIDTH / 2f, GameBasicInfo.WINDOWS_HEIGHT / 2f))
    val inputHolder = KeyInputHolder()

    val renderer = Box2DDebugRenderer()

    override fun join() {
        this.cat.setRenderScale(5.0f)
        this.cat.setRenderOffset(-60f, -20f)
        // this.groundSprite.setPosition(0.0f, 0.0f)
        // this.groundSprite.setSize(1280F, 720F)

        this.camera.zoom = 32f
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0f)
        this.camera.update()

        createBox()

        this.disposeableSet.add(cat)
        this.disposeableSet.add(world)
        this.disposeableSet.add(groundTexture)
    }

    fun createBox() {
        /* Create ground (Static) */

        PolygonShape().apply {
            this.setAsBox(2560f, 20f)
            world.createBody(
                BodyDef().apply {
                    this.type = BodyType.StaticBody
                    this.position.set(0f, 144f)  // 720f / 5f = 144f
                }
            ).createFixture(this, 9.8f)
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

        this.batch.begin()

        this.batch.draw(this.groundTexture, 0f, 0f)
        this.cat.render(batch, 1.0f)

        this.batch.end()

        world.step(1 / 30f, 6, 2)

        // TODO: Debug only
        this.renderer.render(this.world, this.camera.combined)
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
}
