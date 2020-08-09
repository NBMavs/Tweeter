package com.example.tweeter_v1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.bird_book.*
import kotlinx.android.synthetic.main.bird_book_list_row_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

var dbReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("VerifiedBirds")
val user2 = Firebase.auth.currentUser

lateinit var birds: MutableList<VerifyClassification.DBWrite>

class BirdBookActivity: AppCompatActivity() {

    fun loadDB(){
        // Code to read from database!
        dbReference.child(user2!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("ValueEventListener", "Failed")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if(snapshot.key!!.equals(user2.uid))
                    {
                        val user5 = it.getValue(VerifyClassification.DBWrite::class.java)
                        Log.d("test",it.toString())


                        textViewClassName.setText(user5?.birdsType).toString()
                        textViewLocation.setText(user5?.location).toString()
                        textViewDate.setText(user5?.time).toString()
                        if (user5!!.birdsType.equals("Blue Jay")) {
                            buttonBirdWiki.setOnClickListener {
                                val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Blue_jay")
                                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                                startActivity(launchBrowser)
                            }
                        }
                        else if (user5!!.birdsType.equals("Metallic Starling")) {
                            buttonBirdWiki.setOnClickListener {
                                val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Metallic_starling")
                                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                                startActivity(launchBrowser)
                            }
                        }
                        else if (user5!!.birdsType.equals("European Robin")) {
                            buttonBirdWiki.setOnClickListener {
                                val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/European_robin")
                                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                                startActivity(launchBrowser)
                            }
                        }
                        else if (user5!!.birdsType.equals("Common Blackbird")) {
                            buttonBirdWiki.setOnClickListener {
                                val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Common_blackbird")
                                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                                startActivity(launchBrowser)
                            }
                        }
                        else if (user5!!.birdsType.equals("Eurasian Magpie")) {
                            buttonBirdWiki.setOnClickListener {
                                val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Eurasian_magpie")
                                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                                startActivity(launchBrowser)
                            }
                        }
                    }
                }
            }
        })
    }


    fun shareTwitter(message: Editable, CurLoc: String) {
        val tweetIntent = Intent(Intent.ACTION_SEND)
        val c = message.toString() + " " + CurLoc
        tweetIntent.putExtra(Intent.EXTRA_TEXT, c)
        tweetIntent.setType("text/plain")
        val packManager = getPackageManager()
        val resolvedInfoList =
            packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY)
        var resolved = false
        for (resolveInfo in resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.name
                )
                resolved = true
                break
            }
        }
        if (resolved) {
            startActivity(tweetIntent)
        } else {
            val i = Intent()
            i.putExtra(Intent.EXTRA_TEXT, message)
            i.setAction(Intent.ACTION_VIEW)
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message.toString())))
            startActivity(i)
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show()
        }
    }

    private fun urlEncode(s: String): String {
        try {
            return URLEncoder.encode(s, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            Log.d("Failure", "UTF-8 should always be supported", e)
            return ""
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bird_book)

        loadDB()

                var CurLoc = "Lat, Long"

                fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
                    var strAdd = ""
                    var geocoder = Geocoder(this, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
                        if (addresses != null) {
                            val returnedAddress = addresses.get(0)
                            val strReturnedAddress = StringBuilder("")
                            for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                                strReturnedAddress.append(returnedAddress.getAddressLine(i))
                                    .append("\n")
                            }
                            strAdd = strReturnedAddress.toString()
                            Log.d("Address", strReturnedAddress.toString())
                        } else {
                            Log.w("Address", "No Address returned!")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.w("Address", "Cannot get Address!")
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
                    sharedPref.edit().putString("oauth_token_secret", accToken?.tokenSecret ?: "")
                        .apply()
                }



                //listView
                val listView = findViewById<ListView>(R.id.listView)

                listView.adapter =
                    myCustomAdapter(this) // this needs to be custom adapter telling list what to render



    }

            private class myCustomAdapter(context: Context) : BaseAdapter() {

                private val mContext: Context


                //Hardcoded Array List used to fill the bird book with examples. Need to implement dynamic usage.
                //Array List of birds should be saved on users local storage...
                private val birds = arrayListOf<Bird>(
                    Bird("Metallic Starling", R.drawable.metallic_starling, "unknown"),
                    Bird("Blue Jay", R.drawable.blue_jay, "unknown"),
                    Bird("Common Blackbird", R.drawable.common_blackbird, "unknown"),
                    Bird("Eurasion Magpie", R.drawable.eurasion_magpie, "unknown"),
                    Bird("European Robin", R.drawable.european_robin, "unknown")
                )

                init {
                    this.mContext = context
                }

                // 4 methods needed to avoid error
                // responsible for number of rows in list
                override fun getCount(): Int {
                    return birds.size
                }

                // Ignore
                override fun getItemId(position: Int): Long {
                    return position.toLong()
                }

                // Ignore
                override fun getItem(position: Int): Any {
                    return "Test String!"
                }

                // responsible for rendering out each row
                override fun getView(
                    position: Int,
                    convertView: View?,
                    viewGroup: ViewGroup?
                ): View {
                    val layoutInflater = LayoutInflater.from(mContext)
                    val rowMain =
                        layoutInflater.inflate(R.layout.bird_book_list_row_layout, viewGroup, false)

                    //Overwriting textViewClassName in row_main.xml
                    val textViewClassName = rowMain.findViewById<TextView>(R.id.textViewClassName)
                    textViewClassName.text = birds.get(position).name

                    //Overwriting textViewWiki in row_main.xml
                    //val textViewWiki = rowMain.findViewById<TextView>( R.id.textViewWiki)
                    //textViewWiki.text = birds.get(position).wiki

                    //Using image from arrayListOf<Bird>
                    val imageViewBird = rowMain.findViewById<ImageView>(R.id.imageViewBird)
                    imageViewBird.setImageResource(birds.get(position).image)

                    //Overwriting textViewDate in row_main.xml
                    val textViewDate = rowMain.findViewById<TextView>(R.id.textViewDate)
                    textViewDate.text = "Date: ${birds.get(position).date}"

                    //Apply function to individual row Buttons. Add functionality from birds ArrayList
                    val buttonPlaySound = rowMain.findViewById<Button>(R.id.buttonPlaySound)
                    buttonPlaySound.setOnClickListener {
                        Toast.makeText(
                            mContext,
                            "Button clicked for item #$position",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

//            val textView = TextView(mContext)
//            textView.text = "Here is my ROW for my ListView"
//            return textView

                    return rowMain
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