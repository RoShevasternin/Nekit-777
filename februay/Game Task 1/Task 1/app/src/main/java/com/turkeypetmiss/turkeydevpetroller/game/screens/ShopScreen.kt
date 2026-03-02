package com.turkeypetmiss.turkeydevpetroller.game.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.turkeypetmiss.turkeydevpetroller.game.actors.ATmpGroup
import com.turkeypetmiss.turkeydevpetroller.game.actors.button.AButton
import com.turkeypetmiss.turkeydevpetroller.game.actors.panel.APanelBalance
import com.turkeypetmiss.turkeydevpetroller.game.utils.Block
import com.turkeypetmiss.turkeydevpetroller.game.utils.TIME_ANIM_SCREEN
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addActorWithConstraints
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addAndFillActor
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animDelay
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animHide
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animShow
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.setOnClickListener
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.font.FontParameter
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame

class ShopScreen: AdvancedScreen() {

    private val fontParameter = FontParameter().setCharacters(FontParameter.CharType.NUMBERS.chars + "/")
    private val font44        = fontGenerator_SemiBold.generateFont(fontParameter.setSize(44))

    private val ls44 = Label.LabelStyle(font44, Color.valueOf("3C3C3C"))

    private val aPanelBalance  = APanelBalance(this)
    private val aNaftaBtn      = AButton(this, AButton.Type.Nafta)
    private val aGelBtn        = AButton(this, AButton.Type.Gel)

    private val aPanelImg = Image(gdxGame.assetsAll.BUTTONS)

    private val listLbl = List(6) { Label("", ls44) }
    private val listA   = List(6) { Actor() }

    private val ddPokupki = gdxGame.ds_PokupkiData.flow.value

    override fun show() {
        setBackBackground(gdxGame.assetsAll.BACKGROUND_SHOP)
        super.show()
    }

    override fun Group.addActorsOnStageUI() {
        color.a = 0f

        addPanelBalance()
        addBtns()
        addPanelGroup()

        animShow()
    }

    override fun animHide(blockEnd: Block) {
        stageUI.root.animHide(TIME_ANIM_SCREEN)
        stageUI.root.animDelay(TIME_ANIM_SCREEN) { blockEnd() }
    }

    override fun animShow(blockEnd: Block) {
        //stageUI.root.children.onEach { it.clearActions() }

        stageUI.root.animShow(TIME_ANIM_SCREEN)
        stageUI.root.animDelay(TIME_ANIM_SCREEN) { blockEnd() }
    }

    // Actors ------------------------------------------------------------------------

    private fun Group.addPanelBalance() {
        aPanelBalance.setSize(874f, 125f)
        addActorWithConstraints(aPanelBalance) {
            topToTopOf     = this@addPanelBalance
            startToStartOf = this@addPanelBalance
            endToEndOf     = this@addPanelBalance

            marginTop = 97f
        }

    }

    private fun Group.addBtns() {
        aNaftaBtn.setSize(425f, 140f)
        aGelBtn.setSize(425f, 140f)
        addActorWithConstraints(aNaftaBtn) {
            startToStartOf   = this@addBtns
            bottomToBottomOf = this@addBtns

            marginStart  = 83f
            marginBottom = 67f
        }
        addActorWithConstraints(aGelBtn) {
            endToEndOf       = this@addBtns
            bottomToBottomOf = this@addBtns

            marginEnd    = 83f
            marginBottom = 67f
        }

        aNaftaBtn.setOnClickListener {
            if (gdxGame.ds_Gel.flow.value > 0) {
                gdxGame.ds_Gold.update { it + (gdxGame.ds_Gel.flow.value * 2.35f).toInt() }
                gdxGame.ds_Gel.update { 0 }
            }
        }
        aGelBtn.setOnClickListener {
            this@ShopScreen.animHide {
                gdxGame.navigationManager.navigate(GameScreen::class.java.name)
            }
        }

    }

    private fun Group.addPanelGroup() {
        val aGroup = ATmpGroup(this@ShopScreen)
        aGroup.setSize(875f, 1560f)
        addActorWithConstraints(aGroup) {
            startToStartOf   = this@addPanelGroup
            endToEndOf       = this@addPanelGroup
            topToBottomOf    = aPanelBalance

            marginTop = 124f
        }

        aGroup.apply {
            addAndFillActor(aPanelImg)
            addShopBtns()
        }
    }

    private fun Group.addShopBtns() {
        var nx = 0f
        var ny = 1070f

        val listLblValue = listOf(2, 2, 2, 2, 1, 1)
        val listPrice    = listOf(500, 1000, 5000, 10000, 15000, 25000)

        listA.forEachIndexed { index, button ->
            addActor(button)
            button.setBounds(nx, ny, 416f, 488f)

            listLbl[index].also { lbl ->
                addActor(lbl)
                lbl.setBounds(nx + 27, ny + 419, 71f, 44f)

                lbl.setText("${ddPokupki[index]}/${listLblValue[index]}")

                if (index.inc() > 4) {
                    lbl.y -= 20f
                }
            }

            nx += 42 + 416
            if (index.inc() % 2 == 0) {
                nx = 0f
                ny -= 25 + 488
            }

            button.setOnClickListener {
                if (
                    gdxGame.ds_PokupkiData.flow.value[index] < listLblValue[index] &&
                    gdxGame.ds_Gold.flow.value >= listPrice[index]
                ) {
                    gdxGame.soundUtil.apply { play(buy_coin) }

                    gdxGame.ds_Gold.update { it - listPrice[index] }
                    gdxGame.ds_PokupkiData.update {
                        val mList = it.toMutableList()
                        mList[index] += 1
                        mList
                    }
                    gdxGame.ds_Level.update { if (it < 9) it + 1 else it }

                    this@ShopScreen.animHide {
                        gdxGame.navigationManager.navigate(GameScreen::class.java.name)
                    }
                } else gdxGame.soundUtil.apply { play(fail_buy_coin) }
            }
        }
    }



}