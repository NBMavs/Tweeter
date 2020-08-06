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
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.player_sheet.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class RecordFragment : Fragment(), View.OnClickListener, MediaRecorder.OnInfoListener {

    private var navController: NavController?=null

    private var lstButton: ImageButton?=null
    private var recButton: ImageButton?=null

    private var isRecording = false

    private var recPerms: String = Manifest.permission.RECORD_AUDIO
    private var writePerms: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val reqCode: Int = 1

    private var mediaRecorder: MediaRecorder? = null
    private var recFileName: String?=null
    private var recFilePath: String?=null

    private var fileNameText: TextView?=null

    private var timer: Chronometer?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        recButton = view.findViewById<ImageButton>(R.id.record_btn)
        lstButton = view.findViewById<ImageButton>(R.id.record_list_btn)
        timer = view.findViewById<Chronometer>(R.id.record_chronometer)
        fileNameText = view.findViewById<TextView>(R.id.record_filename)

        lstButton!!.setOnClickListener(this)
        recButton!!.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            lstButton!!.id -> {
                navController!!.navigate(R.id.action_recordFragment_to_audioListFragment)
            }
            recButton!!.id -> {
                isRecording = if(isRecording){

                    true

                } else {

                    //Start Recording
                    if(checkUserPerms()){
                        timer!!.base = SystemClock.elapsedRealtime()
                        timer!!.start()

                        startRecording()
                        recButton!!.setImageDrawable(resources.getDrawable(R.drawable.record_btn_recording, null))
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
        mediaRecorder!!.stop()
        mediaRecorder!!.reset()
        mediaRecorder!!.release()
        mediaRecorder = null
        recButton!!.setImageDrawable(resources.getDrawable(R.drawable.record_btn_stopped, null))
        timer!!.stop()
        isRecording = false

        var file = File(recFilePath)
        if(file.exists()){
            Log.d("Tag", "$recFilePath exists")
        }
        else {
            Log.d("Tag", "$recFilePath does not exist")
        }

        fileNameText?.text = "Recording Stopped!\n Last File Saved: \n$recFileName"


    }

    private fun startRecording() {
        var recordPath: String = requireActivity().getExternalFilesDir("/")!!.absolutePath
        val pattern = "yyyy-MM-dd_hh_ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(Date())

        recFileName = "Recording_$date.3gp"
        recFilePath = "$recordPath/$recFileName"

        fileNameText?.text = "Recording!\n File Name: \n$recFileName"


        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setOutputFile("$recordPath/$recFileName")
        mediaRecorder?.setMaxDuration(5000)
        mediaRecorder?.setOnInfoListener(this)

        Log.d("TAG", recFilePath!!)

        mediaRecorder!!.prepare()

        mediaRecorder!!.start()
    }

    private fun checkUserPerms(): Boolean {
        return if (context?.let { ActivityCompat.checkSelfPermission(it, recPerms) } == PackageManager.PERMISSION_GRANTED
            && context?.let { ActivityCompat.checkSelfPermission(it, writePerms) } == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            Log.d("Tag", "Inside request perms block")
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(recPerms, writePerms), reqCode) }
            //activity?.let { ActivityCompat.requestPermissions(it, arrayOf(writePerms), reqCode) }
            false
        }
    }


    override fun onInfo(p0: MediaRecorder?, p1: Int, p2: Int) {
        if(p1 == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
            Log.v("AUDIOCAPTURE","Max duration reached")
            stopRecording()
        }
    }
}