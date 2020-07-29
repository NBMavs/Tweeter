package com.example.tweeter_v1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.manage_account.*

class ManageAccount: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.manage_account )

        buttonChangeUsername.setOnClickListener {
            //CODE
        }
        buttonChangePassword.setOnClickListener {
            //CODE
        }

        //ADD functionality here!
    }
}