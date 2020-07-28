package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.user_menu.*

class UserMenu: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.user_menu )

        buttonRecordAudio.setOnClickListener {

            startActivity( Intent( this, RecordAudio::class.java ) )

        }

    }
}