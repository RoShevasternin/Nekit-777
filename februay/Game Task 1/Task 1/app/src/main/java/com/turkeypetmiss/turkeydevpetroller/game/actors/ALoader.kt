package com.turkeypetmiss.turkeydevpetroller.game.actors

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.turkeypetmiss.turkeydevpetroller.game.actors.progress.AProgressLoader
import com.turkeypetmiss.turkeydevpetroller.game.utils.Acts
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedGroup
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame
import kotlin.math.roundToInt

class ALoader(override val screen: AdvancedScreen): AdvancedGroup() {

    private val aBrandImg       = Image(gdxGame.assetsLoader.brand)
    private val aTitleImg       = Image(gdxGame.assetsLoader.title)
    private val aProgressLoader = AProgressLoader(screen)

    override fun addActorsOnGroup() {
        addBrandImg()
        addTitleImg()
        addProgressLoader()
    }

    // Actors ------------------------------------------------------------------------

    private fun addBrandImg() {
        addActor(aBrandImg)
        aBrandImg.setBounds(313f, 964f, 415f, 415f)
    }

    private fun addTitleImg() {
        addActor(aTitleImg)
        aTitleImg.setBounds(160f, 852f, 726f, 72f)

        // Початковий стан
        aTitleImg.color.a = 0f
        aTitleImg.setOrigin(Align.center)
        aTitleImg.setScale(0.95f)

        // Анімація
        aTitleImg.addAction(
            Acts.sequence(
                Acts.parallel(
                    Acts.fadeIn(0.6f, Interpolation.fade),
                    Acts.scaleTo(1f, 1f, 0.6f, Interpolation.swingOut)
                ),
                Acts.delay(0.2f),
                Acts.forever(
                    Acts.sequence(
                        Acts.scaleTo(1.03f, 1.03f, 1.2f, Interpolation.sine),
                        Acts.scaleTo(1f, 1f, 1.2f, Interpolation.sine)
                    )
                )
            )
        )
    }

    private fun addProgressLoader() {
        addActor(aProgressLoader)
        aProgressLoader.setBounds(160f, 777f, 725f, 36f)
    }

    // Logic ------------------------------------------------------------------------

    fun setProgressPercent(percent: Float) {
        aProgressLoader.setProgressPercent(percent)
    }

}