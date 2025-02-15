package com.example.tweeter_v1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.create_account.*

class CreateAccount: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        buttonCreateAccountSubmit.setOnClickListener {

            //add user account information
            val email = editTextEmailAddress.text.toString()
            val username = editTextUsernameCreateAccount.text.toString()
            val password = editTextPassword.text.toString()
            val passwordConfirm = editTextPasswordConfirm.text.toString()

            //Verify account information is valid
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty())
            {
                if (email.isEmpty())
                    Toast.makeText(this, "Please input your email.", Toast.LENGTH_SHORT).show()

                if (username.isEmpty())
                    Toast.makeText(this, "Please input a username.", Toast.LENGTH_SHORT).show()

                if (password.isEmpty())
                    Toast.makeText(this, "Please input a password.", Toast.LENGTH_SHORT).show()

                if (passwordConfirm.isEmpty())
                    Toast.makeText(this, "Please confirm your password.", Toast.LENGTH_SHORT).show()

                            return@setOnClickListener
            }

            if(username.length > 25)
            {
                Toast.makeText(this, "Please input a username less than 25 characters long.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordConfirm)
            {
                Toast.makeText(this, "Password and confirmation must match!.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("Create_Account", "Email Address is: " + email)
            Log.d("Create_Account", "Username is: " + username)

            //IF account information is valid, add information to database
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful)
                    {
                        Toast.makeText(this, "Account creation failed!", Toast.LENGTH_SHORT).show()
                        Log.d("Create_Account", "Failed")
                        return@addOnCompleteListener
                    }

                    //else if successful
                    else
                    {
                        Toast.makeText(this, "Account creation successful!", Toast.LENGTH_SHORT).show()
                        val user = Firebase.auth.currentUser
                        user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("Create_Account","Email sent.")
                                    val profileUpdates = userProfileChangeRequest {
                                        displayName = username
                                    }
                                    user.updateProfile(profileUpdates)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d("Create_Account","User profile updated.")
                                            }
                                        }

                                    }
                                }
                            }
                        Log.d("Create_Account","Successfully created user with uid: $it.result.user.uid ")
                        setContentView( R.layout.login_screen )
                    }
                }
        }
    }