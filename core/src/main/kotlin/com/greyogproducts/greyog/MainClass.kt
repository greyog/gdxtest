package com.greyogproducts.greyog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.codeandweb.physicseditor.PhysicsShapeCache
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.use

private class FirstScreen : KtxScreen, InputProcessor {
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
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        car?.setLinearVelocity(0.5f,0.1f)
        return false
    }

    companion object {
        const val SPRITE_SCALE = 0.025f
        const val SCREEN_WIDTH = 1024
        const val SCREEN_HEIGHT = 600
        private const val STEP_TIME = 1f/60f
        private const val VELOCITY_ITERS = 6
        private const val POSITION_ITERS = 2
        val atlas = TextureAtlas("sprites.atlas")
    }
    private val image = Texture("ktx-logo.png")
    private val batch = SpriteBatch()

//    private val car = atlas.createSprite("car")
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(50f, 50f, camera)
    private val sprites = addSprites()
    private val world = World(Vector2(0f, 0f), true)
    private val physBodies = PhysicsShapeCache("sprites.xml")

    private var accumulator = 0f

    private var debugRenderer: Box2DDebugRenderer

    private var car: Body?

    private var ground: Body? = null
//    private val stage = Stage(viewport)
    init {
        Box2D.init()
        debugRenderer = Box2DDebugRenderer()
        car = createBody("car", 0f, 0f, MathUtils.PI/2)
        Gdx.input.inputProcessor = this
    }

    private fun stepWorld(delta: Float) {
        val delte = Gdx.graphics.deltaTime
        accumulator += Math.min(delte, 0.25f)

        if (accumulator>= STEP_TIME){
            accumulator -= STEP_TIME
            world.step(STEP_TIME, VELOCITY_ITERS, POSITION_ITERS)
        }
    }
    private fun addSprites(): Map<String, Sprite> {
        val hm = emptyMap<String,Sprite>().toMutableMap()
        atlas.regions.forEach {
            val sprite = atlas.createSprite(it.name)
            val newWidth = sprite.width * SPRITE_SCALE
            val newHeight = sprite.height * SPRITE_SCALE
            sprite.setSize(newWidth, newHeight)
            sprite.setOrigin(0f,0f)
            hm[it.name] = sprite
        }
        return hm.toMap()
    }

    override fun render(delta: Float) {
        stepWorld(delta)
        clearScreen(0.0f, 0.0f, 0.0f)
        batch.use {
//            it.draw(car,0f,0f)
            drawSprite("car",
                    car!!.position.x,
                    car!!.position.y,
                    MathUtils.radiansToDegrees*car!!.angle,
                    it)
//            drawSprite("cherries", 5f,5f, it)
        }
        debugRenderer.render(world, camera.combined)
    }

    private fun drawSprite(name: String, x: Float, y: Float, angle: Float, batch: SpriteBatch) {

        val sprite = sprites[name]
        sprite?.setPosition(x,y)
        sprite?.rotation = angle
        sprite?.setOrigin(0f,0f)
        sprite?.draw(batch)
    }

    private fun createBody(name: String, x: Float, y: Float, rotation: Float): Body? {
        val body = physBodies.createBody(name,world, SPRITE_SCALE, SPRITE_SCALE)
        body.setTransform(x,y,rotation)
        return body
    }

    private fun createGround() {
        if (ground != null) world.destroyBody(ground)
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        val fixtureDef = FixtureDef()
        val shape = PolygonShape()
        shape.setAsBox(camera.viewportWidth, 1f)
        fixtureDef.shape = shape
        fixtureDef.friction = 0.1f

        ground = world.createBody(bodyDef)
        with(ground!!) {
            createFixture(fixtureDef)
            setTransform(0f,0f,0f)
        }

        shape.dispose()
    }

    override fun dispose() {
        image.dispose()
        batch.dispose()
        atlas.dispose()
        debugRenderer.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        batch.projectionMatrix = camera.combined
//        camera.rotate(20f)
//        camera.update()
//        createGround()
    }
}

class MainClass : KtxGame<KtxScreen>() {
    override fun create() {
//        addScreen(FirstScreen())
//        setScreen<FirstScreen>()
        Gdx.app.logLevel = 3
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}
