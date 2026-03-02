package com.turkeypetmiss.turkeydevpetroller.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.toString
import kotlin.io.readBytes

object Gist {

    suspend fun getDataJson(urlGIST: String): DataJSON? = withContext(Dispatchers.IO) {
        try {
            val url = URL(urlGIST)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000  // 5 секунд таймаут на з'єднання
            connection.readTimeout = 5000  // 5 секунд таймаут на читання
            connection.doInput = true

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream: InputStream = connection.inputStream
                val response =
                    inputStream.readBytes().toString(Charsets.UTF_8) // Читаємо відповідь у String
                inputStream.close()

                val json = JSONObject(response)

                DataJSON(
                    link = json.optString("link", ""),
                    flag = json.optString("flag", ""),
                    key  = json.optString("key", ""),
                )
            } else {
                log("Gist = HTTP Error: ${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            log("Gist = Exception: ${e.message}")
            null
        }
    }

    data class DataJSON(
        val link : String,
        val flag : String,
        val key  : String,
    )

}