package club.someoneice.jod.core.actor

import club.someoneice.jod.util.GdxColor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets

class Corals: Disposable {
    val texture = Texture(Gdx.files.internal("textures/story/ocean/corals.png"))
    val texturePos = arrayOf(
        Pair(0, 0),
        Pair(113, 0),
        Pair(0, 113),
        Pair(113, 113),
    )

    val colors = arrayOf(
        GdxColor.BLUE, GdxColor.RED, GdxColor.GREEN, GdxColor.YELLOW, GdxColor.GOLD, GdxColor.PURPLE, GdxColor.ORANGE, GdxColor.PINK, GdxColor.OLIVE, GdxColor.FOREST
    )

    val coralsSet = GdxSets.newSet<CoralData>()

    init {
        0.rangeTo(MathUtils.random(6, 12)).forEach {
            val indexAt = MathUtils.random(0, 3)
            val posAt = this.texturePos[indexAt]
            val textureRegion = TextureRegion(this.texture, posAt.first, posAt.second, posAt.first + 112, posAt.second + 112)

            val posX = 352f + MathUtils.random(0, 825) * 1.2f
            val posY = 50f + MathUtils.random(0, 56) - (if (indexAt > 3) -25f else 12f) * 0.85f

            this.coralsSet.add(CoralData(textureRegion, colors.random().cpy(), Vector2(posX, posY)))
        }
    }

    fun render(batch: Batch) {
        this.coralsSet.forEach {
            it.render(batch)
        }
    }

    override fun dispose() {
        this.texture.dispose()
    }

    data class CoralData(
        val tex: TextureRegion,
        val color: GdxColor,
        val pos: Vector2
    ) {
        fun render(batch: Batch) {
            batch.color = this.color
            batch.draw(this.tex, this.pos.x, this.pos.y)
            batch.color = GdxColor.WHITE
        }
    }
}
