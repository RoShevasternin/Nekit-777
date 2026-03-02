package com.turkeypetmiss.turkeydevpetroller.game.actors.panel

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addActors
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addAndFillActor
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedGroup
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.font.FontParameter
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame
import com.turkeypetmiss.turkeydevpetroller.game.utils.runGDX
import kotlinx.coroutines.launch

class APanelBalance(override val screen: AdvancedScreen): AdvancedGroup() {

    private val fontParameter = FontParameter().setCharacters(FontParameter.CharType.NUMBERS)
    private val font66        = screen.fontGenerator_SemiBold.generateFont(fontParameter.setSize(66))

    private val ls66 = Label.LabelStyle(font66, Color.valueOf("242424"))

    private val aNeftLbl = Label("0", ls66)
    private val aGoldLbl = Label("0", ls66)

    private val aPanelBalanceImg = Image(gdxGame.assetsAll.panel_balance)

    override fun addActorsOnGroup() {
        addAndFillActor(aPanelBalanceImg)

        addActors(aNeftLbl, aGoldLbl)
        aNeftLbl.setBounds(182f, 29f, 131f, 67f)
        aGoldLbl.setBounds(645f, 29f, 131f, 67f)

        coroutine?.launch {
            launch { gdxGame.ds_Gold.flow.collect { runGDX { aGoldLbl.setText(it) } } }
            launch { gdxGame.ds_Gel.flow.collect { runGDX { aNeftLbl.setText(it) } } }
        }
    }

}