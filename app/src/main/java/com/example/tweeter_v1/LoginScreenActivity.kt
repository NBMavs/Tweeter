package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login_screen.*

class LoginScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.login_screen )

        buttonSubmitLogin.setOnClickListener {

            fun verifyLogin( ){
                //Enter code here
            }
            //  IF (Login Valid) {
            startActivity( Intent( this, RecordAudio::class.java ) )
            //  }

        }
    }




}