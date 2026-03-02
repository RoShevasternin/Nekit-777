package com.turkeypetmiss.turkeydevpetroller.game.screens

import com.badlogic.gdx.scenes.scene2d.Group
import com.turkeypetmiss.turkeydevpetroller.game.actors.panel.APanelDashboard
import com.turkeypetmiss.turkeydevpetroller.game.utils.Block
import com.turkeypetmiss.turkeydevpetroller.game.utils.TIME_ANIM_SCREEN
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.HAlign
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.VAlign
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addActorAligned
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animDelay
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animHide
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animShow
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame

class WelcomeScreen: AdvancedScreen() {

    private val aPanelDashboard = APanelDashboard(this)

    override fun show() {
        setBackBackground(gdxGame.assetsLoader.BACKGROUND)
        super.show()
    }

    override fun Group.addActorsOnStageUI() {
        color.a = 0f

        addPanelDashboard()

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

    private fun Group.addPanelDashboard() {
        aPanelDashboard.setSize(1040f, 992f)
        addActorAligned(aPanelDashboard, HAlign.CENTER, VAlign.BOTTOM)
    }

}