package com.example.tweeter_v1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tweeter_v1.AudioListAdapter.AudioViewHolder
import java.io.File

class AudioListAdapter(private val allFiles: Array<File>) :
    RecyclerView.Adapter<AudioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_list_item, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.list_title.text = allFiles[position].name
        holder.list_date.text = allFiles[position].lastModified().toString() + ""
    }

    override fun getItemCount(): Int {
        return allFiles.size
    }

    inner class AudioViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val list_image: ImageView = itemView.findViewById(R.id.list_image_view)
        val list_title: TextView = itemView.findViewById(R.id.list_title)
        val list_date: TextView = itemView.findViewById(R.id.list_date)

    }

}