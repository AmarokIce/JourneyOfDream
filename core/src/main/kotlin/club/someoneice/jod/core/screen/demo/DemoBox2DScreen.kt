package club.someoneice.jod.core.screen.demo

import club.someoneice.jod.data.GameGlobal
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World

class DemoBox2DScreen: ApplicationAdapter() {
    var world: World? = null
    var camera: OrthographicCamera? = null
    var debugRenderer: Box2DDebugRenderer? = null

    override fun create() {
        super.create()

        world = World(Vector2(0f, -10f), true)
        debugRenderer = Box2DDebugRenderer()

        camera = OrthographicCamera()
        camera!!.setToOrtho(false, (Gdx.graphics.width / 10).toFloat(), (Gdx.graphics.height / 10).toFloat())


        val boxBodyDef = BodyDef()
        boxBodyDef.type = BodyDef.BodyType.DynamicBody

        boxBodyDef.position.set(40f, 125f)
        val boxBody = world!!.createBody(boxBodyDef)

        val boxPoly = PolygonShape()
        boxPoly.setAsBox(2f, 1f)
        boxBody.createFixture(boxPoly, 1f)
        boxPoly.dispose()


        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(60f, 25f)
        val bodyCircle = world!!.createBody(bodyDef)

        val circle = CircleShape()
        circle.radius = 2f
        val fixtureDef = FixtureDef()
        fixtureDef.shape = circle
        fixtureDef.density = 1f
        fixtureDef.friction = 0.4f

        bodyCircle.createFixture(fixtureDef)
        circle.dispose()

        val groundBodyDef = BodyDef()
        groundBodyDef.type = BodyDef.BodyType.StaticBody
        groundBodyDef.angle = Math.PI.toFloat() * 15 / 180
        groundBodyDef.position.set(0f, 0f)
        val groundBody = world!!.createBody(groundBodyDef)

        val groundBox = PolygonShape()
        groundBox.setAsBox(camera!!.viewportWidth + 5, 0.5f)
        groundBody.createFixture(groundBox, 0.0f)
        groundBox.dispose()
    }

    override fun render() {
        super.render()
        GameGlobal.initScreen()

        world!!.step(Gdx.graphics.deltaTime, 6, 2)
        camera!!.update()

        debugRenderer!!.render(world, camera!!.combined)
    }

    override fun dispose() {
        debugRenderer!!.dispose()
        world!!.dispose()
        super.dispose()
    }
}
