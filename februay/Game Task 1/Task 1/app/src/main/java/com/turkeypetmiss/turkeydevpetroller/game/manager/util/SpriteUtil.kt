package com.turkeypetmiss.turkeydevpetroller.game.manager.util

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.turkeypetmiss.turkeydevpetroller.game.manager.SpriteManager

class SpriteUtil {

    class Loader {
        private fun getRegion(name: String): TextureRegion = SpriteManager.EnumAtlas.LOADER.data.atlas.findRegion(name)

        val brand         = getRegion("brand")
        val progress      = getRegion("progress")
        val progress_back = getRegion("progress_back")
        val title         = getRegion("title")

        val BACKGROUND = SpriteManager.EnumTexture.BACKGROUND.data.texture
        val MASK       = SpriteManager.EnumTexture.MASK.data.texture
    }

    class All {
        private fun getAllRegion(name: String): TextureRegion = SpriteManager.EnumAtlas.ALL.data.atlas.findRegion(name)

        // atlas All ------------------------------------------------------------------------------

        val btn_def   = getAllRegion("btn_def")
        val btn_press = getAllRegion("btn_press")

        val barel          = getAllRegion("barel")
        val btn_shop_def   = getAllRegion("btn_shop_def")
        val btn_shop_press = getAllRegion("btn_shop_press")
        val gel_def        = getAllRegion("gel_def")
        val gel_press      = getAllRegion("gel_press")
        val nafta_def      = getAllRegion("nafta_def")
        val nafta_press    = getAllRegion("nafta_press")
        val panel_balance  = getAllRegion("panel_balance")
        val panel_lvl      = getAllRegion("panel_lvl")

        //val listGlarePanelGame = List(4) { getAllRegion("glare_panel_game_${it.inc()}") }

        // textures ------------------------------------------------------------------------------

        val BACKGROUND_GAME = SpriteManager.EnumTexture.BACKGROUND_GAME.data.texture
        val BACKGROUND_SHOP = SpriteManager.EnumTexture.BACKGROUND_SHOP.data.texture

        val T1 = SpriteManager.EnumTexture.T1.data.texture
        val T2 = SpriteManager.EnumTexture.T2.data.texture
        val T3 = SpriteManager.EnumTexture.T3.data.texture

        val BUTTONS = SpriteManager.EnumTexture.BUTTONS.data.texture

        private val _1 = SpriteManager.EnumTexture._1.data.texture
        private val _2 = SpriteManager.EnumTexture._2.data.texture
        private val _3 = SpriteManager.EnumTexture._3.data.texture
        private val _4 = SpriteManager.EnumTexture._4.data.texture
        private val _5 = SpriteManager.EnumTexture._5.data.texture
        private val _6 = SpriteManager.EnumTexture._6.data.texture
        private val _7 = SpriteManager.EnumTexture._7.data.texture
        private val _8 = SpriteManager.EnumTexture._8.data.texture
        private val _9 = SpriteManager.EnumTexture._9.data.texture
        private val _10 = SpriteManager.EnumTexture._10.data.texture

        val listLevels = listOf(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10)

    }

}