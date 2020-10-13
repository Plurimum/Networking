package com.example.networking

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.net.URL

class MyIntentService : IntentService("MyIntentService") {

    companion object {
        var picturesList: ArrayList<MainActivity.Pictures> = arrayListOf()
        lateinit var highResBitmap: Bitmap
        var requestURL = "https://api.vk.com/method/photos.search?params[q]=kekw&v=5.124&access_token=2e2cbdcb2e2cbdcb2e2cbdcbe52e58f5f522e2c2e2cbdcb715003dfc98866760df041aa"
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("FullScreenPicture", "Loading")
        val currentURL = intent?.getStringExtra("HighResolutionURL")
        val bitmap = BitmapFactory.decodeStream(URL(currentURL).openStream())
        highResBitmap = bitmap
        startActivity(Intent(this, FullscreenImageActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
