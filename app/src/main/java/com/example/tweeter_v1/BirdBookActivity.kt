package com.example.tweeter_v1

import androidx.appcompat.app.AppCompatActivity


var birds : MutableList<VerifyClassification.DBWrite> =  mutableListOf(VerifyClassification.DBWrite("","","","",""))

var CurLoc: String = ""
class BirdBookActivity: AppCompatActivity()

    /* Code to read from database!
    fun loadDB(){
        birds.clear()
        val user = Firebase.auth.currentUser
        dbReference.child(user!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("ValueEventListener", "Backed out of Bird Book")
            }
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                tweet = "Click here to display bird book!"
                Log.d("Snapshot UID", user.uid)
                snapshot.children.forEach {
                    if(snapshot.key!!.equals(user.uid))
                    {
                        val user5 = it.getValue(VerifyClassification.DBWrite::class.java)
                        birds.add(user5!!)

                        tweet = "I just verified a " + user5.birdsType + " at " + user5.location + " on " + user5.time + " on the Tweeter app!"
                    }
                }
                val size = birds.size
                Log.d("birds array completed","Total birds: $size")
            }
        }
    }



    fun shareTwitter(message: Editable) {
        val tweetIntent = Intent(Intent.ACTION_SEND)
        val c = message.toString()
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
        //editTextTweet.text = tweet.toEditable()

        LocationHelper().startListeningUserLocation(
            this,
            object : LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    // Here you got user location :)
                    //CurLoc = location.latitude.toString() + "," + location.longitude.toString()
                    CurLoc = getCompleteAddressString(location.latitude, location.longitude)
                }
            })


                //val message = editTextTweet.text


        //listView
        val listView = findViewById<ListView>(R.id.birdbook_list_view)
//        listView.adapter =
//            myCustomAdapter(this) // this needs to be custom adapter telling list what to render
    }

    fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses.get(0)
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
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



//    private class myCustomAdapter(context: Context) : BaseAdapter() {
//
//        private val mContext: Context
//
//        //Hardcoded Array List used to fill the bird book with examples. Need to implement dynamic usage.
//        //Array List of birds should be saved on users local storage...
//        private val birdslocal = arrayListOf<Bird>(
//            Bird("Metallic Starling", R.drawable.metallic_starling, "unknown"),
//            Bird("Blue Jay", R.drawable.blue_jay, "unknown"),
//            Bird("Common Blackbird", R.drawable.common_blackbird, "unknown"),
//            Bird("Eurasion Magpie", R.drawable.eurasion_magpie, "unknown"),
//            Bird("European Robin", R.drawable.european_robin, "unknown")
//        )
//
//        init {
//            this.mContext = context
//        }
//
//        // 4 methods needed to avoid error
//        // responsible for number of rows in list
//        override fun getCount(): Int {
//            return birds.size
//        }
//
//        // Ignore
//        override fun getItemId(position: Int): Long {
//            return position.toLong()
//        }
//
//        // Ignore
//        override fun getItem(position: Int): Any {
//            return "Test String!"
//        }
//
//        // responsible for rendering out each row
//        override fun getView(
//            position: Int,
//            convertView: View?,
//            viewGroup: ViewGroup?
//        ): View {
//            val layoutInflater = LayoutInflater.from(mContext)
//            val rowMain =
//                layoutInflater.inflate(R.layout.bird_book_list_row_layout, viewGroup, false)
//
//            //Overwriting textViewClassName in row_main.xml
//            val textViewClassName = rowMain.findViewById<TextView>(R.id.textViewClassName)
//            textViewClassName.text = birds.get(position).birdsType
//
//            //Overwriting textViewWiki in row_main.xml
//            //val textViewWiki = rowMain.findViewById<TextView>( R.id.textViewWiki)
//            //textViewWiki.text = birds.get(position).wiki
//            val buttonBirdWiki = rowMain.findViewById<Button>(R.id.buttonBirdWiki)
//            if (birds.get(position).birdsType.equals("Blue Jay")) {
//               buttonBirdWiki.setOnClickListener {
//                   val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Blue_jay")
//                   val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
//                   this.mContext.startActivity(launchBrowser)
//                }
//            }
//            else if (birds.get(position).birdsType.equals("Metallic Starling")) {
//                buttonBirdWiki.setOnClickListener {
//                    val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Metallic_starling")
//                    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
//                    this.mContext.startActivity(launchBrowser)
//                }
//            }
//            else if (birds.get(position).birdsType.equals("European Robin")) {
//                buttonBirdWiki.setOnClickListener {
//                    val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/European_robin")
//                    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
//                    this.mContext.startActivity(launchBrowser)
//                }
//            }
//            else if (birds.get(position).birdsType.equals("Common Blackbird")) {
//                buttonBirdWiki.setOnClickListener {
//                    val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Common_blackbird")
//                    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
//                    this.mContext.startActivity(launchBrowser)
//                }
//            }
//            else if (birds.get(position).birdsType.equals("Eurasian Magpie")) {
//                buttonBirdWiki.setOnClickListener {
//                    val uriUrl = Uri.parse("https://en.wikipedia.org/wiki/Eurasian_magpie")
//                    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
//                    this.mContext.startActivity(launchBrowser)
//                }
//            }
//
//            //Overwwriting textViewLocation in
//            val textViewLocation = rowMain.findViewById<TextView>(R.id.textViewLocation)
//            textViewLocation.text = birds.get(position).location

            //Using image from arrayListOf<Bird>
            val imageViewBird = rowMain.findViewById<ImageView>(R.id.imageViewBird)
            imageViewBird.setImageResource(birdslocal.get(position).image)

            //Overwriting textViewDate in row_main.xml
            val textViewDate = rowMain.findViewById<TextView>(R.id.textViewDate)
            textViewDate.text = "Date: ${birds.get(position).time}"

            //Apply function to individual row Buttons. Add functionality from birds ArrayList
            val buttonPlaySound = rowMain.findViewById<Button>(R.id.buttonPlaySound)
            buttonPlaySound.setOnClickListener {
                Toast.makeText(
                    mContext,
                    "Button clicked for item #$position",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val textView = TextView(mContext)
            textView.text = "Here is my ROW for my ListView"
            return textView

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
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)*/