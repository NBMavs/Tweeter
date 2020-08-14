package com.example.tweeter_v1

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tweeter_v1.R.id
import java.io.File

class AudioListAdapter(private val allFiles: ArrayList<File>, private var onPlayClick1: onPlayClick, private var context: Context) :
    RecyclerView.Adapter<AudioListAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        //Initializes ui items on each card
        val list_play_btn: ImageView = itemView.findViewById(id.list_play_btn)
        val list_title: TextView = itemView.findViewById(id.list_title)
        val list_duration: TextView = itemView.findViewById(id.list_duration)
        val list_classify_btn: Button = itemView.findViewById(id.list_classify_btn)
        val list_delete_btn: ImageView = itemView.findViewById(id.list_delete_btn)


        init{

            //Click listener for play button
            list_play_btn.setOnClickListener(this)

            //Click listener for classify button
            list_classify_btn.setOnClickListener {View ->
                val position: Int = absoluteAdapterPosition

                val audioFilePath = allFiles[absoluteAdapterPosition].absolutePath
                val audioFile = allFiles[absoluteAdapterPosition].name
                Log.d("ABS_PATH", allFiles[absoluteAdapterPosition].absolutePath)
                Log.d("ABS_File", allFiles[absoluteAdapterPosition].name )



                if ( audioFilePath != null){

                    val intent = Intent( context, ClassificationActivity::class.java )
                    Log.d("audio_file_path", "Path is $audioFilePath")
                    intent.putExtra( "audio_file_path", audioFilePath )
                    intent.putExtra( "audio_file", allFiles[absoluteAdapterPosition].name )
                    context.startActivity( intent )

                }

            }

            //Click listener for delete button
            list_delete_btn.setOnClickListener {View ->
                val position: Int = absoluteAdapterPosition
                Toast.makeText(itemView.context,"You clicked on delete recording ${position + 1}", Toast.LENGTH_SHORT).show()
                onDeleteCLick(absoluteAdapterPosition)
            }
        }

        override fun onClick(p0: View?) {
            onPlayClick1.onClickListener(
                allFiles[absoluteAdapterPosition],
                absoluteAdapterPosition
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.audio_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        Log.d("ALL_FILES_SIZE", allFiles.size.toString())
        return allFiles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Sets name and audio clip duration
        holder.list_title.text = allFiles[position].name
        holder.list_duration.text = getDuration(allFiles[position]).toString()
    }

    // Returns duration of a sound file as a formatted string
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

    // Deletes a file in the arraylist of bird recordings
    private fun onDeleteCLick(position: Int) {
        val file: File = File(allFiles[position].absolutePath)
        file.delete()
        allFiles.removeAt(position)
        notifyItemRemoved(position)
    }

    //Interface for play button (it needs to work with the audio list page directly)
    public interface onPlayClick {
        fun onClickListener(file: File, position: Int)
    }

}