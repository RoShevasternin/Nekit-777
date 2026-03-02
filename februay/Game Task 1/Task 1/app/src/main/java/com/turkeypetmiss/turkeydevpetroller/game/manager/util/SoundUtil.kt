package com.turkeypetmiss.turkeydevpetroller.game.manager.util

import com.badlogic.gdx.audio.Sound
import com.turkeypetmiss.turkeydevpetroller.game.manager.AudioManager
import com.turkeypetmiss.turkeydevpetroller.game.utils.runGDX
import com.turkeypetmiss.turkeydevpetroller.game.manager.SoundManager
import kotlin.compareTo
import kotlin.div
import kotlin.times

class SoundUtil {

    val buy_coin      = AdvancedSound(SoundManager.EnumSound.buy_coin.data.sound, 1f)
    val click         = AdvancedSound(SoundManager.EnumSound.click.data.sound, 1f)
    val fail_buy_coin = AdvancedSound(SoundManager.EnumSound.fail_buy_coin.data.sound, 1f)
    val touch_oil     = AdvancedSound(SoundManager.EnumSound.touch_oil.data.sound, 0.12f)

    // 0..100
    var volumeLevel = AudioManager.volumeLevelPercent

    var isPause = (volumeLevel <= 0f)

    fun play(advancedSound: AdvancedSound, playCoff: Float = 1f) {
        if (isPause.not()) {
            advancedSound.apply {
                sound.play(((volumeLevel / 100f) * coff) * playCoff)
            }
        }
    }

    data class AdvancedSound(val sound: Sound, val coff: Float)
}