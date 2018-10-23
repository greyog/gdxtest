package com.greyogproducts.greyog

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.app.KtxScreen
import ktx.graphics.use

class GameScreen : KtxScreen{
    companion object {
        const val SPRITE_SCALE = 0.025f
        const val SCREEN_WIDTH = 1024
        const val SCREEN_HEIGHT = 600
        val atlas = TextureAtlas("sprites.atlas")
    }

    private lateinit var gameWorld: GameWorld
    private lateinit var guiStage: Stage
    private lateinit var renderer: GameRenderer
    private lateinit var guiCamera: OrthographicCamera
    private lateinit var gameCamera: OrthographicCamera
    private val batch = SpriteBatch()


    override fun show() {
        guiStage = Stage()
        guiStage.viewport.update(SCREEN_WIDTH, SCREEN_HEIGHT, false)
        val orange = Image()
        val sprite = GameScreen.atlas.createSprite("orange")
        orange.drawable = TextureRegionDrawable(TextureRegion(sprite))
        orange.setPosition(0f,0f)
        orange.setSize(50f, 50f)
        guiStage.addActor(orange)

        gameWorld = GameWorld()
        renderer = GameRenderer(gameWorld)
        gameCamera = gameWorld.stage.camera as OrthographicCamera
        guiCamera = guiStage.camera as OrthographicCamera
        guiCamera.position.set((SCREEN_WIDTH/2).toFloat(), (SCREEN_HEIGHT/2).toFloat(), 0f)

    }


    private var sourceX = 0
    private var sourceY = 0

    private val v1 = Vector2()
    private val v2 = Vector2()

    override fun render(delta: Float) {
        ktx.app.clearScreen(0f,0f,0f)
//       draw background
//        batch.projectionMatrix = gameWorld.stage.camera.combined
//        v1.set(gameCamera.position.x, gameCamera.position.y)
        v1.set(gameWorld.car.x, gameWorld.car.y)
        v2.set(gameWorld.stage.screenToStageCoordinates(v1))
        sourceX = v2.x.toInt()
        sourceY = v2.y.toInt()
//        println("stX = $sourceX , soY = $sourceY")
        batch.use {
            it.draw(gameWorld.bckTexture, 0f, 0f,
                    sourceX,
                    sourceY,
                    GameScreen.SCREEN_WIDTH,
                    GameScreen.SCREEN_HEIGHT)
        }
//        guiCamera.update()
        gameWorld.update(delta)
        guiStage.act(delta)

        renderer.render(delta)
        guiStage.draw()
    }
}
