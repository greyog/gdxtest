package com.greyogproducts.greyog

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer

class GameRenderer(private val gameWorld: GameWorld) {
    private val renderer = Box2DDebugRenderer()
    private val camera = gameWorld.stage.camera


    init {
//        camera.position.x = gameWorld.car.body.position.x
//        camera.position.y = gameWorld.car.body.position.y
        camera.position.x = gameWorld.car.x
        camera.position.y = gameWorld.car.y
    }
//    camera interpolation lerp
    private val lerp = 0.01f

    fun render(delta: Float) {
// camera follow car
        camera.position.x += (gameWorld.car.x - camera.position.x) * lerp * delta
        camera.position.y += (gameWorld.car.y - camera.position.y) * lerp * delta
//        camera.position.x = gameWorld.car.x
//        camera.position.y = gameWorld.car.y

//        clearScreen(0.0f, 0.0f, 0.0f)
//        debug render
        camera.update()
//        renderer.render(gameWorld.phWorld, camera.combined)

//        game stage
        gameWorld.stage.draw()

    }
}
