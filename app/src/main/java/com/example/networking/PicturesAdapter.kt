package com.example.networking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class PicturesAdapter(
    private val pictures: List<MainActivity.Pictures>,
    private val onClick: (MainActivity.Pictures) -> Unit
) : RecyclerView.Adapter<PicturesAdapter.PicturesViewHolder>() {

    override fun getItemCount() = pictures.size

    override fun onBindViewHolder(
        holder: PicturesViewHolder,
        position: Int
    ) = holder.bind(pictures[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesViewHolder {
        val holder = PicturesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        )
        holder.root.setOnClickListener {
            onClick(pictures[holder.adapterPosition])
        }
        return holder
    }

    class PicturesViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(picture : MainActivity.Pictures) {
            with(root) {
                pictureImage.setImageBitmap(picture.bitmap)
                pictureDescription.text = picture.description
            }
        }
    }
}