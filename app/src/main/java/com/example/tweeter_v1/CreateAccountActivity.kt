package com.example.tweeter_v1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.create_account.*

class CreateAccount: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        buttonCreateAccountSubmit.setOnClickListener {

            //add user account information
            val firstName = editTextFirstName.text.toString()
            val lastName = editTextLastName.text.toString()
            val email = editTextEmailAddress.text.toString()
            val username = editTextUsernameCreateAccount.text.toString()
            val password = editTextPassword.text.toString()
            val passwordConfirm = editTextPasswordConfirm.text.toString()

            //Verify account information is valid
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty())
            {
                if (firstName.isEmpty() || lastName.isEmpty())
                    Toast.makeText(this, "Please input your name.", Toast.LENGTH_SHORT).show()

                if (email.isEmpty())
                    Toast.makeText(this, "Please input your email.", Toast.LENGTH_SHORT).show()

                if (password.isEmpty())
                    Toast.makeText(this, "Please input a password.", Toast.LENGTH_SHORT).show()

                            return@setOnClickListener
            }

            if (password != passwordConfirm)
            {
                Toast.makeText(this, "Password and confirmation must match!.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("Create_Account", "First Name is: " + firstName)
            Log.d("Create_Account", "Last Name is: " + lastName)
            Log.d("Create_Account", "Email Address is: " + email)
            Log.d("Create_Account", "Username is: " + username)
            Log.d("Create_Account", "Password is: " + password)
            Log.d("Create_Account", "First Name is: " + passwordConfirm)

            //IF account information is valid, add information to database
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful)
                    {
                        Log.d("Account", "Failed")
                        return@addOnCompleteListener
                    }

                    //else if successful
                    Log.d("Account", "Successfully created user with uid: $(it.result.user.uid}")
                }
            //ELSE display errors over correlating text field(s)

        }
    }



}