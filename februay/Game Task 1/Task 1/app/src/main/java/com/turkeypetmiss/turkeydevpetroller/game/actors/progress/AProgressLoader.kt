package com.turkeypetmiss.turkeydevpetroller.game.actors.progress

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.turkeypetmiss.turkeydevpetroller.game.actors.mask.AOldMask
import com.turkeypetmiss.turkeydevpetroller.game.utils.WIDTH_UI
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addAndFillActor
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedGroup
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame
import com.turkeypetmiss.turkeydevpetroller.game.utils.runGDX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AProgressLoader(override val screen: AdvancedScreen): AdvancedGroup() {

    private val LENGTH = 725f

    private val backgroundImage = Image(gdxGame.assetsLoader.progress_back)
    private val progressImage   = Image(gdxGame.assetsLoader.progress)
    private val mask            = AOldMask(screen, gdxGame.assetsLoader.MASK, alphaWidth = WIDTH_UI.toInt())

    private val onePercentX = LENGTH / 100f

    // 0 .. 100 %
    val progressPercentFlow = MutableStateFlow(0f)


    override fun addActorsOnGroup() {
        addAndFillActor(backgroundImage)
        addMask()

        coroutine?.launch {
            progressPercentFlow.collect { percent ->
                runGDX {
                    progressImage.x = (percent * onePercentX) - LENGTH
                }
            }
        }

        //addListener(inputListener())
    }

    // ---------------------------------------------------
    // Add Actors
    // ---------------------------------------------------

    private fun AdvancedGroup.addMask() {
        addAndFillActor(mask)
        mask.addProgress()
    }

    private fun AdvancedGroup.addProgress() {
        addAndFillActor(progressImage)
    }

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

    private fun inputListener() = object : InputListener() {
        override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            touchDragged(event, x, y, pointer)
            return true
        }

        override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            progressPercentFlow.value = when {
                y <= 0 -> 0f
                y >= LENGTH -> 100f
                else -> y / onePercentX
            }

            event?.stop()
        }
    }

    fun setProgressPercent(percent: Float) {
        progressPercentFlow.value = percent
    }


}