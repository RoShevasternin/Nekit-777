package com.turkeypetmiss.turkeydevpetroller.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

val Any.currentClassName: String get() = this::class.java.simpleName

fun log(message: String) {
    Log.i("TPERSN", message)
}

fun cancelCoroutinesAll(vararg coroutine: CoroutineScope?) {
    coroutine.forEach { it?.cancel() }
}