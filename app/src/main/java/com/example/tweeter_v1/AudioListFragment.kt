package com.example.tweeter_v1


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File

class AudioListFragment : Fragment(), AudioListAdapter.onPlayClick {

    private var playerSheet: ConstraintLayout? = null
    private var audioFileList: RecyclerView? = null
    private var fileList: ArrayList<File> = arrayListOf<File>()
    private var audioListAdapter: AudioListAdapter? = null
    private lateinit var audioListView: ListView

    private var fileToPlay: File? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    //UI stuff
    private var playButton: ImageButton? = null
    private var playerFileName: TextView? = null
    private var playerStatusBar: TextView? = null
    private var seekBar: SeekBar? = null
    private var seekBarHandler: Handler? = null
    private var seekBarRunnable: Runnable? = null
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null


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

        playerSheet = view.findViewById<ConstraintLayout>(R.id.player_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet!!)

        audioFileList = view.findViewById<RecyclerView>(R.id.audio_list_view)

        //Getting path for all phone-recorded audio files
//        var audioFilePath: String = requireActivity().getExternalFilesDir("/")!!.absolutePath
//        var directory: File = File(audioFilePath)
//        fileList.addAll(directory.listFiles())

        //Getting path for trial files
        var audioFilePath: String = "/sdcard/AudioData/"
        var directory: File = File(audioFilePath)
        fileList.addAll(directory.listFiles())



        audioFileList!!.setHasFixedSize(true)
        audioFileList!!.layoutManager = LinearLayoutManager(context)
        audioFileList!!.adapter = AudioListAdapter(fileList, this, requireContext())

        playButton = view.findViewById<ImageButton>(R.id.player_play_button)
        playerFileName = view.findViewById<TextView>(R.id.file_name)
        playerStatusBar = view.findViewById<TextView>(R.id.player_header_title)

        seekBar = view.findViewById(R.id.seek_bar)

        playButton!!.setOnClickListener(View.OnClickListener {View ->
            if (isPlaying) {
                pause()
            } else {
                if (fileToPlay != null) {
                    resume()
                }
            }
        })




        (bottomSheetBehavior as BottomSheetBehavior<ConstraintLayout>).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    (bottomSheetBehavior as BottomSheetBehavior<ConstraintLayout>).state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
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
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

        isPlaying = true
        mediaPlayer = MediaPlayer()

        mediaPlayer!!.setDataSource(fileToPlay.absolutePath)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()

        playButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_pause_btn))
        playerFileName!!.setText(fileToPlay.getName())
        playerStatusBar!!.setText("Playing")

        mediaPlayer!!.setOnCompletionListener { mediaPlayer ->
            stopAudio()
            playerStatusBar!!.text = "Finished"
            playButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_play_btn))
        }

        seekBar?.max = mediaPlayer!!.duration
        seekBarHandler = Handler()
        updateRunnable()
        seekBarHandler!!.postDelayed(seekBarRunnable!!, 0)

    }

    private fun pause() {
        mediaPlayer!!.pause()
        playButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_play_btn))
        isPlaying = false
    }

    private fun resume() {
        mediaPlayer!!.start()
        playButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_pause_btn))
        isPlaying = true
    }

    private fun updateRunnable() {
        seekBarRunnable = object : Runnable {
            override fun run() {
                seekBar!!.progress = mediaPlayer!!.currentPosition
                seekBarHandler!!.postDelayed(this, 5)
            }
        }
    }

    private fun stopAudio() {
        isPlaying = false
        mediaPlayer!!.stop()

    }


}

