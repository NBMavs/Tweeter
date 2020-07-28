package com.example.tweeter_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //OPEN login activity
        buttonLogin.setOnClickListener {
            startActivity( Intent( this, LoginScreen::class.java ) )
        }

        //OPEN create account activity
        buttonCreateAccount.setOnClickListener {
            startActivity( Intent( this, CreateAccount::class.java ) )
        }

    }
}