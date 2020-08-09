
package layout

import android.media.MediaMetadataRetriever
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

class AudioListAdapter(private val allFiles: Array<File>) :
    RecyclerView.Adapter<AudioListAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val list_play: ImageView = itemView.findViewById(id.list_play_btn)
        val list_title: TextView = itemView.findViewById(id.list_title)
        val list_duration: TextView = itemView.findViewById(id.list_duration)
        val list_classify_btn: Button = itemView.findViewById(id.list_classify_btn)
        val list_delete_btn: ImageView = itemView.findViewById(id.list_delete_btn)

        init{
            itemView.setOnClickListener { View ->
                val position: Int = adapterPosition
                Toast.makeText(itemView.context,"You clicked on item # ${position + 1}", Toast.LENGTH_SHORT).show()
            }
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

}