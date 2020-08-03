package com.example.tweeter_v1

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.bird_book.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


var provider = OAuthProvider.newBuilder("twitter.com")

var firebaseUser = Firebase.auth.currentUser
val user = Firebase.auth.currentUser!!


class BirdBookActivity: AppCompatActivity() {

    fun shareTwitter(message:Editable) {
            val tweetIntent = Intent(Intent.ACTION_SEND)
            tweetIntent.putExtra(Intent.EXTRA_TEXT, message.toString())
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

        val message = editTextTweet.text

        val buttonIntentTweet: Button = findViewById(R.id.buttonIntentTweet)
        buttonIntentTweet.setOnClickListener {
            shareTwitter(message)
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
}