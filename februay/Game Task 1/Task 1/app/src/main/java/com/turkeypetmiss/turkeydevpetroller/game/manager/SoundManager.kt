package com.turkeypetmiss.turkeydevpetroller.game.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound

class SoundManager(var assetManager: AssetManager) {

    var loadableSoundList = mutableListOf<SoundData>()

    fun load() {
        loadableSoundList.onEach { assetManager.load(it.path, Sound::class.java) }
    }

    fun init() {
        loadableSoundList.onEach { it.sound = assetManager[it.path, Sound::class.java] }
        loadableSoundList.clear()
    }

    enum class EnumSound(val data: SoundData) {
        click        (SoundData("sound/click.mp3")),
        buy_coin     (SoundData("sound/buy_coin.mp3")),
        fail_buy_coin(SoundData("sound/fail_buy_coin.mp3")),
        touch_oil    (SoundData("sound/touch_oil.mp3")),
    }

    data class SoundData(
        val path: String,
    ) {
        lateinit var sound: Sound
    }

}