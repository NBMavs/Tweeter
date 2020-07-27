package com.example.tweeter_v1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin.setOnClickListener {
            startActivity( Intent( this, LoginScreen::class.java ) )
        }

        buttonCreateAccount.setOnClickListener {
            startActivity( Intent( this, CreateAccount::class.java ) )
        }

        val preferences = getSharedPreferences( "database" , Context.MODE_PRIVATE )
        val savedFirstName = preferences.getString( "savedFirstName", "This value doesn't exist." )
        d( "name", "First Name: $savedFirstName" );

    }
}