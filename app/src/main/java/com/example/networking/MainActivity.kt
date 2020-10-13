package com.example.networking

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networking.MyIntentService.Companion.picturesList
import com.example.networking.MyIntentService.Companion.requestURL
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.net.URL


@Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
class MainActivity : AppCompatActivity() {

    private var resultReceiver: PicturesReceiver? = null

    @Parcelize
    data class Pictures(
        val bitmap: Bitmap,
        val description: String,
        val HighResolutionURL: String
    ) : Parcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultReceiver =
            savedInstanceState?.getParcelable("receiver") ?: PicturesReceiver(Handler()).apply {
                bind()
            }
        val viewManager = LinearLayoutManager(this)
        val myTask = MyTask()
        myRecyclerView.apply {
            if (picturesList.isEmpty()) {
                picturesList = myTask.execute().get()
            }
            layoutManager = viewManager
            adapter = PicturesAdapter(picturesList) {
                startService(
                    Intent(
                        this@MainActivity, MyIntentService::class.java
                    ).putExtra(
                        "HighResolutionURL", it.HighResolutionURL
                    ).putExtra("index of HighRes", picturesList.indexOf(it))
                )
            }
        }
    }

    class MyTask : AsyncTask<String, Unit, ArrayList<Pictures>>() {

        override fun doInBackground(vararg params: String?): ArrayList<Pictures> {
            val jsonResponse = URL(requestURL)
                .openConnection().run {
                    connect()
                    getInputStream().bufferedReader().readLines().joinToString("")
                }
            val request: Request = Gson().fromJson(jsonResponse, Request::class.java)
            val resultList: ArrayList<Pictures> = ArrayList()
            for (i in request.response.items.indices) {
                val bitmap: Bitmap
                val input: InputStream = URL(
                    request.response.items[i].sizes[0].url
                ).openStream()
                bitmap = BitmapFactory.decodeStream(input)
                val item = Pictures(
                    bitmap, request.response.items[i].text,
                    request.response.items[i].sizes[
                            request.response.items[i].sizes.size - 1].url
                )
                resultList.add(item)
            }
            return resultList
        }

        override fun onPostExecute(result: ArrayList<Pictures>?) {
            if (result != null) {
                Log.d("postExecute", "success")
            }
        }
    }

    override fun onResume() {
        resultReceiver = PicturesReceiver(Handler()).apply {
            bind()
        }
        super.onResume()
    }

    override fun onPause() {
        resultReceiver!!.unBind()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("receiver", resultReceiver)
        super.onSaveInstanceState(outState)
    }

    inner class PicturesReceiver(handler: Handler) : ResultReceiver(handler) {

        private var isBinded = false

        fun bind() {
            isBinded = true
        }

        fun unBind() {
            isBinded = false
        }

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (isBinded) {
                val images: ArrayList<Pictures> =
                    resultData!!.getParcelableArrayList("images")!!
                picturesList.addAll(images)
                super.onReceiveResult(resultCode, resultData)
            }
        }
    }
}