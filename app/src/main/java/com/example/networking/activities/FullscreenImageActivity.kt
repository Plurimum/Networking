package com.example.networking.activities

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.networking.R
import com.example.networking.service.LoadingContent
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

class FullscreenImageActivity : AppCompatActivity() {

    private lateinit var  mService: LoadingContent
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LoadingContent.LocalBinder
            mService = binder.getService()
            mBound = true
            val pos = intent.getIntExtra(MainActivity.IMG_KEY, 0)
            fullscreenImage.setImageBitmap(mService.pictures[pos].bitmap)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)
        if (!mBound) {
            Intent(this, LoadingContent::class.java).also {
                intent -> bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }

}