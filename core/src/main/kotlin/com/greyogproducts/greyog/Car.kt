package com.greyogproducts.greyog

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Scaling

class Car(private val world: GameWorld): Image() {
    companion object {
        private const val OBJ_SCALE = 2f
        const val OBJ_WIDTH = GameWorld.UNIT_WIDTH * OBJ_SCALE
        const val OBJ_HEIGHT = GameWorld.UNIT_HEIGHT * OBJ_SCALE
        private const val VELOCITY = 10f
    }
    enum class State {
        TURN_RIGHT, TURN_LEFT, NO_TURN, BRAkE
    }
    val body: Body
    var state = State.NO_TURN
        set(value) {
            field = value
            moveMe = when(value){
                State.TURN_RIGHT -> {
                    {turnMe(1, it)}
                }
                State.TURN_LEFT -> {
                    {turnMe(-1, it)}
                }
                else -> {
                    {moveMeAhead(it)}
                }
            }
        }

    private var moveMe:(Float)-> Unit = {moveMeAhead(it)}


    init {
        val sprite = GameScreen.atlas.createSprite("car")
        drawable = TextureRegionDrawable(TextureRegion(sprite))

        val shape = PolygonShape()
        shape.setAsBox(OBJ_WIDTH, OBJ_HEIGHT)

        val bodyDef = BodyDef()
        with(bodyDef) {
            type = BodyDef.BodyType.DynamicBody
            position.x = 0f
            position.y = 0f
            linearDamping = 0.1f
            angularDamping = 0.5f
        }
        body = world.phWorld.createBody(bodyDef)

        val fixtureDef = FixtureDef()
        with(fixtureDef){
            density = 1f
            friction = 0.3f
            restitution = 0.8f
        }
        shape.dispose()

//        this.setPosition(body.position.x - RADIUS, body.position.y - RADIUS)
        this.setSize(OBJ_WIDTH, OBJ_HEIGHT)
        this.setScaling(Scaling.stretch)
//        this.setAlign(Align.center)
        setOrigin(OBJ_WIDTH/2, OBJ_HEIGHT/2)
        this.setPosition(body.position.x, body.position.y)

//        rotation= 90f

//        body.setLinearVelocity(20f,0f)

    }

    override fun act(delta: Float) {
        super.act(delta)
//        rotation = MathUtils.radiansToDegrees * body.angle
//        setPosition(body.position.x, body.position.y)
        moveMe(delta)

    }

    private val rotRadius = 10f
    private val angVel = VELOCITY / rotRadius

    private fun moveMeAhead(delta: Float) {
        x += VELOCITY * MathUtils.cosDeg(rotation) * delta
        y += VELOCITY * MathUtils.sinDeg(rotation) * delta
    }

    private fun turnMe(direction: Int, delta: Float) {
        rotation -= direction * (angVel * delta * MathUtils.radiansToDegrees)
        rotation = rotation.rem(360)
        x += VELOCITY * MathUtils.sinDeg(rotation + 90) * delta
        y -= VELOCITY * MathUtils.cosDeg(rotation + 90) * delta
//        println("delta = $delta, x= $x y = $y, rotation = $rotation")
    }

}
