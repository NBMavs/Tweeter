package com.example.tweeter_v1

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.create_account.*

class CreateAccount: AppCompatActivity() {

    lateinit var handler:DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.create_account )

        handler = DatabaseHelper( this )

        //add user input account information
        buttonCreateAccountSubmit.setOnClickListener {

            handler.insertUserData(
                editTextFirstName.text.toString(),
                editTextEmailAddress.text.toString(),
                editTextPassword.text.toString(),
                )


//            val database = getSharedPreferences( "database" , Context.MODE_PRIVATE )
//            database.edit().apply {
//                putString( "savedFirstName", editTextFirstName.text.toString() )
//
//            }.apply()

        }



    }



}