package com.example.tweeter_v1

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.verify_class.*
import java.util.*


class VerifyClassification :  AppCompatActivity() {
    val user = Firebase.auth.currentUser
    data class DBWrite(
        val wiki: String = "",
        var user: String = "",
        var time: String = "",
        var location: String = "",
        var id: String = ""
    )
    val database = FirebaseDatabase.getInstance()

    fun writeNewMS(
        firebaseData: DatabaseReference,
        CurLoc: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("https://en.wikipedia.org/wiki/Metallic_starling", user!!.uid)
        )
        User.forEach {
            val key = firebaseData.child("MetallicStarling").push().key
            it.user = user.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            firebaseData.child(user.uid).child(key.toString()).setValue(it)
        }
        Log.d("Write_Bird_Book","Metallic Starling added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Metallic Starling added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewER(
        firebaseData: DatabaseReference,
        CurLoc: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("https://en.wikipedia.org/wiki/European_robin", user!!.uid)
        )
        User.forEach {
            val key = firebaseData.child("EuropeanRobin").push().key
            it.user = user.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            firebaseData.child(user.uid).child(key.toString()).setValue(it)
        }
        Log.d("Write_Bird_Book","European Robin added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "European Robin added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewCBB(
        firebaseData: DatabaseReference,
        CurLoc: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("https://en.wikipedia.org/wiki/Common_blackbird", user!!.uid)
        )
        User.forEach {
            val key = firebaseData.child("CommonBlackbird").push().key
            it.user = user.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            firebaseData.child(user.uid).child(key.toString()).setValue(it)
        }
        Log.d("Write_Bird_Book","Common Blackbird added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Common Blackbird added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewEM(
        firebaseData: DatabaseReference,
        CurLoc: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("https://en.wikipedia.org/wiki/Eurasian_magpie", user!!.uid)
        )
        User.forEach {
            val key = firebaseData.child("EurasianMagpie").push().key
            it.user = user.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            firebaseData.child(user.uid).child(key.toString()).setValue(it)
        }
        Log.d("Write_Bird_Book","Eurasian Magpie added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Eurasian Magpie added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewBJ(
        firebaseData: DatabaseReference,
        CurLoc: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("https://en.wikipedia.org/wiki/Blue_jay")
        )
        User.forEach {
            val key = firebaseData.child("BlueJay").push().key
            it.user = user!!.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            it.id = user.uid
            firebaseData.child(user.uid).child(key.toString()).setValue(it)
        }
        Log.d("Write_Bird_Book","Blue Jay added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Blue Jay added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verify_class)

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

        var BirdType = ""

        buttonWriteDB.setOnClickListener {
            BirdType = editTextWriteDB.text.toString()
            if(BirdType.equals("Metallic Starling")){
                val ref = database.getReference("VerifiedBirds/Metallic Starling")
                this.writeNewMS(ref, CurLoc)
            }
            else if(BirdType.equals("European Robin")){
                val ref = database.getReference("VerifiedBirds/European Robin")
                this.writeNewER(ref, CurLoc)
            }
            else if(BirdType.equals("Common Blackbird")){
                val ref = database.getReference("VerifiedBirds/Common Blackbird")
                this.writeNewCBB(ref, CurLoc)
            }
            else if(BirdType.equals("Blue Jay")){
                val ref = database.getReference("VerifiedBirds/Blue Jay")
                this.writeNewBJ(ref, CurLoc)
            }
            else if(BirdType.equals("Eurasian Magpie")){
                val ref = database.getReference("VerifiedBirds/Eurasian Magpie")
                this.writeNewEM(ref, CurLoc)
            }
            else{
                Log.d("Write_Bird_Book","Failed - Unknown Bird Type: $BirdType")
                Toast.makeText(this, "Failed to write to bird book!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }



        }




    }
}

