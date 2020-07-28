package com.example.tweeter_v1

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.media.MediaRecorder
import android.media.MediaPlayer
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.record_audio.*


class RecordAudio: AppCompatActivity() {

    lateinit var mr : MediaRecorder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.record_audio )

        button.isEnabled = false
        button2.isEnabled = false

        var path = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp"
        mr = MediaRecorder()

        if(ActivityCompat.checkSelfPermission( this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),  111)
            button.isEnabled = true

        //Start recording
        button.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            button2.isEnabled = true
            button.isEnabled = false
        }

        //Stop recording
        button2.setOnClickListener {
            mr.stop()
            button.isEnabled = true
            button2.isEnabled = false
        }

        button3.setOnClickListener {
            var mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            button.isEnabled = true
    }

}