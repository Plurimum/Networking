package com.example.networking

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log/*
import com.example.networking.MainActivity.Companion.highResBitmap
import com.example.networking.MainActivity.Companion.picturesList*/
import java.net.URL

class MyIntentService : IntentService("MyIntentService") {

    companion object {
        var picturesList: ArrayList<MainActivity.Pictures> = arrayListOf()
        lateinit var highResBitmap: Bitmap
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("Service thread", "Successfully started")
        val currentURL = intent?.getStringExtra("HighResolutionURL")
        val bitmap = BitmapFactory.decodeStream(URL(currentURL).openStream())
        val index = intent?.extras?.get("index of HighRes") as Int
        highResBitmap = bitmap
        //picturesList[index].HighResolutionBitmap = bitmap
        startActivity(Intent(this, FullscreenImageActivity::class.java).putExtra(
            "index", index
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
