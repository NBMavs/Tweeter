package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login_screen.*

class LoginScreen: AppCompatActivity() {

    fun usernameIsInFirebase( username: String ): Boolean{
        //CODE HERE
        return true
    }

    fun passwordMatchesUsernameAccount( username: String, password: String ): Boolean{
        //RETURN TRUE or FALSE
        return true
    }

    fun verifyLogin( username: String, password: String ): Boolean{
        if ( (usernameIsInFirebase( username ) ) && ( passwordMatchesUsernameAccount( username, password ) ) ) {
            //uploadAccountInformation( username )
            return true
        }
        else{
            //display red text message saying "Invalid username/password"
            return false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.login_screen )

        buttonSubmitLogin.setOnClickListener {

            //Take user input username & password
            var username = editTextEmailLogin.text.toString()
            var password = editTextPassword.text.toString()

            if( verifyLogin( username, password ) ){
                //This opens User Menu after username & password validation
                startActivity( Intent( this, UserMenu::class.java ) )   //Move into verifyLogin()
            }


        }
    }
}