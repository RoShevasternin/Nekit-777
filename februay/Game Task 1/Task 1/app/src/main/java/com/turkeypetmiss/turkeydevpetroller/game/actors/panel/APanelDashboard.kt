package com.turkeypetmiss.turkeydevpetroller.game.actors.panel

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.turkeypetmiss.turkeydevpetroller.game.actors.button.AButton
import com.turkeypetmiss.turkeydevpetroller.game.screens.GameScreen
import com.turkeypetmiss.turkeydevpetroller.game.screens.TutorialScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addAndFillActor
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedGroup
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame

class APanelDashboard(override val screen: AdvancedScreen): AdvancedGroup() {

    private val listDash = listOf(
        gdxGame.assetsAll.T1,
        gdxGame.assetsAll.T2,
    )

    private var currentIndex = 0

    private val aNextBtn  = AButton(screen, AButton.Type.Def)
    private val aPanelImg = Image(listDash[currentIndex])

    override fun addActorsOnGroup() {
        addPanelImg()
        addNextBtn()
    }

    // Actors ------------------------------------------------------------------------

    private fun addPanelImg() {
        addAndFillActor(aPanelImg)
    }

    private fun addNextBtn() {
        addActor(aNextBtn)
        aNextBtn.setBounds(168f, 231f, 705f, 131f)
        aNextBtn.setOnClickListener {
            currentIndex++
            if (currentIndex >= 2) {
                 screen.animHide {
                     if (gdxGame.ds_IsTutorial.flow.value) {
                         gdxGame.ds_IsTutorial.update { false }
                         gdxGame.navigationManager.navigate(TutorialScreen::class.java.name)
                     } else {
                         gdxGame.navigationManager.navigate(GameScreen::class.java.name)
                     }
                 }
            } else next()
        }
    }

    // Logic ----------------------------------------------

    private fun next() {
        aPanelImg.drawable = TextureRegionDrawable(listDash[currentIndex])
    }

}