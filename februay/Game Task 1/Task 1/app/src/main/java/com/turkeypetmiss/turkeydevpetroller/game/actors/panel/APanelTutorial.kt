package com.turkeypetmiss.turkeydevpetroller.game.actors.panel

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.turkeypetmiss.turkeydevpetroller.game.actors.button.AButton
import com.turkeypetmiss.turkeydevpetroller.game.screens.GameScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedGroup
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.font.FontParameter
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame

class APanelTutorial(override val screen: AdvancedScreen): AdvancedGroup() {

    private val fontParameter = FontParameter().setCharacters("1-LVL")
    private val font66        = screen.fontGenerator_SemiBold.generateFont(fontParameter.setSize(66))

    private val ls66 = Label.LabelStyle(font66, Color.WHITE)

    private val aDashboardImg   = Image(gdxGame.assetsAll.T3)
    private val aNextBtn        = AButton(screen, AButton.Type.Def)
    private val aShopBtn        = AButton(screen, AButton.Type.Shop)

    private val aPanelBalance = APanelBalance(screen)

    private val aPanelLvlImg = Image(gdxGame.assetsAll.panel_lvl)
    private val aLvlLbl      = Label("1-LVL", ls66)

    override fun addActorsOnGroup() {
        addDashboardImg()
        addPanelBalance()
        addPanelLvl()
        addNextBtn()
        addShopBtn()
    }

    // Actors ------------------------------------------------------------------------

    private fun addDashboardImg() {
        addActor(aDashboardImg)
        aDashboardImg.setBounds(0f, 0f, width, 1824f)
    }

    private fun addPanelBalance() {
        addActor(aPanelBalance)
        aPanelBalance.setBounds(83f, 1828f, 876f, 126f)
    }

    private fun addPanelLvl() {
        addActor(aPanelLvlImg)
        aPanelLvlImg.setBounds(83f, 2023f, 318f, 168f)

        addActor(aLvlLbl)
        aLvlLbl.setBounds(135f, 2073f, 187f, 67f)
    }

    private fun addNextBtn() {
        addActor(aNextBtn)
        aNextBtn.setBounds(169f, 129f, 705f, 131f)
        aNextBtn.setOnClickListener {
            screen.animHide {
                gdxGame.navigationManager.navigate(GameScreen::class.java.name)
            }
        }
    }

    private fun addShopBtn() {
        addActor(aShopBtn)
        aShopBtn.setBounds(640f, 2023f, 318f, 168f)
        aShopBtn.disable(false)
    }

}