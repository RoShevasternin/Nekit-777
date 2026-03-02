package com.turkeypetmiss.turkeydevpetroller.game.dataStore

import com.turkeypetmiss.turkeydevpetroller.game.manager.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class DS_PokupkiData(override val coroutine: CoroutineScope): DataStoreJsonUtil<List<Int>>(
    serializer   = ListSerializer(Int.serializer()),
    deserializer = ListSerializer(Int.serializer()),
) {

    override val dataStore = DataStoreManager.DataPokupki

    override val flow = MutableStateFlow(List(6) { 0 })

    init { initialize() }

}