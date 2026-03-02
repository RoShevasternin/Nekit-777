package com.turkeypetmiss.turkeydevpetroller.game.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.turkeypetmiss.turkeydevpetroller.game.actors.ATmpGroup
import com.turkeypetmiss.turkeydevpetroller.game.actors.button.AButton
import com.turkeypetmiss.turkeydevpetroller.game.actors.panel.APanelBalance
import com.turkeypetmiss.turkeydevpetroller.game.utils.Block
import com.turkeypetmiss.turkeydevpetroller.game.utils.TIME_ANIM_SCREEN
import com.turkeypetmiss.turkeydevpetroller.game.utils.WIDTH_UI
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.HAlign
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.VAlign
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addActorAligned
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addActorWithConstraints
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addAndFillActor
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addAndFillActors
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animDelay
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animHide
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animShow
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.disable
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.setOnClickListener
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.setOnClickListenerWithBlock
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.font.FontParameter
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame
import com.turkeypetmiss.turkeydevpetroller.game.utils.runGDX
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.get

class GameScreen: AdvancedScreen() {

    private val fontParameter = FontParameter().setCharacters(FontParameter.CharType.NUMBERS.chars + "-LVL")
    private val font66        = fontGenerator_SemiBold.generateFont(fontParameter.setSize(66))

    private val ls66 = Label.LabelStyle(font66, Color.WHITE)

    private val aShopBtn      = AButton(this, AButton.Type.Shop)
    private val aPanelBalance = APanelBalance(this)


    private val aPanelLvlImg = Image(gdxGame.assetsAll.panel_lvl)
    private val aLvlLbl      = Label("${gdxGame.ds_Level.flow.value.inc()}-LVL", ls66)

    private val listClick         = List(75) { Image(gdxGame.assetsAll.barel) }
    private var currentClickIndex = 0

    override fun show() {
        setBackBackground(gdxGame.assetsAll.BACKGROUND_GAME)
        super.show()
    }

    override fun Group.addActorsOnStageUI() {
        color.a = 0f

        addAndFillActor(Image(gdxGame.assetsAll.listLevels[gdxGame.ds_Level.flow.value]).apply { this.disable() })
        listClick.onEach {
            addActor(it)
            it.setPosition(WIDTH_UI + 100f, 0f)
        }

        addPanelLbl()
        addShopBtn()
        addPanelBalance()

        animShow()


        this.setOnClickListenerWithBlock(
            touchDownBlock = { x, y ->
                gdxGame.soundUtil.apply { play(touch_oil) }

                gdxGame.ds_Gel.update { it + 1 }

                if (currentClickIndex == listClick.lastIndex) currentClickIndex = 0

                listClick[currentClickIndex++].apply {
                    this.setPosition(x - 110f, y - 95f)
                    launchGaz()
                }
            }
        )
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

    private fun Group.addPanelLbl() {
        val aGroup = ATmpGroup(this@GameScreen)
        aGroup.setSize(318f, 166f)
        addActorWithConstraints(aGroup) {
            topToTopOf     = this@addPanelLbl
            startToStartOf = this@addPanelLbl

            marginTop   = 67f
            marginStart = 83f
        }

        aGroup.addAndFillActors(aPanelLvlImg, aLvlLbl)
        aLvlLbl.setAlignment(Align.center)
    }

    private fun Group.addShopBtn() {
        aShopBtn.setSize(318f, 166f)
        addActorWithConstraints(aShopBtn) {
            topToTopOf = this@addShopBtn
            endToEndOf = this@addShopBtn

            marginTop = 67f
            marginEnd = 83f
        }

        aShopBtn.setOnClickListener {
            this@GameScreen.animHide {
                gdxGame.navigationManager.navigate(ShopScreen::class.java.name, this@GameScreen::class.java.name)
            }
        }

    }

    private fun Group.addPanelBalance() {
        aPanelBalance.setSize(874f, 125f)
        addActorWithConstraints(aPanelBalance) {
            topToBottomOf  = aShopBtn
            startToStartOf = this@addPanelBalance
            endToEndOf     = this@addPanelBalance

            marginTop = 71f
        }

    }

    // Logic ----------------------------------------------------------------------

    private fun Image.launchGaz() {
        coroutine?.launch {
            delay(500)
            runGDX {
                animHide(0.5f) {
                    setPosition(WIDTH_UI+100f, 0f)
                    animShow()
                }
            }
        }
    }

}