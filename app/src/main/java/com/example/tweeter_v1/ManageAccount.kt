package com.example.tweeter_v1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.manage_account.*

class ManageAccount: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.manage_account )

        val user = Firebase.auth.currentUser

        user?.let {
            val name = user.displayName
            val email = user.email

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            textViewUsernameCurrent.text = user.displayName
            textViewEmailCurrent.text = user.email
        }





        buttonChangeUsername.setOnClickListener {
            val username = editTextChangeUsername.text.toString()

            if(username.isEmpty()) {
                Toast.makeText(this, "Please enter a new username.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else
            {
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Profile_Update","User profile updated.")
                        }
                    }
            }
        }

        buttonChangeEmail.setOnClickListener {
            val email = editTextChangeEmail.text.toString()

            if(email.isEmpty()) {
                Toast.makeText(this, "Please enter a new email.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else
            {
                user!!.updateEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("Profile_Update","Email sent.")
                                    }
                                }
                            Log.d("Profile_Update","User email updated to $email.")
                        }
                    }
            }
        }

        buttonChangePassword.setOnClickListener {
            val username = editTextChangeUsername.text.toString()

            if(username.isEmpty()) {
                Toast.makeText(this, "Please enter a new username.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else
            {
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Profile_Update","User profile updated.")
                        }
                    }
            }
        }

        //ADD functionality here!
    }
}