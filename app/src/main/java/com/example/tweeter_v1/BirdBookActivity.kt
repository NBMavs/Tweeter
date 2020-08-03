package com.example.tweeter_v1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.bird_book.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class BirdBookActivity: AppCompatActivity() {

    fun shareTwitter(message: Editable, CurLoc: String) {
            val tweetIntent = Intent(Intent.ACTION_SEND)
            val c = message.toString() + " " +  CurLoc
            tweetIntent.putExtra(Intent.EXTRA_TEXT, c)
            tweetIntent.setType("text/plain")
            val packManager = getPackageManager()
            val resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY)
            var resolved = false
            for (resolveInfo in resolvedInfoList)
            {
                if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android"))
                {
                    tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name)
                    resolved = true
                    break
                }
            }
            if (resolved)
            {
                startActivity(tweetIntent)
            }
            else
            {
                val i = Intent()
                i.putExtra(Intent.EXTRA_TEXT, message)
                i.setAction(Intent.ACTION_VIEW)
                i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message.toString())))
                startActivity(i)
                Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show()
            }
        }
        private fun urlEncode(s:String):String {
            try
            {
                return URLEncoder.encode(s, "UTF-8")
            }
            catch (e: UnsupportedEncodingException) {
                Log.d("Failure","UTF-8 should always be supported", e)
                return ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bird_book)

        var CurLoc = "Lat, Long"

        LocationHelper().startListeningUserLocation(this , object : LocationHelper.MyLocationListener {
            override fun onLocationChanged(location: Location) {
                // Here you got user location :)
                CurLoc = location.latitude.toString() + "," + location.longitude.toString()
                Log.d("Location","" + location.latitude + "," + location.longitude)
            }
        })

        val message = editTextTweet.text

        val buttonIntentTweet: Button = findViewById(R.id.buttonIntentTweet)
        buttonIntentTweet.setOnClickListener {
            shareTwitter(message, CurLoc)
        }

        suspend fun getUserProfile() {
            val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }

            //Twitter Id
            val twitterId = usr.id.toString()
            Log.d("Twitter Id: ", twitterId)

            //Twitter Handle
            val twitterHandle = usr.screenName
            Log.d("Twitter Handle: ", twitterHandle)

            //Twitter Name
            val twitterName = usr.name
            Log.d("Twitter Name: ", twitterName)

            //Twitter Email
            val twitterEmail = usr.email

            // Twitter Access Token
            Log.d("Twitter Access Token", accToken?.token ?: "")
            var accessToken = accToken?.token ?: ""

            // Save the Access Token (accToken.token) and Access Token Secret (accToken.tokenSecret) using SharedPreferences
            // This will allow us to check user's logging state every time they open the app after cold start.
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            sharedPref.edit().putString("oauth_token", accToken?.token ?: "").apply()
            sharedPref.edit().putString("oauth_token_secret", accToken?.tokenSecret ?: "").apply()
        }
    }

    class LocationHelper {

        val LOCATION_REFRESH_TIME = 3000 // 3 seconds. The Minimum Time to get location update
        val LOCATION_REFRESH_DISTANCE = 30 // 30 meters. The Minimum Distance to be changed to get location update
        val MY_PERMISSIONS_REQUEST_LOCATION = 100

        var myLocationListener: MyLocationListener? = null

        interface MyLocationListener {
            fun onLocationChanged(location: Location)
        }

        fun startListeningUserLocation(context: Context, myListener: MyLocationListener) {
            myLocationListener = myListener

            val mLocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

            val mLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    //your code here
                    myLocationListener!!.onLocationChanged(location) // calling listener to inform that updated location is available
                }
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
// check for permissions
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(),LOCATION_REFRESH_DISTANCE.toFloat(), mLocationListener)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // permission is denied by user, you can show your alert dialog here to send user to App settings to enable permission
                } else {
                    ActivityCompat.requestPermissions(context,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),MY_PERMISSIONS_REQUEST_LOCATION)
                }
            }
        }
    }
}