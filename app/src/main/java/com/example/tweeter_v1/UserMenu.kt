package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_menu.*



class UserMenu: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.user_menu )

        //Removes functionality of back button for this page. Back button stays on same page at MainMenu
        onBackPressedDispatcher.addCallback { this }

        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid

            textViewDisplayName.text = name
        }



        buttonManageAccount.setOnClickListener {
            //Open AUDIO RECORDER
            startActivity(Intent(this, ManageAccount::class.java))

            buttonRecordAudio.setOnClickListener {
                //Open AUDIO RECORDER
                startActivity(Intent(this, RecordAudio::class.java))

            }
            buttonBirdBook.setOnClickListener {
                //OPEN BIRD BOOK
                startActivity(Intent(this, BirdBookActivity::class.java))

            }
            buttonViewMap.setOnClickListener {
                //OPEN MAP
                startActivity(Intent(this, ViewMapActivity::class.java))

            }
            buttonLogout.setOnClickListener {
                //Do firebase stuff/logout account
                //OPEN main_activity
                startActivity(Intent(this, MainActivity::class.java))

            }

        }
    }
}