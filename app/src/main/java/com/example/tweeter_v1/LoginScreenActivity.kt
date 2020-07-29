package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.login_screen.*
import kotlinx.android.synthetic.main.login_screen.editTextPassword
import kotlinx.android.synthetic.main.login_screen.editTextEmailAddress as editTextEmailAddress1

lateinit var auth: FirebaseAuth


class LoginScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.login_screen )
       // auth = Firebase.auth

        buttonSubmitLogin.setOnClickListener {

            //Take user input username & password
            val email = editTextEmailAddress.text.toString()
            val password = editTextPassword.text.toString()

            Log.d("Login", "Attempt login with email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Log in","signInWithEmail:success")
                        startActivity(Intent(this, UserMenu::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Log in","signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_LONG).show()
                    }
                }
              //  .addOnFailureListener()



        }

        buttonForgotPassword.setOnClickListener {

            //Take user input username & password
            val email = editTextEmailAddress.text.toString()

            Log.d("Forgot Password", "Forgot Password with email: $email")

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Log.d("Forgot Password", "Email sent.")
                    }
                 else {
                        // If sign in fails, display a message to the user.
                        Log.d("Forgot Password","Invalid Email:failure", task.exception)
                        Toast.makeText(baseContext, "Email not found.", Toast.LENGTH_LONG).show()
                    }
                }
            //  .addOnFailureListener()



        }
    }
}