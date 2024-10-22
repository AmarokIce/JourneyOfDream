package club.someoneice.jod.util

import com.badlogic.gdx.Input

object CtrlSets {
    val MOVING_KEYS = setOf(
        Input.Keys.A, Input.Keys.D, Input.Keys.LEFT, Input.Keys.RIGHT
    )

    val LEFT_MOVING_KEYS = setOf(
        Input.Keys.A, Input.Keys.LEFT
    )

    val RIGHT_MOVING_KEYS = setOf(
        Input.Keys.D, Input.Keys.RIGHT
    )

    val JUMP_KEYS = setOf(
        Input.Keys.W, Input.Keys.UP, Input.Keys.SPACE
    )

    val RUNNING_KEYS = setOf(
        Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT
    )

    val INTERACT_KEYS = setOf(
        Input.Keys.Z, Input.Keys.ENTER
    )
}
