package com.example.networking.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import com.example.networking.activities.MainActivity.Companion.API_REQUEST
import com.example.networking.adapter.PicturesAdapter
import com.example.networking.data.PictureBitmap
import com.example.networking.data.ServerResponse
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class LoadingContent : Service() {

    inner class LocalBinder : Binder() {
        fun getService(): LoadingContent = this@LoadingContent
    }

    private lateinit var jsonResponse: ServerResponse
    var pictures = mutableListOf<PictureBitmap>()
    private val binder = LocalBinder()
    var isLoading = false
    private lateinit var picturesDownloadAsyncTask: PicturesDownloadAsyncTask

    fun stickToData(picturesAdapter: PicturesAdapter) {
        if (!::jsonResponse.isInitialized) {
            FetchPicturesAsyncTask { response ->
                jsonResponse = Gson().fromJson(response, ServerResponse::class.java)
                val urls = jsonResponse.hits.map { it.webformatURL }
                picturesDownloadAsyncTask = PicturesDownloadAsyncTask(
                    pictures,
                    { this.isLoading = false },
                    picturesAdapter
                )
                isLoading = true
                picturesDownloadAsyncTask.execute(urls)
            }.execute()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun continueStickingToData(picturesAdapter: PicturesAdapter) {
        picturesDownloadAsyncTask.picturesAdapter = picturesAdapter
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    class PicturesDownloadAsyncTask(
        private var storage: MutableList<PictureBitmap>?,
        private val isLoadingNotifier: () -> Unit,
        var picturesAdapter: PicturesAdapter?
    ) : AsyncTask<List<String>, PictureBitmap, Unit>() {

        override fun doInBackground(vararg params: List<String>) {
            params[0].forEachIndexed { i, it ->
                publishProgress(
                    PictureBitmap(getBitmap(it), i)
                )
            }
        }

        override fun onProgressUpdate(vararg values: PictureBitmap?) {
            super.onProgressUpdate(*values)
            storage?.add(values[0]!!)
            picturesAdapter!!.notifyDataSetChanged()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            isLoadingNotifier()
            storage = null
            picturesAdapter = null
        }

        private fun getBitmap(src: String?): Bitmap? {
            return try {
                val url = URL(src)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    class FetchPicturesAsyncTask(private val callback: (String) -> Unit) :
        AsyncTask<String, Unit, Unit>() {
        override fun doInBackground(vararg params: String?) {
            val reply = URL(API_REQUEST).openConnection().run {
                connect()
                getInputStream().bufferedReader().readLines().joinToString("")
            }
            callback(reply)
        }
    }
}
