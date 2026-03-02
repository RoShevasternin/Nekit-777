package com.turkeypetmiss.turkeydevpetroller.game.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class SpriteManager(var assetManager: AssetManager) {

    var loadableAtlasList   = mutableListOf<AtlasData>()
    var loadableTexturesList   = mutableListOf<TextureData>()

    fun loadAtlas() {
        loadableAtlasList.onEach { assetManager.load(it.path, TextureAtlas::class.java) }
    }

    fun initAtlas() {
        loadableAtlasList.onEach { it.atlas = assetManager[it.path, TextureAtlas::class.java] }
        loadableAtlasList.clear()
    }

    // Texture
    fun loadTexture() {
        loadableTexturesList.onEach { assetManager.load(it.path, Texture::class.java) }
    }

    fun initTexture() {
        loadableTexturesList.onEach { it.texture = assetManager[it.path, Texture::class.java] }
        loadableTexturesList.clear()
    }

    fun initAtlasAndTexture() {
        initAtlas()
        initTexture()
    }


    enum class EnumAtlas(val data: AtlasData) {
        LOADER  (AtlasData("atlas/loader.atlas")),
        ALL     (AtlasData("atlas/all.atlas")),
    }

    enum class EnumTexture(val data: TextureData) {
        // Loader
        BACKGROUND(TextureData("textures/loader/background.png")),
        MASK      (TextureData("textures/loader/mask.png")),

        // All
        BACKGROUND_GAME(TextureData("textures/all/background_game.png")),
        BACKGROUND_SHOP(TextureData("textures/all/background_shop.png")),

        T1 (TextureData("textures/all/t1.png")),
        T2 (TextureData("textures/all/t2.png")),
        T3 (TextureData("textures/all/t3.png")),

        BUTTONS (TextureData("textures/all/buttons.png")),

        _1 (TextureData("textures/all/levels/1.png")),
        _2 (TextureData("textures/all/levels/2.png")),
        _3 (TextureData("textures/all/levels/3.png")),
        _4 (TextureData("textures/all/levels/4.png")),
        _5 (TextureData("textures/all/levels/5.png")),
        _6 (TextureData("textures/all/levels/6.png")),
        _7 (TextureData("textures/all/levels/7.png")),
        _8 (TextureData("textures/all/levels/8.png")),
        _9 (TextureData("textures/all/levels/9.png")),
        _10 (TextureData("textures/all/levels/10.png")),

    }

    data class AtlasData(val path: String) {
        lateinit var atlas: TextureAtlas
    }

    data class TextureData(val path: String) {
        lateinit var texture: Texture
    }

}