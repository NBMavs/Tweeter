package com.example.tweeter_v1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.create_account.*

class CreateAccount: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.create_account )

        buttonCreateAccountSubmit.setOnClickListener {

            //add user account information
            val firstName = editTextFirstName.text.toString()
            val lastName = editTextLastName.text.toString()
            val email = editTextEmailAddress.text.toString()
            val username = editTextUsernameCreateAccount.text.toString()
            val password = editTextPassword.text.toString()
            val passwordConfirm = editTextPasswordConfirm.text.toString()

            //Verify account information is valid

            //IF account information is valid, add information to database

            //ELSE display errors over correlating text field(s)

        }
    }



}