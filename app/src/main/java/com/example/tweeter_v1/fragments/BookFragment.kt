package com.example.tweeter_v1

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

var birdsArrayFromDB : MutableList<VerifyClassification.DBWrite> =  mutableListOf(VerifyClassification.DBWrite("","","","",""))
var dbReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("VerifiedBirds")


class BookFragment : Fragment() {

    var birdsArrayFromDB = loadDB()

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
        Log.d("Birds Array Start",birdsArrayFromDB.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("onViewCreated Array 2", birdsArrayFromDB.toString())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val birdsLocal = arrayListOf<Bird>(
            Bird("Metallic Starling", R.drawable.metallic_starling, "unknown"),
            Bird("Blue Jay", R.drawable.blue_jay, "unknown"),
            Bird("Common Blackbird", R.drawable.common_blackbird, "unknown"),
            Bird("Eurasion Magpie", R.drawable.eurasion_magpie, "unknown"),
            Bird("European Robin", R.drawable.european_robin, "unknown")
        )


        userBirdBookRecyclerView = view.findViewById<RecyclerView>(R.id.birdbook_list_view)

        userBirdBookRecyclerView!!.setHasFixedSize(true)
        userBirdBookRecyclerView!!.layoutManager = LinearLayoutManager(context)
        userBirdBookRecyclerView!!.adapter = BirdBookAdapter(birdsArrayFromDB, requireContext())
        //userBirdBookRecyclerView!!.adapter = BirdBookAdapter(birdsLocal!!, requireContext())

    }
}

fun loadDB(): MutableList<VerifyClassification.DBWrite> {
    birdsArrayFromDB.clear()
    val user = Firebase.auth.currentUser
    dbReference.child(user!!.uid).addValueEventListener(object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Log.d("ValueEventListener", "Backed out of Bird Book")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            tweet = "Click here to display bird book!"
            Log.d("Snapshot UID", user.uid)
            snapshot.children.forEach {
                if(snapshot.key!! == user.uid)
                {
                    val user5 = it.getValue(VerifyClassification.DBWrite::class.java)
                    birdsArrayFromDB.add(user5!!)

                    tweet = "I just verified a " + user5.birdsType + " at " + user5.location + " on " + user5.time + " on the Tweeter app!"
                }
            }
            val size = birdsArrayFromDB.size
            Log.d("birds array completed","Total birds: $size")
        }
    })
    return birdsArrayFromDB
}

/*private fun makeCurrentFragment( fragment: Fragment) =
    supportFragmentManager.beginTransaction().apply{
        TextUtils.replace(R.id.mapview, fragment)
        commit()
    }*/