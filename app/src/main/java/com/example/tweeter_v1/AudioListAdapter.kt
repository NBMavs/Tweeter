
package com.example.tweeter_v1

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tweeter_v1.R
import com.example.tweeter_v1.R.id
import java.io.File
//import com.tensorflow.android.audio.features.WavFile

class AudioListAdapter(private val allFiles: ArrayList<File>, private var onPlayClick1: onPlayClick) :
    RecyclerView.Adapter<AudioListAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        val list_play_btn: ImageView = itemView.findViewById(id.list_play_btn)
        val list_title: TextView = itemView.findViewById(id.list_title)
        val list_duration: TextView = itemView.findViewById(id.list_duration)
        val list_classify_btn: Button = itemView.findViewById(id.list_classify_btn)
        val list_delete_btn: ImageView = itemView.findViewById(id.list_delete_btn)

        init{
            list_play_btn.setOnClickListener(this)

            //Click listener for classify button
            list_classify_btn.setOnClickListener {View ->
                val position: Int = absoluteAdapterPosition

                //Toast.makeText(itemView.context,"You clicked on classify recording ${allFiles[position].name}", Toast.LENGTH_SHORT).show()

                val audioFilePath = allFiles[absoluteAdapterPosition].absolutePath
                Log.d("ABS_PATH", allFiles[absoluteAdapterPosition].absolutePath)


                if ( audioFilePath != null){

                    Toast.makeText(itemView.context,"This bird is here, dude...", Toast.LENGTH_SHORT).show()
//                    val result = classifyNoise(audioFilePath)
//                    result_text.text = "Predicted Noise : $result"
                }
                else{
                    Toast.makeText(itemView.context,"Add some birds, dude...", Toast.LENGTH_SHORT).show()
                }
            }

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
        return allFiles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.list_title.text = allFiles[position].name
        holder.list_duration.text = getDuration(allFiles[position]).toString()
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

    private fun onDeleteCLick(position: Int) {
        val file: File = File(allFiles[position].absolutePath)
        file.delete()
        allFiles.removeAt(position)
        notifyItemRemoved(position)
    }

//    private fun classifySound(position: Int) {
//        try {
//            //wavFile = WavFile.openWavFile(allFiles[position])
//        }
//    }


    public interface onPlayClick {
        fun onClickListener(file: File, position: Int)
    }




}