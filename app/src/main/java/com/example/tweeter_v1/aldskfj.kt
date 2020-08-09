//package com.tvacstudio.audiorecorder
//
//import AudioListAdapter.onItemListClick
//import android.media.MediaPlayer
//import android.os.Bundle
//import android.os.Handler
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.SeekBar
//import android.widget.SeekBar.OnSeekBarChangeListener
//import android.widget.TextView
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
//import java.io.File
//import java.io.IOException
//
///**
// * A simple [Fragment] subclass.
// */
//class AudioListFragment : Fragment(), onItemListClick {
//    private var playerSheet: ConstraintLayout? = null
//    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
//    private var audioList: RecyclerView? = null
//    private var allFiles: Array<File>
//    private var audioListAdapter: AudioListAdapter? = null
//    private var mediaPlayer: MediaPlayer? = null
//    private var isPlaying = false
//    private var fileToPlay: File? = null
//
//    //UI Elements
//    private var playBtn: ImageButton? = null
//    private var playerHeader: TextView? = null
//    private var playerFilename: TextView? = null
//    private var playerSeekbar: SeekBar? = null
//    private var seekbarHandler: Handler? = null
//    private var updateSeekbar: Runnable? = null
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_audio_list, container, false)
//    }
//
//    override fun onViewCreated(
//        view: View,
//        savedInstanceState: Bundle?
//    ) {
//        super.onViewCreated(view, savedInstanceState)
//        playerSheet = view.findViewById(R.id.player_sheet)
//        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet)
//        audioList = view.findViewById(R.id.audio_list_view)
//        playBtn = view.findViewById(R.id.player_play_btn)
//        playerHeader = view.findViewById(R.id.player_header_title)
//        playerFilename = view.findViewById(R.id.player_filename)
//        playerSeekbar = view.findViewById(R.id.player_seekbar)
//        val path = activity!!.getExternalFilesDir("/")!!.absolutePath
//        val directory = File(path)
//        allFiles = directory.listFiles()
//        audioListAdapter = AudioListAdapter(allFiles, this)
//        audioList.setHasFixedSize(true)
//        audioList.setLayoutManager(LinearLayoutManager(context))
//        audioList.setAdapter(audioListAdapter)
//        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
//            override fun onStateChanged(
//                bottomSheet: View,
//                newState: Int
//            ) {
//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
//                }
//            }
//
//            override fun onSlide(
//                bottomSheet: View,
//                slideOffset: Float
//            ) {
//                //We cant do anything here for this app
//            }
//        })
//        playBtn.setOnClickListener(View.OnClickListener {
//            if (isPlaying) {
//                pauseAudio()
//            } else {
//                if (fileToPlay != null) {
//                    resumeAudio()
//                }
//            }
//        })
//        playerSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seekBar: SeekBar,
//                progress: Int,
//                fromUser: Boolean
//            ) {
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                pauseAudio()
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                val progress = seekBar.progress
//                mediaPlayer!!.seekTo(progress)
//                resumeAudio()
//            }
//        })
//    }
//
//    fun onClickListener(file: File?, position: Int) {
//        fileToPlay = file
//        if (isPlaying) {
//            stopAudio()
//            playAudio(fileToPlay)
//        } else {
//            playAudio(fileToPlay)
//        }
//    }
//
//    private fun pauseAudio() {
//        mediaPlayer!!.pause()
//        playBtn!!.setImageDrawable(
//            activity!!.resources.getDrawable(R.drawable.player_play_btn, null)
//        )
//        isPlaying = false
//        seekbarHandler!!.removeCallbacks(updateSeekbar!!)
//    }
//
//    private fun resumeAudio() {
//        mediaPlayer!!.start()
//        playBtn!!.setImageDrawable(
//            activity!!.resources.getDrawable(R.drawable.player_pause_btn, null)
//        )
//        isPlaying = true
//        updateRunnable()
//        seekbarHandler!!.postDelayed(updateSeekbar!!, 0)
//    }
//
//    private fun stopAudio() {
//        //Stop The Audio
//        playBtn!!.setImageDrawable(
//            activity!!.resources.getDrawable(R.drawable.player_play_btn, null)
//        )
//        playerHeader!!.text = "Stopped"
//        isPlaying = false
//        mediaPlayer!!.stop()
//        seekbarHandler!!.removeCallbacks(updateSeekbar!!)
//    }
//
//    private fun playAudio(fileToPlay: File?) {
//        mediaPlayer = MediaPlayer()
//        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
//        try {
//            mediaPlayer!!.setDataSource(fileToPlay!!.absolutePath)
//            mediaPlayer!!.prepare()
//            mediaPlayer!!.start()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        playBtn!!.setImageDrawable(
//            activity!!.resources.getDrawable(R.drawable.player_pause_btn, null)
//        )
//        playerFilename!!.text = fileToPlay!!.name
//        playerHeader!!.text = "Playing"
//        //Play the audio
//        isPlaying = true
//        mediaPlayer!!.setOnCompletionListener {
//            stopAudio()
//            playerHeader!!.text = "Finished"
//        }
//        playerSeekbar!!.max = mediaPlayer!!.duration
//        seekbarHandler = Handler()
//        updateRunnable()
//        seekbarHandler!!.postDelayed(updateSeekbar!!, 0)
//    }
//
//    private fun updateRunnable() {
//        updateSeekbar = object : Runnable {
//            override fun run() {
//                playerSeekbar!!.progress = mediaPlayer!!.currentPosition
//                seekbarHandler!!.postDelayed(this, 500)
//            }
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if (isPlaying) {
//            stopAudio()
//        }
//    }
//}