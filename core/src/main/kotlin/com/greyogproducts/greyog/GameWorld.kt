package com.greyogproducts.greyog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.TextureData
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class GameWorld {
    companion object {
        const val UNIT_WIDTH = GameScreen.SCREEN_WIDTH * GameScreen.SPRITE_SCALE
        const val UNIT_HEIGHT = GameScreen.SCREEN_HEIGHT * GameScreen.SPRITE_SCALE
        private const val STEP_TIME = 1f/60f
        private const val VELOCITY_ITERS = 6
        private const val POSITION_ITERS = 3
    }
    val GRAVITY = Vector2(0f, 0f)
    val stage = Stage()
    val phWorld = World(GRAVITY, true)
    lateinit var car: Car
    lateinit var bckTexture: Texture


    init {
        stage.viewport.update(UNIT_WIDTH.toInt(), UNIT_HEIGHT.toInt(), false)
        Gdx.app.log(this.javaClass.name,"$UNIT_WIDTH, $UNIT_HEIGHT")

        createMyWorld()
    }

    var stepX: Int = 0

    private fun createMyWorld() {
        stage.addActor(ScreenActor())

        car = Car(this)
        stage.addActor(car)

        stage.addListener {
            Gdx.app.log("eventListener", "event = $it")
            return@addListener false
        }

        bckTexture = Texture(Gdx.files.internal("background.png"))
        bckTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)

        Gdx.input.inputProcessor = object : InputProcessor {
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                car.state = Car.State.NO_TURN
                return false
            }

            override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
                return false
            }

            override fun keyTyped(character: Char): Boolean {
                return false
            }

            override fun scrolled(amount: Int): Boolean {
                return false
            }

            override fun keyUp(keycode: Int): Boolean {
                return false
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                return false
            }

            override fun keyDown(keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.RIGHT -> stepX += 10
                    Input.Keys.LEFT -> stepX -= 10
                }
                return false
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
//                Gdx.app.log(this.toString(),"${stage.stageToScreenCoordinates(Vector2(screenX.toFloat(),screenY.toFloat()))}")
                if (screenX >= GameScreen.SCREEN_WIDTH / 2) car.state = Car.State.TURN_RIGHT
                else car.state = Car.State.TURN_LEFT
                return false
            }

        }

    }

    private var accumulator = 0f

    fun update(delta: Float) {
//        accumulator += Math.min(delta, 0.25f)
//
//        if (accumulator>= STEP_TIME){
//            accumulator -= STEP_TIME
//            phWorld.step(STEP_TIME, VELOCITY_ITERS, POSITION_ITERS)
//        }

        stage.act(delta)

    }

    class ScreenActor : Actor() {
        override fun act(delta: Float) {
            super.act(delta)
            val viewport = stage.viewport
            width = viewport.screenWidth.toFloat()
            height = viewport.screenHeight.toFloat()
            x = viewport.screenX.toFloat()
            y= viewport.screenY.toFloat()
        }
    }
}