package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //Removes functionality of back button for this page. Back button stays on same page at MainMenu
        onBackPressedDispatcher.addCallback { this }

        //OPEN login activity
        buttonLogin.setOnClickListener {
            startActivity(Intent(this, NavigationMain::class.java))   //Remove after testing

            //startActivity( Intent( this, LoginScreen::class.java ) )          //Uncomment after testing
        }

        //OPEN create account activity
        buttonCreateAccount.setOnClickListener {
            startActivity( Intent( this, CreateAccount::class.java ) )
        }

    }
}