package com.example.tweeter_v1

import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File

class AudioListFragment : Fragment(), AudioListAdapter.onPlayClick {

//    private var playerSheet: ConstraintLayout? = null
    private var audioFileList: RecyclerView? = null
    private var fileList: ArrayList<File> = arrayListOf<File>()
    private var audioListAdapter: AudioListAdapter? = null
    private lateinit var audioListView: ListView

    private var fileToPlay: File? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    //UI stuff
//    private var playButton = requireView().findViewById<ImageButton>(R.id.player_play_button)
//    private var playerFileName = requireView().findViewById<ImageButton>(R.id.player_header_name)
//    private var playerHeaderName = requireView().findViewById<ImageButton>(R.id.player_header_title)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        playerSheet = view.findViewById<ConstraintLayout>(R.id.player_sheet)
//        var bottomSheetBehavior = BottomSheetBehavior.from(playerSheet!!)

        audioFileList = view.findViewById<RecyclerView>(R.id.audio_list_view)

        var audioFilePath: String = requireActivity().getExternalFilesDir("/")!!.absolutePath
        var directory: File = File(audioFilePath)
        fileList.addAll(directory.listFiles())


        audioFileList!!.setHasFixedSize(true)
        audioFileList!!.layoutManager = LinearLayoutManager(context)
        audioFileList!!.adapter = AudioListAdapter(fileList, this)



//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                }
//            }
//        })
    }

    override fun onClickListener(file: File, position: Int) {
        fileToPlay = file
        if (isPlaying) {
            stopAudio()
            playAudio(fileToPlay!!)
        } else {
            playAudio(fileToPlay!!)
        }
        Log.d("PLAY_LOG", "File playing " + file.name)
    }

    private fun playAudio(fileToPlay: File) {

        isPlaying = true
        mediaPlayer = MediaPlayer()

        mediaPlayer!!.setDataSource(fileToPlay.absolutePath)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()

    }

    private fun stopAudio() {
        isPlaying = false
        mediaPlayer!!.stop()

    }


}