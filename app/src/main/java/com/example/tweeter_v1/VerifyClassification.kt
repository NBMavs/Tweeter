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
    @SerializedName("user") var user: String,
    @SerializedName("time") var time: String,
    @SerializedName("location") var location: String,
    @SerializedName("id") var id: String,
    @SerializedName("birdsType") var birdsType: String
){
        constructor(bird: DBWrite) : this("","","","","")  constructor(): this("","","","","")}

    val database = FirebaseDatabase.getInstance()
    val user = Firebase.auth.currentUser

    fun writeNewBird(
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
        Log.d("Write_Bird_Book","$birdsType added to bird book at $currentTime from $CurLoc")
        Toast.makeText(this, "$birdsType added to bird book at $currentTime from $CurLoc", Toast.LENGTH_LONG).show()
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
                birdsType = editTextWriteDB.text.toString()
                val ref = database.getReference("VerifiedBirds")
                this.writeNewBird(ref, CurLoc, birdsType)
            }



        }




    }

