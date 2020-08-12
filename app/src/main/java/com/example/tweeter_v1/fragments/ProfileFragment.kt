package com.example.tweeter_v1.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tweeter_v1.R
import com.example.tweeter_v1.user
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for context fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val user = Firebase.auth.currentUser

        user?.let {
            val name = user.displayName
            val email = user.email

            view.textViewUsernameCurrent.text = name
            view.textViewEmailCurrent.text = email
        }

        view.buttonChangeUsername.setOnClickListener{
            val username = editTextChangeUsername.text.toString()

            when {
                username.isEmpty() -> {
                    Toast.makeText(context, "Please enter a new username.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                username.length > 25 -> {
                    Toast.makeText(context, "Please input a username less than 25 characters long.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Profile_Update","Username updated to $username.")
                                Toast.makeText(context, "Username updated to $username", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }

        view.buttonChangeEmail.setOnClickListener {
            val email = editTextChangeEmail.text.toString()
            val emailconfirm = editTextConfirmEmail.text.toString()

            if(email.isEmpty() || emailconfirm.isEmpty()) {
                Toast.makeText(context, "Please enter both a new email and  matching confirmation email.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(email != emailconfirm) {
                Toast.makeText(context, "Please enter both a new email and  matching confirmation email.", Toast.LENGTH_LONG).show()
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
                            Toast.makeText(context, "User email updated to $email. Verification email sent.", Toast.LENGTH_LONG).show()
                        }
                        else
                            Toast.makeText(context, "Email authentication failed, please enter a full email address.", Toast.LENGTH_LONG).show()
                    }
            }
        }

        view.buttonChangePassword.setOnClickListener {
            val newpassword = editTextChangePassword.text.toString()
            val newpassconfirm = editTextChangePasswordConfirm.text.toString()


            if(newpassword != newpassconfirm) {
                Toast.makeText(context, "Password and Password Confirm must match completely.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                if (newpassword.isEmpty() || newpassconfirm.isEmpty()) {
                    Toast.makeText(context,"Both password and new password must be entered.",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                else {
                    user!!.updatePassword(newpassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Profile_Update", "User password updated.")
                                Toast.makeText(context,"User password updated.",Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }

    return view
    }
}