@file:JvmName("DesktopLauncher")

package com.greyogproducts.greyog.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.tools.texturepacker.TexturePacker

import com.greyogproducts.greyog.MainClass

/** Launches the desktop (LWJGL) application. */
fun main(args: Array<String>) {
//    TexturePacker.processIfModified("../raw", "../assets", "sprites.atlas")
    LwjglApplication(MainClass(), LwjglApplicationConfiguration().apply {
        title = "game00"
        width = 1024
        height = 600
        resizable = false
        intArrayOf(128, 64, 32, 16).forEach{
            addIcon("libgdx$it.png", Files.FileType.Internal)
        }
    })
}
