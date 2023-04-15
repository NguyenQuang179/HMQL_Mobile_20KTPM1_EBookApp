package com.example.hmql_ebookapp

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class translate_api : AsyncTask<String, String, String>() {
    private var listener: OnTranslationCompleteListener? = null

    override fun doInBackground(vararg strings: String): String {
        val strArr = strings
        var str = ""
        try {
            val encode = URLEncoder.encode(strArr[0], "utf-8")
            val sb = StringBuilder()
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=")
            sb.append(strArr[1])
            sb.append("&tl=")
            sb.append(strArr[2])
            sb.append("&dt=t&q=")
            sb.append(encode)
            val url = URL(sb.toString())
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val statusCode = connection.responseCode
            if (statusCode == 200) {
                val inputStream = connection.inputStream
                val result = readStream(inputStream)
                val jsonArray = JSONArray(result).getJSONArray(0)
                var str2 = str
                for (i in 0 until jsonArray.length()) {
                    val jSONArray2 = jsonArray.getJSONArray(i)
                    val sb2 = StringBuilder()
                    sb2.append(str2)
                    sb2.append(jSONArray2.get(0).toString())
                    str2 = sb2.toString()
                }
                inputStream.close()
                return str2
            } else {
                throw IOException(connection.responseMessage)
            }
        } catch (e: Exception) {
            Log.e("translate_api", e.message, e)
            listener?.onError(e)
            return str
        }
    }

    private fun readStream(inputStream: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString("UTF-8")
    }

    override fun onPreExecute() {
        super.onPreExecute()
        listener?.onStartTranslation()
    }

    override fun onPostExecute(text: String) {
        listener?.onCompleted(text)
    }

    fun setOnTranslationCompleteListener(listener: OnTranslationCompleteListener) {
        this.listener = listener
    }

    interface OnTranslationCompleteListener {
        fun onStartTranslation()
        fun onCompleted(text: String)
        fun onError(e: Exception)
    }
}
