package com.turkeypetmiss.turkeydevpetroller.game.manager

import com.badlogic.gdx.Gdx
import com.turkeypetmiss.turkeydevpetroller.game.GDXGame
import com.turkeypetmiss.turkeydevpetroller.game.screens.*
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.runGDX

class NavigationManager(val game: GDXGame) {

    private val backStack = mutableListOf<String>()
    var key: Int? = null
        private set

    fun navigate(toScreenName: String, fromScreenName: String? = null, key: Int? = null) = runGDX {
        this.key = key

        game.updateScreen(getScreenByName(toScreenName))
        backStack.filter { name -> name == toScreenName }.onEach { name -> backStack.remove(name) }
        fromScreenName?.let { fromName ->
            backStack.filter { name -> name == fromName }.onEach { name -> backStack.remove(name) }
            backStack.add(fromName)
        }
    }

    fun back(key: Int? = null) = runGDX {
        this.key = key

        if (isBackStackEmpty()) exit() else game.updateScreen(getScreenByName(backStack.removeAt(backStack.lastIndex)))
    }


    fun exit() = runGDX { Gdx.app.exit() }


    fun isBackStackEmpty() = backStack.isEmpty()

    private fun getScreenByName(name: String): AdvancedScreen = when(name) {
        LoaderScreen  ::class.java.name -> LoaderScreen()
        WelcomeScreen ::class.java.name -> WelcomeScreen()
        TutorialScreen::class.java.name -> TutorialScreen()
        GameScreen    ::class.java.name -> GameScreen()
        ShopScreen    ::class.java.name -> ShopScreen()

        else -> GameScreen()
    }

}