package club.someoneice.jod.api.bean

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import ktx.collections.GdxSet

abstract class BaseActor : Actor(), Disposable {
    protected val disposables: GdxSet<Disposable> = GdxSet()

    override fun act(delta: Float) {
        super.act(delta)
        update(delta)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        render(batch, parentAlpha)
    }

    abstract fun update(delta: Float)
    abstract fun render(batch: Batch, parentAlpha: Float)

    override fun clear() {
        super.clear()
        disposables.forEach(Disposable::dispose)
    }
}
