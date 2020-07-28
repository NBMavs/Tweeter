package com.example.tweeter_v1

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.record_audio.*


class RecordAudio: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.record_audio )

        button.isEnabled = false
        button2.isEnabled = false

        if(ActivityCompat.checkSelfPermission( this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO,
                                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE),  111)
            button.isEnabled = true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            button.isEnabled = true
    }

}