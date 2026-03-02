package com.turkeypetmiss.turkeydevpetroller.game.screens

import com.badlogic.gdx.scenes.scene2d.Group
import com.turkeypetmiss.turkeydevpetroller.game.GDX_GLOBAL_isGame
import com.turkeypetmiss.turkeydevpetroller.game.GDX_GLOBAL_isLoadAssets
import com.turkeypetmiss.turkeydevpetroller.game.actors.ALoader
import com.turkeypetmiss.turkeydevpetroller.game.manager.MusicManager
import com.turkeypetmiss.turkeydevpetroller.game.manager.SoundManager
import com.turkeypetmiss.turkeydevpetroller.game.manager.SpriteManager
import com.turkeypetmiss.turkeydevpetroller.game.utils.Block
import com.turkeypetmiss.turkeydevpetroller.game.utils.HEIGHT_UI
import com.turkeypetmiss.turkeydevpetroller.game.utils.TIME_ANIM_SCREEN
import com.turkeypetmiss.turkeydevpetroller.game.utils.WIDTH_UI
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.HAlign
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.VAlign
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.addActorAligned
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animHide
import com.turkeypetmiss.turkeydevpetroller.game.utils.actor.animShow
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame
import com.turkeypetmiss.turkeydevpetroller.game.utils.runGDX
import com.turkeypetmiss.turkeydevpetroller.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoaderScreen : AdvancedScreen() {

    private val progressFlow     = MutableStateFlow(0f)
    private var isFinishLoading  = false
    private var isFinishProgress = false

    private val aLoader by lazy { ALoader(this) }

    override fun show() {
        stageUI.root.color.a = 0f

        loadSplashAssets()
        setBackBackground(gdxGame.assetsLoader.BACKGROUND)
        super.show()

        animShow()

        loadAssets()
        collectProgress()
    }

    override fun render(delta: Float) {
        super.render(delta)
        loadingAssets()
        isFinish()
    }

    override fun animHide(blockEnd: Block) {
        stageUI.root.animHide(TIME_ANIM_SCREEN) { blockEnd() }
    }

    override fun animShow(blockEnd: Block) {
        stageUI.root.animShow(TIME_ANIM_SCREEN) { blockEnd() }
    }

    // Actors ------------------------------------------------------------------------

    override fun Group.addActorsOnStageUI() {
        //aMain.debug()
        aLoader.setSize(WIDTH_UI, HEIGHT_UI)
        addActorAligned(aLoader, HAlign.CENTER, VAlign.CENTER)
    }

    // Logic ------------------------------------------------------------------------

    private fun loadSplashAssets() {
        with(gdxGame.spriteManager) {
            loadableAtlasList = mutableListOf(SpriteManager.EnumAtlas.LOADER.data)
            loadAtlas()
            loadableTexturesList = mutableListOf(
                SpriteManager.EnumTexture.BACKGROUND.data,
                SpriteManager.EnumTexture.MASK.data,
            )
            loadTexture()
        }
        gdxGame.assetManager.finishLoading()
        gdxGame.spriteManager.initAtlasAndTexture()
    }

    private fun loadAssets() {
        with(gdxGame.spriteManager) {
            loadableAtlasList = SpriteManager.EnumAtlas.entries.map { it.data }.toMutableList()
            loadAtlas()
            loadableTexturesList = SpriteManager.EnumTexture.entries.map { it.data }.toMutableList()
            loadTexture()
        }
        with(gdxGame.musicManager) {
            loadableMusicList = MusicManager.EnumMusic.entries.map { it.data }.toMutableList()
            load()
        }
        with(gdxGame.soundManager) {
            loadableSoundList = SoundManager.EnumSound.entries.map { it.data }.toMutableList()
            load()
        }
    }

    private fun initAssets() {
        gdxGame.spriteManager.initAtlasAndTexture()
        gdxGame.musicManager.init()
        gdxGame.soundManager.init()
    }

    private fun loadingAssets() {
        if (isFinishLoading.not()) {
            if (gdxGame.assetManager.update(16)) {
                isFinishLoading = true
                initAssets()
            }
            progressFlow.value = gdxGame.assetManager.progress
        }
    }

    private fun collectProgress() {
        coroutine?.launch {
            var progress = 0
            progressFlow.collect { p ->
                while (progress < (p * 100)) {
                    progress += 1

                    runGDX { aLoader.setProgressPercent(progress.toFloat()) }

                    if (progress % 50 == 0) log("progress = $progress%")
                    if (progress == 100) isFinishProgress = true

                    delay((25..35).shuffled().first().toLong())
                }
            }
        }
    }

    private fun isFinish() {
        if (isFinishProgress && GDX_GLOBAL_isGame) {
            isFinishProgress = false

            toGame()
        }
    }

    private fun toGame() {
        GDX_GLOBAL_isLoadAssets = true
        gdxGame.activity.hideWebView()

        gdxGame.musicUtil.apply { currentMusic = WHITE.apply {
            isLooping = true
            coff      = 0.20f
        } }

        animHide { gdxGame.navigationManager.navigate(WelcomeScreen::class.java.name) }

    }


}