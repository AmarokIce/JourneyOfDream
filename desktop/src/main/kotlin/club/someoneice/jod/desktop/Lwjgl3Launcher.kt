@file:JvmName("Lwjgl3Launcher")

package club.someoneice.jod.desktop

import club.someoneice.jod.api.GameBasicInfo
import club.someoneice.jod.core.GameMain
import club.someoneice.jod.core.GameMain.ArchitectureOS
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return

    Lwjgl3Application(GameMain(ArchitectureOS.LWJGL), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Journey of Dream")
        setWindowedMode(GameBasicInfo.WINDOWS_WIDTH, GameBasicInfo.WINDOWS_HEIGHT)
        setWindowIcon(*(arrayOf(512, 128, 32).map { "catlogo$it.png" }.toTypedArray()))
    })

}
