package com.example.tweeter_v1

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.user_menu.*
import kotlinx.android.synthetic.main.verify_class.*


class VerifyClassification :  AppCompatActivity() {

    data class VerBirds(
        val type: String = "",
        val wiki: String = "",
        var user: String = "",
        var time: String = "",
        var location: String = ""
    )

    val database = FirebaseDatabase.getInstance()
    var ref = database.getReference("VerifiedBirds")





    fun loadDatabase(firebaseData: DatabaseReference) {
        val availableBirds: List<VerBirds> = mutableListOf(
            VerBirds("Metallic Starling", "https://en.wikipedia.org/wiki/Metallic_starling"),
            VerBirds("European Robin", "https://en.wikipedia.org/wiki/European_robin"),
            VerBirds("Common Blackbird", "https://en.wikipedia.org/wiki/Common_blackbird"),
            VerBirds("Blue Jay", "https://en.wikipedia.org/wiki/Blue_jay"),
            VerBirds("Eurasian Magpie", "https://en.wikipedia.org/wiki/Eurasian_magpie")
        )
        availableBirds.forEach {
            val key = firebaseData.child("VerBirds").push().key
            it.user = key!!
            it.time = key!!
            it.location = key!!
            firebaseData.child("VerBirds").child(key).setValue(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verify_class)




        buttonWriteDB.setOnClickListener {
            this.loadDatabase(ref)
        }

        var CurLoc = "Lat, Long"

        BirdBookActivity.LocationHelper().startListeningUserLocation(
            this,
            object : BirdBookActivity.LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    // Here you got user location :)
                    CurLoc = location.latitude.toString() + "," + location.longitude.toString()
                    Log.d("Location", "" + location.latitude + "," + location.longitude)
                }
            })
    }
}

