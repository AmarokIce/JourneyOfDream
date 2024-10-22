package club.someoneice.jod.api

import com.badlogic.gdx.utils.Disposable

@FunctionalInterface
fun interface Disposer {
    /**
     * Distinguish from [Disposable], Disposer care "Do what" not "Will do".
     *
     * @see club.someoneice.jod.util.ScreenUtil.setBackgroundColorInside
     */
    fun dispose()
}
