package com.example.networking.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.activities.FullscreenImageActivity
import com.example.networking.activities.MainActivity
import com.example.networking.R
import com.example.networking.data.PictureBitmap

class PicturesAdapter(
    private var picturesList: List<PictureBitmap>,
    private val context: Context
) : RecyclerView.Adapter<PicturesAdapter.PicturesViewHolder>() {

    override fun getItemCount() = picturesList.size

    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        holder.setData(picturesList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesViewHolder {
        return PicturesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    inner class PicturesViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.pictureImage)
        fun setData(pictureBitmap: PictureBitmap) {
            imageView.setImageBitmap(pictureBitmap.bitmap)
            view.setOnClickListener{
                val intent = Intent(context, FullscreenImageActivity::class.java)
                intent.putExtra(MainActivity.IMG_KEY, pictureBitmap.index)
                context.startActivity(intent)
            }
        }
    }
}