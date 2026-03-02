package com.turkeypetmiss.turkeydevpetroller

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.turkeypetmiss.turkeydevpetroller.databinding.ActivityMainBinding
import com.turkeypetmiss.turkeydevpetroller.game.utils.gdxGame
import com.turkeypetmiss.turkeydevpetroller.util.OneTime
import com.turkeypetmiss.turkeydevpetroller.util.WebViewHelper
import com.turkeypetmiss.turkeydevpetroller.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {

    companion object {
        var statusBarHeight = 0
        var navBarHeight    = 0
    }

    private val coroutine  = CoroutineScope(Dispatchers.Default)
    private val onceExit   = OneTime()

    private val onceSystemBarHeight = OneTime()

    lateinit var binding : ActivityMainBinding

    val windowInsetsController by lazy { WindowCompat.getInsetsController(window, window.decorView) }

    lateinit var webViewHelper: WebViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            onceSystemBarHeight.use {
                statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

                log("statusBarHeight = $statusBarHeight | navBarHeight = $navBarHeight")

                // hide Status or Nav bar (після встановлення їх розмірів)
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            if (binding.webView.isVisible) {
                val imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val navBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                val totalBottom = maxOf(imeBottom, navBottom)

                binding.root.setPadding(0, statusBarHeight, 0, totalBottom)
                log("ime = $imeBottom | navBar = $navBarHeight | total = $totalBottom")
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    override fun exit() {
        onceExit.use {
            log("exit")
            coroutine.launch(Dispatchers.Main) {
                finishAndRemoveTask()
                delay(100)
                exitProcess(0)
            }
        }
    }

    private fun initialize() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webViewHelper = WebViewHelper(this)
    }

    // WebView -----------------------------------------------------------------------

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        log("MainActivity: onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        if (binding.webView.isVisible) {
            binding.webView.restoreState(savedInstanceState)
        }
    }

    override fun onResume() {
        log("MainActivity: onResume")
        super.onResume()
        if (binding.webView.isVisible) {
            CookieManager.getInstance().flush()
            binding.webView.onResume()
        }
    }

    override fun onPause() {
        log("MainActivity: onPause")
        super.onPause()
        if (binding.webView.isVisible) {
            CookieManager.getInstance().flush()
            binding.webView.onPause()
        }
    }

    // Logic -----------------------------------------------------------------------------------------

    @SuppressLint("SourceLockedOrientationActivity")
    fun hideWebView() {
        runOnUiThread {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            binding.webView.loadUrl("about:blank")
            binding.webView.visibility = View.GONE

            binding.root.setPadding(0, 0, 0, 0)

            binding.navHostFragment.requestFocus()
            gdxGame.resume()
        }
    }

    fun showWebView() {
        runOnUiThread {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER

            binding.root.setPadding(0, statusBarHeight, 0, 0)
            binding.webView.visibility = View.VISIBLE

            binding.webView.requestFocus()
            gdxGame.pause()
        }
    }

    fun getGistURL() = resources.getString(R.string.gist)

}