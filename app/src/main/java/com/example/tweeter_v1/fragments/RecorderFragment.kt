package com.example.tweeter_v1

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*


class RecorderFragment : Fragment(), View.OnClickListener, MediaRecorder.OnInfoListener {

    //Recording button
    private var recordBtn: ImageButton?=null

    //Controls state of recorder
    private var isRecording = false

    //Variables for permissions
    private var recPerms: String = Manifest.permission.RECORD_AUDIO
    private var writePerms: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val reqCode: Int = 1

    // Declaring variables for media recorder/file storage
    private var recorder: MediaRecorder? = null
    private var recFileName: String?=null
    private var recFilePath: String?=null
    private var fileNameText: TextView?=null

    //Variable for chronometer
    private var timer: Chronometer?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recorder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recordBtn = view.findViewById<ImageButton>(R.id.record_btn)
        timer = view.findViewById<Chronometer>(R.id.record_chronometer)
        fileNameText = view.findViewById<TextView>(R.id.record_filename)

        recordBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            // Handles behavior of record button
            // If a recording is in progress, nothing happens
            // If there is no recording in progress, one is started
            recordBtn!!.id -> {
                isRecording = if(isRecording){

                    true

                } else {

                    //Start Recording
                    if(checkUserPerms()){
                        timer!!.base = SystemClock.elapsedRealtime()
                        timer!!.start()

                        startRecording()
                        recordBtn!!.setImageDrawable(resources.getDrawable(R.drawable.record_btn_recording, null))
                        true
                    }
                    else{
                        false
                    }
                }
            }
        }
    }

    private fun stopRecording() {
        //Stops, resets, and releases the media recorder
        recorder!!.stop()
        recorder!!.reset()
        recorder!!.release()
        recorder = null

        //Changes recording image to one that matches the new state
        recordBtn!!.setImageDrawable(resources.getDrawable(R.drawable.record_btn_stopped, null))

        //Stops the chronometer
        timer!!.stop()

        isRecording = false

        fileNameText?.text = "Recording Stopped!\n Last File Saved: \n$recFileName"
    }

    private fun startRecording() {
        // Storage path for recording
        var recordPath: String = requireActivity().getExternalFilesDir("/")!!.absolutePath

        // Creates unique file name
        Log.d("Recording_location", recordPath)
        val pattern = "yyyy-MM-dd_hh_ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(Date())

        // Assembles file name
        recFileName = "Recording_$date.3gp"
        recFilePath = "$recordPath/$recFileName"

        // Updates text to display
        fileNameText?.text = "Recording!\n File Name: \n$recFileName"

        // Prepares and begins recording
        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder?.setOutputFile("$recordPath/$recFileName")
        recorder?.setMaxDuration(4000)
        recorder?.setOnInfoListener(this)
        recorder!!.prepare()
        recorder!!.start()
        Log.d("TAG", recFilePath!!)
    }

    // Handles checking and requesting required user permissions
    private fun checkUserPerms(): Boolean {
        return if (context?.let { ActivityCompat.checkSelfPermission(it, recPerms) } == PackageManager.PERMISSION_GRANTED
            && context?.let { ActivityCompat.checkSelfPermission(it, writePerms) } == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(recPerms, writePerms), reqCode) }
            false
        }
    }

    // Helper function that allows recording to stop after a
    // certain number of seconds
    override fun onInfo(p0: MediaRecorder?, p1: Int, p2: Int) {
        if(p1 == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
            stopRecording()
        }
    }






}