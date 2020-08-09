//package com.example.tweeter_v1
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//import java.io.File
//
//class asdf(
//
//    private val allFiles: Array<File>,
//    private val onPlayClick: onItemListClick
//
//) : RecyclerView.Adapter<AudioViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
//        val view: View = LayoutInflater.from(parent.context)
//            .inflate(R.layout.single_list_item, parent, false)
//        timeAgo = TimeAgo()
//        return AudioViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
//        holder.list_title.text = allFiles[position].name
//        holder.list_date.setText(timeAgo.getTimeAgo(allFiles[position].lastModified()))
//    }
//
//    override fun getItemCount(): Int {
//        return allFiles.size
//    }
//
//    inner class AudioViewHolder(itemView: View) :
//
//        RecyclerView.ViewHolder(itemView), View.OnClickListener {
//
//        private val list_image: ImageView
//        private val list_title: TextView
//        private val list_date: TextView
//
//        override fun onClick(v: View) {
//            onItemListClick.onClickListener(
//                allFiles[adapterPosition],
//                adapterPosition
//            )
//        }
//
//        init {
//            list_image = itemView.findViewById(R.id.list_image_view)
//            list_title = itemView.findViewById(R.id.list_title)
//            list_date = itemView.findViewById(R.id.list_date)
//            itemView.setOnClickListener(this)
//        }
//    }
//
//    interface onItemListClick {
//        fun onClickListener(file: File?, position: Int)
//    }
//
//}