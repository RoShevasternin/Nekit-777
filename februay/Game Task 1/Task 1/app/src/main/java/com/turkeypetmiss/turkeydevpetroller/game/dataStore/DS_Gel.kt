package com.turkeypetmiss.turkeydevpetroller.game.dataStore

import com.turkeypetmiss.turkeydevpetroller.game.manager.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class DS_Gel(override val coroutine: CoroutineScope): DataStoreUtil<Int>(){

    override val dataStore = DataStoreManager.Gel

    override val flow = MutableStateFlow(0)

    init { initialize() }

}