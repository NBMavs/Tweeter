package com.example.tweeter_v1

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.verify_class.*
import java.util.*



class VerifyClassification :  AppCompatActivity() {
    data class DBWrite(
    //@SerializedName("wiki") var wiki: String,
    @SerializedName("user") var user: String,
    @SerializedName("time") var time: String,
    @SerializedName("location") var location: String,
    @SerializedName("id") var id: String,
    @SerializedName("birdsType") var birdsType: String
){

        constructor(bird: DBWrite) : this("","","","","")  constructor(): this("","","","","")}


    val database = FirebaseDatabase.getInstance()
    val user = Firebase.auth.currentUser

    fun writeNewMS(
        firebaseData: DatabaseReference,
        CurLoc: String,
        birdsType: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("","","","","")
        )
        User.forEach {
            it.birdsType = birdsType
            it.user = user!!.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            it.id = user!!.uid
            firebaseData.child(user.uid).child(it.time).setValue(it)
            Log.d("DBWrite","$it")
        }
        Log.d("Write_Bird_Book","Metallic Starling added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Metallic Starling added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewER(
        firebaseData: DatabaseReference,
        CurLoc: String,
        birdsType: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("","","","","")
        )
        User.forEach {
            it.birdsType = birdsType
            it.user = user!!.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            it.id = user!!.uid
            firebaseData.child(user.uid).child(it.time).setValue(it)
        }
        Log.d("Write_Bird_Book","European Robin added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "European Robin added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewCBB(
        firebaseData: DatabaseReference,
        CurLoc: String,
        birdsType: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("","","","","")
        )
        User.forEach {
            it.birdsType = birdsType
            it.user = user!!.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            it.id = user!!.uid
            firebaseData.child(user.uid).child(it.time).setValue(it)
        }
        Log.d("Write_Bird_Book","Common Blackbird added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Common Blackbird added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewEM(
        firebaseData: DatabaseReference,
        CurLoc: String,
        birdsType: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("","","","","")
        )
        User.forEach {
            it.birdsType = birdsType
            it.user = user!!.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            it.id = user!!.uid
            firebaseData.child(user.uid).child(it.time).setValue(it)
        }
        Log.d("Write_Bird_Book","Eurasian Magpie added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Eurasian Magpie added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }

    fun writeNewBJ(
        firebaseData: DatabaseReference,
        CurLoc: String,
        birdsType: String
    ) {
        val currentTime = Calendar.getInstance().getTime()
        val User : List<DBWrite> = mutableListOf(
            DBWrite("","","","","")
        )
        User.forEach {
            it.birdsType = birdsType
            it.user = user!!.displayName.toString()
            it.time = currentTime.toString()
            it.location = CurLoc
            it.id = user!!.uid
            firebaseData.child(user.uid).child(it.time).setValue(it)
        }
        Log.d("Write_Bird_Book","Blue Jay added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "Blue Jay added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verify_class)

        var CurLoc = "Lat, Long"

        fun getCompleteAddressString(LATITUDE:Double, LONGITUDE:Double):String {
            var strAdd = ""
            var geocoder = Geocoder(this, Locale.getDefault())
            try
            {
                val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                if (addresses != null)
                {
                    val returnedAddress = addresses.get(0)
                    val strReturnedAddress = StringBuilder("")
                    for (i in 0..returnedAddress.getMaxAddressLineIndex())
                    {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    }
                    strAdd = strReturnedAddress.toString()
                    Log.d("Address", strReturnedAddress.toString())
                }
                else
                {
                    Log.w("Address","No Address returned!")
                }
            }
            catch (e:Exception) {
                e.printStackTrace()
                Log.w("Address","Cannot get Address!")
            }
            return strAdd
        }
        BirdBookActivity.LocationHelper().startListeningUserLocation(
            this,
            object : BirdBookActivity.LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    // Here you got user location :)
                    //CurLoc = location.latitude.toString() + "," + location.longitude.toString()
                    CurLoc = getCompleteAddressString(location.latitude, location.longitude)
                    Log.d("Location", "" + CurLoc)
                }
            })

        var birdsType = ""
        var birdType = ""



        buttonWriteDB.setOnClickListener {
            birdType = editTextWriteDB.text.toString()
            if(birdType.equals("Metallic Starling")){
                birdsType = "Metallic Starling"
                val ref = database.getReference("VerifiedBirds")
                this.writeNewMS(ref, CurLoc, birdsType)
            }
            else if(birdType.equals("European Robin")){
                birdsType = "European Robin"
                val ref = database.getReference("VerifiedBirds")
                this.writeNewER(ref, CurLoc, birdsType )
            }
            else if(birdType.equals("Common Blackbird")){
                birdsType = "Common Blackbird"
                val ref = database.getReference("VerifiedBirds")
                this.writeNewCBB(ref, CurLoc, birdsType)
            }
            else if(birdType.equals("Blue Jay")){
                birdsType = "Blue Jay"
                val ref = database.getReference("VerifiedBirds")
                this.writeNewBJ(ref, CurLoc, birdsType)
            }
            else if(birdType.equals("Eurasian Magpie")){
                birdsType = "Eurasian Magpie"
                val ref = database.getReference("VerifiedBirds")
                this.writeNewEM(ref, CurLoc, birdsType)
            }
            else{
                Log.d("Write_Bird_Book","Failed - Unknown Bird Type: $birdType")
                Toast.makeText(this, "Failed to write to bird book!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }



        }




    }
}

