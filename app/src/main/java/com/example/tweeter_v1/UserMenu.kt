package com.example.tweeter_v1

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.user_menu.*



class UserMenu: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.user_menu )

        //Removes functionality of back button for this page. Back button stays on same page at MainMenu
        onBackPressedDispatcher.addCallback { this }


        buttonManageAccount.setOnClickListener {
            //Open ACCOUNT MANAGEMENT
            startActivity(Intent(this, ManageAccount::class.java))
        }
        buttonRecordAudio.setOnClickListener {
            //Open AUDIO RECORDER
            val intent = Intent(this, RecordFragment::class.java)
            startActivity(intent)
            //startActivity(Intent(this, RecordAudio::class.java))

        }
        buttonBirdBook.setOnClickListener {
            //OPEN BIRD BOOK
            startActivity(Intent(this, BirdBookActivity::class.java))

        }
        buttonViewMap.setOnClickListener {
            //OPEN MAP
            startActivity(Intent(this, ViewMapActivity::class.java))

        }
        buttonLogout.setOnClickListener {
            //Do firebase stuff/logout account
            //OPEN main_activity
            startActivity(Intent(this, MainActivity::class.java))

        }

        }
    }