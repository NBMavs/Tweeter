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
                            Log.d("Profile_Update","Username updated to $username.")
                            Toast.makeText(this, "Username updated to $username", Toast.LENGTH_LONG).show()
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
                            user.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("Profile_Update","Email sent.")
                                    }
                                }
                            Log.d("Profile_Update","User email updated to $email.")
                            Toast.makeText(this, "User email updated to $email. Verification email sent.", Toast.LENGTH_LONG).show()
                        }
                        else
                            Toast.makeText(this, "Email authentication failed, please enter a full email address.", Toast.LENGTH_LONG).show()
                    }
            }
        }

        buttonChangePassword.setOnClickListener {
            val oldpassword = editTextOldPassword.text.toString()
            val newpassword = editTextChangePassword.text.toString()
            val newpassconfirm = editTextChangePasswordConfirm.text.toString()


            if(newpassword != newpassconfirm) {
            Toast.makeText(this, "Password and Password Confirm must match completely.", Toast.LENGTH_LONG).show()
            return@setOnClickListener
            }
            else {
                if (newpassword.isEmpty() || newpassconfirm.isEmpty()) {
                    Toast.makeText(this,"Both password and new password must be entered.",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                else {
                    user!!.updatePassword(newpassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Profile_Update", "User password updated.")
                                Toast.makeText(this,"User password updated.",Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }
    }
}