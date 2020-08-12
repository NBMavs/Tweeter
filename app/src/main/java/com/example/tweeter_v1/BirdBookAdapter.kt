package com.example.tweeter_v1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

val birdsLocal = arrayListOf<Bird>(
    Bird("Metallic Starling", R.drawable.metallic_starling, "https://en.wikipedia.org/wiki/Metallic_starling"),
    Bird("Blue Jay", R.drawable.blue_jay, "https://en.wikipedia.org/wiki/Blue_jay"),
    Bird("Common Blackbird", R.drawable.common_blackbird, "https://en.wikipedia.org/wiki/Common_blackbird"),
    Bird("Eurasion Magpie", R.drawable.eurasion_magpie, "https://en.wikipedia.org/wiki/Eurasian_magpie"),
    Bird("European Robin", R.drawable.european_robin, "https://en.wikipedia.org/wiki/European_robin")
)

class BirdBookAdapter (private var birdsArrayFromDB: MutableList<VerifyClassification.DBWrite>, private val context: Context) :
//class BirdBookAdapter(private var birdsArrayFromDB: ArrayList<Bird>, private val context: Context) :
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
            birdBookEntryTweetButton.setOnClickListener { View ->
                Toast.makeText(context, "Clicked TWEET", Toast.LENGTH_SHORT).show()

            }

            birdBookEntryViewMapButton.setOnClickListener {View ->

            }

            birdBookEntryWikiButton.setOnClickListener {View ->
                val wiki : String = getWiki(birdsArrayFromDB[position].birdsType)
                    val uriUrl = Uri.parse(wiki)
                    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                    context.startActivity(launchBrowser)
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
        Log.d("birdArray",birdsArrayFromDB.toString())
        holder.birdBookEntrySpecies!!.text = birdsArrayFromDB[position].birdsType
        holder.birdBookEntryTimeDate!!.text = birdsArrayFromDB[position].time
        holder.birdBookEntryLocation!!.text = birdsArrayFromDB[position].location
        holder.birdBookEntryImage!!.setImageDrawable(context.resources.getDrawable(getImageDrawable(
            birdsArrayFromDB[position].birdsType), null))

/*        holder.birdBookEntrySpecies!!.text = birdsArrayFromDB[position].name
        holder.birdBookEntryTimeDate!!.text = birdsArrayFromDB[position].name
        holder.birdBookEntryLocation!!.text = birdsArrayFromDB[position].name
        holder.birdBookEntryImage!!.setImageDrawable(context.resources.getDrawable(birdsArrayFromDB[position].image
        , null))*/

    }

    private fun getImageDrawable(birdName: String) : Int {
        for(bird in birdsLocal){
            if(bird.name == birdName){
                return bird.image
            }
        }
        return 0
    }

    private fun getWiki(birdName: String) : String {
        for(bird in birdsLocal){
            if(bird.name == birdName){
                return bird.wiki
            }
        }
        return "https://en.wikipedia.org/wiki/Bird"
    }

}
