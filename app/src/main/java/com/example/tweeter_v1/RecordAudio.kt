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
import android.widget.Toast
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"



class RecordAudio: AppCompatActivity() {

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.record_audio )


        output = Environment.getExternalStorageDirectory().toString() + "/recording.3gp"
//        output = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"





        //Start recording
        buttonViewMap.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
                buttonViewMap.isEnabled = true
            } else {
                startRecording()
            }


        }

        //Stop recording
        button2.setOnClickListener {
            stopRecording()
        }

        button3.setOnClickListener {
            var mp = MediaPlayer()
            mp.setDataSource(output)
            mp.prepare()
            mp.start()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            buttonViewMap.isEnabled = true
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setOutputFile(output)

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            Toast.makeText(this, "Illegal State Exception!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        } catch (e: IOException) {
            Toast.makeText(this, "IO Exception!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }


    private fun stopRecording(){
        if(state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

}