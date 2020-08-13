package com.example.tweeter_v1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        LocationHelper().startListeningUserLocation(
            this,
            object : LocationHelper.MyLocationListener {
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



    class LocationHelper {

        val LOCATION_REFRESH_TIME =
            3000 // 3 seconds. The Minimum Time to get location update
        val LOCATION_REFRESH_DISTANCE =
            30 // 30 meters. The Minimum Distance to be changed to get location update
        val MY_PERMISSIONS_REQUEST_LOCATION = 100

        var myLocationListener: MyLocationListener? = null

        interface MyLocationListener {
            fun onLocationChanged(location: Location)
        }

        fun startListeningUserLocation(context: Context, myListener: MyLocationListener) {
            myLocationListener = myListener

            val mLocationManager =
                context.getSystemService(LOCATION_SERVICE) as LocationManager

            val mLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    //your code here
                    myLocationListener!!.onLocationChanged(location) // calling listener to inform that updated location is available
                }

                override fun onStatusChanged(
                    provider: String,
                    status: Int,
                    extras: Bundle
                ) {
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
// check for permissions
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME.toLong(),
                    LOCATION_REFRESH_DISTANCE.toFloat(),
                    mLocationListener
                )
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    // permission is denied by user, you can show your alert dialog here to send user to App settings to enable permission
                } else {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
            }
        }
    }
}

