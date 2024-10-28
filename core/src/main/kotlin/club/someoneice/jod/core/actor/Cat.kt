package club.someoneice.jod.core.actor

import club.someoneice.jod.api.Entity
import club.someoneice.jod.data.CtrlSets
import club.someoneice.jod.util.AnimationController
import club.someoneice.jod.util.KeyInputHolder
import club.someoneice.jod.util.ResourceUtil.createTexturesArray
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class Cat(pos: Vector2 = Vector2.Zero): Entity(pos = pos) {
    val staticCtrl = AnimationController(5.0f, *createTexturesArray("textures/cat/static/cat", 6))
    val walkingCtrl = AnimationController(5.0f, *createTexturesArray("textures/cat/walk/cat_walk", 7))
    val runningCtrl = AnimationController(5.0f, *createTexturesArray("textures/cat/running/cat_running", 10))
    val jumpingCtrl = AnimationController(5.0f, *createTexturesArray("textures/cat/jump/cat_jumping", 6))
    val stretchCtrl = AnimationController(5.0f, *createTexturesArray("textures/cat/stretch/cat_stretch", 16))

    var actionState = State.STATIC
    var directionRight = true
    var notGrounded = false

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

        if (jumpingCtrl.getDeltas() > 5 * 6) {
            this.jumpingCtrl.resetDeltas()
            this.notGrounded = false
            this.actionState = State.STATIC
        }

        this.entityBody.isAwake = this.actionState != State.STATIC && this.actionState != State.STRETCH

        if (this.stretchCtrl.getDeltas() > 16 * 5) {
            this.actionState = State.STATIC
        }

        if (this.staticCtrl.getDeltas() > 3 * 6 * 5) {
            this.actionState = State.STRETCH
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

    fun renderWith(batch: Batch, delta: Float, tex: Texture) {
        this.update(delta)
        val texture = updateStateAndGetTexture(delta)

        val region = TextureRegion(tex, 0, 0, tex.width, tex.height)
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

        val flag = keyMoving != -1
        if (flag) {
            this.directionRight = CtrlSets.RIGHT_MOVING_KEYS.contains(keyMoving)
        }

        if (this.notGrounded || this.actionState == State.JUMPING) {
            return
        }

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
            if (catBodyPos.x <= 1532f) {
                this.camera.x = max(this.camera.x - speed, 480f)
            }

            if (catBodyPos.x > 0) {
                catBodyPos.x = max(catBodyPos.x - speed, 0f)
            }
        } else if (CtrlSets.RIGHT_MOVING_KEYS.contains(keyInput)) {
            if (catBodyPos.x >= 480f) {
                this.camera.x = min(this.camera.x + speed, 1532f)
            }

            if (catBodyPos.x < 1932f) {
                catBodyPos.x = min(catBodyPos.x + speed, 1932f)
            }
        }

        if (input.containsAny(CtrlSets.JUMP_KEYS) && this.actionState != State.JUMPING) {
            this.actionState = State.JUMPING
        }

        if (this.jumpingCtrl.getDeltas() == 5f) {
            this.entityBody.applyLinearImpulse(Vector2(this.entityBody.linearVelocity.x, this.entityBody.mass * 265f), this.entityBody.position, true)
        }

        this.notGrounded = this.entityBody.linearVelocity.y != 0f
        this.entityBody.setTransform(catBodyPos, this.entityBody.angle)
        this.entityBody.setLinearVelocity(this.entityBody.linearVelocity.x, this.entityBody.linearVelocity.y)
    }

    fun updateStateAndGetTexture(delta: Float): Texture {
        fun resetState(state: State) {
            if (state != State.STATIC)  this.staticCtrl.resetDeltas()
            if (state != State.WALKING) this.walkingCtrl.resetDeltas()
            if (state != State.RUNNING) this.runningCtrl.resetDeltas()
            if (state != State.JUMPING) this.jumpingCtrl.resetDeltas()
            if (state != State.STRETCH) this.stretchCtrl.resetDeltas()
        }

        if (this.notGrounded && this.actionState != State.JUMPING) {
            return this.jumpingCtrl.textures[4]
        }

        return when (actionState) {
            State.STATIC -> {
                resetState(State.STATIC)
                this.staticCtrl.addDelta(delta)
                this.staticCtrl.getTexture()
            }

            State.STRETCH -> {
                resetState(State.STRETCH)
                this.stretchCtrl.addDelta(delta)
                this.stretchCtrl.getTexture()
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

                if (notGrounded && floor(this.jumpingCtrl.getDeltas()) == 24f) {
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
        JUMPING,
        STRETCH
    }
}
