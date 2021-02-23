package com.example.networking.activities

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.networking.R
import com.example.networking.adapter.PicturesAdapter
import com.example.networking.data.PictureBitmap
import com.example.networking.service.LoadingContent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var picturesAdapter: PicturesAdapter
    private lateinit var mService: LoadingContent
    private var mBound: Boolean = false

    companion object {
        const val ACCESS_NETWORK_STATE_PERMISSION_REQUEST_ID = 111
        const val INTERNET_PERMISSION_REQUEST_ID = 101
        const val IMG_KEY = "IMAGE"
        const val API_KEY = "20043610-c75bd1aa1dd8579aa72763fca"
        const val API_REQUEST = "https://pixabay.com/api/?key=$API_KEY&q=sea&image_type=photo&per_page=100"
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LoadingContent.LocalBinder
            mService = binder.getService()
            mBound = true
            initiateRecyclerView(mService.pictures)
            if (mService.pictures.isEmpty()) {
                mService.stickToData(picturesAdapter)
            } else if (mService.isLoading) {
                mService.continueStickingToData(picturesAdapter)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPermissionAccessNetworkState()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_NETWORK_STATE),
                ACCESS_NETWORK_STATE_PERMISSION_REQUEST_ID
            )
        }
        if (!checkPermissionInternet()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET),
                INTERNET_PERMISSION_REQUEST_ID
            )
        }
        if (!mBound) {
            Intent(this, LoadingContent::class.java).also { intent ->
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }


    private fun initiateRecyclerView(curList: List<PictureBitmap>) {
        if (!::picturesAdapter.isInitialized) {
            picturesAdapter = PicturesAdapter(curList, this)
        }
        val gridLayoutManager = GridLayoutManager(
            this,
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 5 else 3
        )
        myRecyclerView.layoutManager = gridLayoutManager
        myRecyclerView.adapter = picturesAdapter
    }

    private fun checkPermissionInternet(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionAccessNetworkState(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        ) != PackageManager.PERMISSION_GRANTED
    }
}