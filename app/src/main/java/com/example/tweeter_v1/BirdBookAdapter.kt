package com.example.tweeter_v1

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
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
import java.io.File
import java.security.AccessController.getContext

class BirdBookAdapter (private var birdsArrayFromDB: MutableList<VerifyClassification.DBWrite>, private val context: Context) :
    RecyclerView.Adapter<BirdBookAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val birdBookEntrySpecies: TextView = itemView.findViewById(R.id.bird_species)
        val birdBookEntryTimeDate: TextView? = itemView.findViewById(R.id.time_spotted)
        val birdBookEntryLocation: TextView? = itemView.findViewById(R.id.location_spotted)
        val birdBookEntryImage: ImageView? = itemView.findViewById(R.id.bird_image)

        val birdBookEntryTweetButton: Button = itemView.findViewById(R.id.tweet_btn)
        val birdBookEntryViewMapButton: Button = itemView.findViewById(R.id.view_map_btn)
        val birdBookEntryWikiButton: Button = itemView.findViewById(R.id.view_wiki_btn)

        init{


            //Click listener for classify button
            birdBookEntryTweetButton.setOnClickListener {View ->

            }

            birdBookEntryViewMapButton.setOnClickListener {View ->

            }

            birdBookEntryWikiButton.setOnClickListener {View ->

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.bird_book_list_row_layout, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        Log.d("ALL_FILES_SIZE", birdsArrayFromDB.size.toString())
        return birdsArrayFromDB.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.birdBookEntrySpecies!!.text = "Hello"
        holder.birdBookEntryTimeDate!!.text = "My"
        holder.birdBookEntryLocation!!.text = "Dood"
        holder.birdBookEntryImage!!.setImageDrawable(context.resources.getDrawable(getImageDrawable(
            birdsArrayFromDB[position].birdsType), null))

    }

    fun getImageDrawable(birdName: String) : Int {
        val birdNames = arrayListOf<String>(
            "blue jay",
            "cedar waxling",
            "great horned owl",
            "osprey",
            "tree swallow"
        )

        val birdsDrawables = arrayListOf<Int>(
            R.drawable.metallic_starling,
            R.drawable.blue_jay,
            R.drawable.common_blackbird,
            R.drawable.eurasion_magpie,
            R.drawable.european_robin
        )

        for(i in birdNames.indices){
            if(birdNames[i] == birdName){
                return birdsDrawables[i]
            }
        }
        return 0
    }
}