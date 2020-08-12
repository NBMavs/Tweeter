package com.example.tweeter_v1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BirdBookFragment : Fragment() {

    var birdsArrayFromDB : MutableList<VerifyClassification.DBWrite>? = null

    private var userBirdBookRecyclerView: RecyclerView? = null

    private var birdBookEntryTweetButton: Button? = null
    private var birdBookEntryViewMapButton: Button? = null
    private var birdBookEntryWikiButton: Button? = null

    private var birdBookEntrySpecies: TextView? = null
    private var birdBookEntryTimeDate: TextView? = null
    private var birdBookEntryLocation: TextView? = null
    private var birdBookEntryImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bird_book, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        birdsArrayFromDB = null

        userBirdBookRecyclerView = view.findViewById<RecyclerView>(R.id.audio_list_view)

        userBirdBookRecyclerView!!.setHasFixedSize(true)
        userBirdBookRecyclerView!!.layoutManager = LinearLayoutManager(context)
        userBirdBookRecyclerView!!.adapter =
            birdsArrayFromDB?.let { BirdBookAdapter(it, requireContext()) }


    }
}