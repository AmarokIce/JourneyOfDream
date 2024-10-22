package club.someoneice.jod.common.actor

import club.someoneice.jod.api.bean.Entity
import club.someoneice.jod.tool.AnimationController
import club.someoneice.jod.tool.KeyInputHolder
import club.someoneice.jod.util.CtrlSets
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class CatEntity(pos: Vector2 = Vector2.Zero): Entity(pos = pos) {
    val staticCtrl = AnimationController(5.0f, *createTexturesArray("textures/actor/cat/static/CAT", 6))
    val walkingCtrl = AnimationController(5.0f, *createTexturesArray("textures/actor/cat/walk/CAT_walk", 7))
    val runningCtrl = AnimationController(5.0f, *createTexturesArray("textures/actor/cat/running/CAT_running", 10))
    val jumpingCtrl = AnimationController(5.0f, *createTexturesArray("textures/actor/cat/jump/CAT_jump", 6))

    var actionState = State.STATIC
    var directionRight = true
    var isJumping = false

    lateinit var entityBody: Body

    val camera = Vector2(pos)

    init {
        this.disposeableSet.add(staticCtrl)
        this.disposeableSet.add(walkingCtrl)
        this.disposeableSet.add(runningCtrl)
        this.disposeableSet.add(jumpingCtrl)
    }

    override fun update(delta: Float) {
        this.position.set(this.entityBody.position)

        if (!isJumping && jumpingCtrl.getDeltas() > 5 * 6) {
            this.jumpingCtrl.resetDeltas()
            this.actionState = State.STATIC
        }
    }

    override fun render(batch: Batch, delta: Float) {
        this.update(delta)
        val texture = updateStateAndGetTexture(delta)
        val region = TextureRegion(texture, 0, 0, texture.width, texture.height)
        region.flip(!this.directionRight, false)
        batch.draw(region, this.getRenderPointX(), this.getRenderPointY(),
            texture.width * this.scale.x, texture.height * this.scale.y)
    }

    override fun handleInput(inputHolder: KeyInputHolder) {
        this.setStateByKeyInput(inputHolder)
        this.setMovingByKeyInput(inputHolder)
    }

    private fun setStateByKeyInput(input: KeyInputHolder) {
        val keyMoving = input.findLastIn(CtrlSets.MOVING_KEYS)
        this.directionRight = CtrlSets.RIGHT_MOVING_KEYS.contains(keyMoving)
        if (this.isJumping || this.actionState == State.JUMPING) {
            this.actionState = State.JUMPING
            return
        }

        val flag = keyMoving != -1

        this.actionState = if (flag) {
            if (input.containsAny(CtrlSets.RUNNING_KEYS)) State.RUNNING else State.WALKING
        } else State.STATIC
    }

    private fun setMovingByKeyInput(input: KeyInputHolder) {
        if (this.actionState == State.JUMPING && this.jumpingCtrl.getDeltas() < 5f) {
            return
        }

        var speed = 3.0f
        val flag = input.containsAny(CtrlSets.RUNNING_KEYS)

        if (flag) {
            speed *= 1.5f
        }

        val keyInput = input.findLastIn(CtrlSets.MOVING_KEYS)

        val catBodyPos = this.entityBody.position

        if (CtrlSets.LEFT_MOVING_KEYS.contains(keyInput)) {
            if (catBodyPos.x <= 2080f) {
                this.camera.x = max(this.camera.x - speed, 480f)
            }

            if (catBodyPos.x > 0) {
                catBodyPos.x = max(catBodyPos.x - speed, 0f)
            }
        } else if (CtrlSets.RIGHT_MOVING_KEYS.contains(keyInput)) {
            if (catBodyPos.x >= 480f) {
                this.camera.x = min(this.camera.x + speed, 2080f)
            }

            if (catBodyPos.x < 2500f) {
                catBodyPos.x = min(catBodyPos.x + speed, 2500f)
            }
        }

        this.entityBody.setTransform(catBodyPos, this.entityBody.angle)

        if (input.containsAny(CtrlSets.JUMP_KEYS) && !this.isJumping) {
            this.actionState = State.JUMPING
        }

        this.isJumping = this.entityBody.linearVelocity.y != 0f

        if (this.jumpingCtrl.getDeltas() == 5f)
            this.entityBody.applyLinearImpulse(Vector2(0f, this.entityBody.mass * 265f), this.entityBody.position, true)

        this.entityBody.setLinearVelocity(0f, this.entityBody.linearVelocity.y)
    }

    fun updateStateAndGetTexture(delta: Float): Texture {
        fun resetState(state: State) {
            if (state != State.STATIC)  this.staticCtrl.resetDeltas()
            if (state != State.WALKING) this.walkingCtrl.resetDeltas()
            if (state != State.RUNNING) this.runningCtrl.resetDeltas()
            if (state != State.JUMPING) this.jumpingCtrl.resetDeltas()
        }

        if (this.isJumping && this.actionState != State.JUMPING) {
            this.jumpingCtrl.resetDeltas()
            this.jumpingCtrl.addDelta(20f)
            this.actionState = State.JUMPING
        }

        return when (actionState) {
            State.STATIC -> {
                resetState(State.STATIC)
                this.staticCtrl.addDelta(delta)
                this.staticCtrl.getTexture()
            }

            State.WALKING -> {
                resetState(State.WALKING)
                this.walkingCtrl.addDelta(delta)
                this.walkingCtrl.getTexture()
            }

            State.RUNNING -> {
                resetState(State.RUNNING)
                this.runningCtrl.addDelta(delta)
                this.runningCtrl.getTexture()
            }

            State.JUMPING -> {
                resetState(State.JUMPING)

                if (isJumping && floor(this.jumpingCtrl.getDeltas()) == 24f) {
                    return this.jumpingCtrl.getTexture()
                } else if (this.jumpingCtrl.getDeltas() > 30f) {
                    this.jumpingCtrl.resetDeltas()
                }

                this.jumpingCtrl.addDelta(delta)
                this.jumpingCtrl.getTexture()
            }
        }
    }

    override fun getRenderPointX(): Float = this.entityBody.position.x + this.offsetRender.x
    override fun getRenderPointY(): Float = this.entityBody.position.y + this.offsetRender.y

    enum class State{
        STATIC,
        WALKING,
        RUNNING,
        JUMPING
    }
}
