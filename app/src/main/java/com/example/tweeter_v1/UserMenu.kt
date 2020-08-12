package com.example.tweeter_v1

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_menu.*
import kotlinx.coroutines.*
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

//lateinit var twitterDialog: Dialog
//lateinit var twitter: Twitter
//var accToken: AccessToken? = null

//val user = Firebase.auth.currentUser

class UserMenu: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_menu)

//        //Removes functionality of back button for this page. Back button stays on same page at MainMenu
//        onBackPressedDispatcher.addCallback { this }
//
//        buttonManageAccount.setOnClickListener {
//            //Open ACCOUNT MANAGEMENT
//            startActivity(Intent(this, ManageAccount::class.java))
//        }
//        buttonRecordAudio.setOnClickListener {
//            //Open AUDIO RECORDER
//
//
//            startActivity(Intent(this, RecordingBrowser::class.java))
//
//        }
//        buttonBirdBook.setOnClickListener {
//            //OPEN BIRD BOOK
//            //startActivity(Intent(this, BirdBookActivity::class.java))
//
//        }
//        buttonViewMap.setOnClickListener {
//            //OPEN MAP
//            startActivity(Intent(this, ViewMapActivity::class.java))
//
//        }
//
//        buttonTempVerify.setOnClickListener {
//            //Verify Classification Window
//            startActivity(Intent(this, VerifyClassification::class.java))
//
//        }
//
//        buttonNavigationMain.setOnClickListener{
//            startActivity(Intent(this, NavigationMain::class.java))
//
//        }
//
//

//        buttonLogout.setOnClickListener {
//            //Do firebase stuff/logout account
//            FirebaseAuth.getInstance().signOut()    // Sign user out of system
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            //OPEN main_activity
//            startActivity(Intent(this, MainActivity::class.java))
//
//        }

//            GlobalScope.launch {
//                val results = GlobalScope.async { isLoggedIn() }
//                val result = results.await()
//                if (result) {
//                    // Show the Activity with the logged in user
//                    Log.d("LoggedIn?: ", "YES")
//                } else {
//                    // Show the Home Activity
//                    Log.d("LoggedIn?: ", "NO")
//                }
//            }
//
//            buttonTwitterLogin1.setOnClickListener {
//                getRequestToken()
//            }
//
//        }
//
//        private fun getRequestToken() {
//            GlobalScope.launch(Dispatchers.Default) {
//                val builder = ConfigurationBuilder()
//                    .setDebugEnabled(true)
//                    .setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
//                    .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
//                    .setIncludeEmailEnabled(true)
//                val config = builder.build()
//                val factory = TwitterFactory(config)
//                twitter = factory.instance
//                try {
//                    val requestToken = twitter.oAuthRequestToken
//                    withContext(Dispatchers.Main) {
//                        setupTwitterWebviewDialog(requestToken.authorizationURL)
//                    }
//                } catch (e: IllegalStateException) {
//                    Log.e("ERROR: ", e.toString())
//                }
//            }
//        }
//
//        // Show twitter login page in a dialog
//        @SuppressLint("SetJavaScriptEnabled")
//        fun setupTwitterWebviewDialog(url: String) {
//            twitterDialog = Dialog(this)
//            val webView = WebView(this)
//            webView.isVerticalScrollBarEnabled = false
//            webView.isHorizontalScrollBarEnabled = false
//            webView.webViewClient = TwitterWebViewClient()
//            webView.settings.javaScriptEnabled = true
//            webView.loadUrl(url)
//            twitterDialog.setContentView(webView)
//            twitterDialog.show()
//        }
//
//        // A client to know about WebView navigations
//        // For API 21 and above
//        @Suppress("OverridingDeprecatedMember")
//        inner class TwitterWebViewClient : WebViewClient() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest?
//            ): Boolean {
//                if (request?.url.toString().startsWith(TwitterConstants.CALLBACK_URL)) {
//                    Log.d("Authorization URL: ", request?.url.toString())
//                    handleUrl(request?.url.toString())
//
//                    // Close the dialog after getting the oauth_verifier
//                    if (request?.url.toString().contains(TwitterConstants.CALLBACK_URL)) {
//                        twitterDialog.dismiss()
//                    }
//                    return true
//                }
//                return false
//            }
//
//            // For API 19 and below
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                if (url.startsWith(TwitterConstants.CALLBACK_URL)) {
//                    Log.d("Authorization URL: ", url)
//                    handleUrl(url)
//
//                    // Close the dialog after getting the oauth_verifier
//                    if (url.contains(TwitterConstants.CALLBACK_URL)) {
//                        twitterDialog.dismiss()
//                    }
//                    return true
//                }
//                return false
//            }
//
//            // Get the oauth_verifier
//            private fun handleUrl(url: String) {
//                val uri = Uri.parse(url)
//                val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
//                GlobalScope.launch(Dispatchers.Main) {
//                    accToken =
//                        withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
//                    getUserProfile()
//                }
//            }
//        }
//
//        suspend fun getUserProfile() {
//            val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }
//
//            //Twitter Id
//            val twitterId = usr.id.toString()
//            Log.d("Twitter Id: ", twitterId)
//
//            //Twitter Handle
//            val twitterHandle = usr.screenName
//            Log.d("Twitter Handle: ", twitterHandle)
//
//            //Twitter Name
//            val twitterName = usr.name
//            Log.d("Twitter Name: ", twitterName)
//
//            //Twitter Email
//            val twitterEmail = usr.email
//
//            // Twitter Access Token
//            Log.d("Twitter Access Token", accToken?.token ?: "")
//            var accessToken = accToken?.token ?: ""
//
//
//            // Save the Access Token (accToken.token) and Access Token Secret (accToken.tokenSecret) using SharedPreferences
//            // This will allow us to check user's logging state every time they open the app after cold start.
//            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
//            sharedPref.edit().putString("oauth_token", accToken?.token ?: "").apply()
//            sharedPref.edit().putString("oauth_token_secret", accToken?.tokenSecret ?: "").apply()
//        }
//
//
//        suspend fun isLoggedIn(): Boolean {
//            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
//            val accessToken = sharedPref.getString("oauth_token", "")
//            val accessTokenSecret = sharedPref.getString("oauth_token_secret", "")
//
//            val builder = ConfigurationBuilder()
//            builder.setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
//                .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
//                .setOAuthAccessToken(accessToken)
//                .setOAuthAccessTokenSecret(accessTokenSecret)
//            val config = builder.build()
//            val factory = TwitterFactory(config)
//            val twitter = factory.instance
//            try {
//                withContext(Dispatchers.IO) { twitter.verifyCredentials() }
//                return true
//            } catch (e: Exception) {
//                return false
//            }
//        }

    }
}