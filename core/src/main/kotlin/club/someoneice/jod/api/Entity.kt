package club.someoneice.jod.api

import club.someoneice.jod.util.KeyInputHolder
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.utils.Disposable
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets

abstract class Entity(pos: Vector2 = Vector2.Zero): Disposable {

    protected val disposeableSet = GdxSets.newSet<Disposable>()
    protected val offsetRender = Vector2.Zero
    protected val scale = Vector2(1f, 1f)
    protected val position = Vector2(pos.x, pos.y)

    abstract fun update(delta: Float)
    abstract fun render(batch: Batch, delta: Float = 1.0f)
    abstract fun handleInput(inputHolder: KeyInputHolder)

    fun getPos(): Vector2 = this.position
    fun setPos(pos: Vector2) = this.position.set(pos.x, pos.y)
    fun setPos(x: Float, y: Float) = this.position.set(x, y)

    fun getRenderOffsetPos(): Vector2 = this.offsetRender
    fun setRenderOffsetPos(pos: Vector2) = this.offsetRender.set(pos.x, pos.y)
    fun setRenderOffset(x: Float, y: Float) = this.offsetRender.set(x, y)

    fun getRenderScale(): Vector2 = this.scale
    fun setRenderScale(scale: Vector2) = this.scale.set(scale)
    fun setRenderScale(x: Float, y: Float) = this.scale.set(x, y)
    fun setRenderScale(scale: Float) = this.scale.set(scale, scale)

    abstract fun getRenderPointX(): Float
    abstract fun getRenderPointY(): Float

    override fun dispose() {
        this.disposeableSet.forEach(Disposable::dispose)
    }

    companion object {
        fun createDefatleShape(): Shape = PolygonShape().apply {
            this.setAsBox(20f, 20f)
        }
    }
}
