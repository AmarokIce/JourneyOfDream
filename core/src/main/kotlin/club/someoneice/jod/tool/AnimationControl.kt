package club.someoneice.jod.tool

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.utils.Disposable

class AnimationController(frameDuration: Float, vararg val textures: Texture): Disposable {
    private var deltas = 0.0f
    private val animation = Animation(frameDuration, *textures)

    fun getTexture(): Texture = this.animation.getKeyFrame(this.deltas, true)
    fun getDeltas(): Float = this.deltas

    fun addDelta(delta: Float) {
        this.deltas += delta
    }

    fun resetDeltas() {
        this.deltas = 0.0f
    }

    override fun dispose() {
        this.textures.forEach(Disposable::dispose)
    }
}
