package com.example.networking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle/*
import com.example.networking.MainActivity.Companion.highResBitmap
import com.example.networking.MainActivity.Companion.picturesList*/
import com.example.networking.MyIntentService.Companion.highResBitmap
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

class FullscreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)
        //fullscreenImage.setImageBitmap(picturesList[intent.extras?.get("index") as Int].HighResolutionBitmap)
        fullscreenImage.setImageBitmap(highResBitmap)
    }
}