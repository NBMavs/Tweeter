package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.login_screen.*
import kotlinx.android.synthetic.main.login_screen.editTextPassword

class LoginScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.login_screen )

        buttonSubmitLogin.setOnClickListener {

            //Take user input username & password
            val email = editTextEmailAddress.text.toString()
            val password = editTextPassword.text.toString()

            Log.d("Login", "Attempt login with email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener()
                {
                    if(!it.isSuccessful)
                    {
                        Toast.makeText(this, "Login information incorrect!", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    else
                    {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity( Intent( this, UserMenu::class.java ) )
                    }
                }
              //  .addOnFailureListener()

       //     if( verifyLogin( email, password ) ){
                //This opens User Menu after username & password validation
    //            startActivity( Intent( this, UserMenu::class.java ) )   //Move into verifyLogin()
      //      }


        }
    }
}