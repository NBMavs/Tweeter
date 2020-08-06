package com.example.tweeter_v1

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tweeter_v1.AudioListAdapter.AudioViewHolder
import java.io.File
import java.util.concurrent.TimeUnit


class AudioListAdapter(private val allFiles: Array<File>) :
    RecyclerView.Adapter<AudioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_list_item, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.list_title.text = allFiles[position].name
        holder.list_duration.text = getDuration(allFiles[position]).toString()
    }

    override fun getItemCount(): Int {
        return allFiles.size
    }

    inner class AudioViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val list_image: ImageView = itemView.findViewById(R.id.list_play_btn)
        val list_title: TextView = itemView.findViewById(R.id.list_title)
        val list_duration: TextView = itemView.findViewById(R.id.list_duration)
        val list_classify_btn: Button = itemView.findViewById(R.id.list_classify_btn)
        val list_delete_btn: ImageView = itemView.findViewById(R.id.list_delete_image)

    }

    private fun getDuration(file: File): String {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(file.absolutePath)
        val durationStr =
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeDuration = durationStr!!.toLong()
        val minutes = (timeDuration / (1000*60)) % 60
        val seconds = (timeDuration / (1000)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


}