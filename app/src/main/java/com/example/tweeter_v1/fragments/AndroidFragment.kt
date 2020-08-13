package com.example.tweeter_v1

import android.content.Context
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
import java.io.*

class AndroidFragment : Fragment(), AudioListAdapter.onPlayClick {

    private var playerSheet: ConstraintLayout? = null
    private var audioFileList: RecyclerView? = null
    private var fileList: ArrayList<File> = arrayListOf<File>()
    private var audioListAdapter: AudioListAdapter? = null
    private lateinit var audioListView: ListView

    private var fileToPlay: File? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    //UI stuff
    private var playerPlayButton: ImageButton? = null
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
        return inflater.inflate(R.layout.fragment_android, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerSheet = view.findViewById<ConstraintLayout>(R.id.player_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet!!)

        audioFileList = view.findViewById<RecyclerView>(R.id.audio_list_view)


        //Getting path for all phone-recorded audio files
        var audioFilePath: String = requireActivity().getExternalFilesDir("/")!!.absolutePath
        Log.d("audio_file_path", "Audio Files stored in $audioFilePath" )

        // Bird Sound Test Data (Assets folder)
        context?.let { getAssetsFile(it, "blue_jay_1.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "blue_jay_2.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "blue_jay_3.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "osprey_1.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "osprey_2.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "osprey_3.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "cedar_waxling_1.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "cedar_waxling_2.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "cedar_waxling_3.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "great_horned_owl_1.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "great_horned_owl_2.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "great_horned_owl_3.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "tree_swallow_1.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "tree_swallow_2.wav", audioFilePath) }
        context?.let { getAssetsFile(it, "tree_swallow_3.wav", audioFilePath) }


        var directory: File = File(audioFilePath)
        fileList.addAll(directory!!.listFiles())


        audioFileList!!.setHasFixedSize(true)
        audioFileList!!.layoutManager = LinearLayoutManager(context)
        audioFileList!!.adapter = AudioListAdapter(fileList, this, requireContext())

        playerPlayButton = view.findViewById<ImageButton>(R.id.player_play_button)
        playerFileName = view.findViewById<TextView>(R.id.file_name)
        playerStatusBar = view.findViewById<TextView>(R.id.player_header_title)

        seekBar = view.findViewById(R.id.seek_bar)

        playerPlayButton!!.setOnClickListener(View.OnClickListener {View ->
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
        //Expands the audio player bottom sheet
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

        //Sets boolean that controls the playing state
        isPlaying = true

        //Initializes media player with necessary info, and starts playing
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(fileToPlay.absolutePath)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()

        //Changes image from play to pause, and updates audio player text
        playerPlayButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_pause_btn))
        playerFileName!!.text = fileToPlay.name
        playerStatusBar!!.text = "Playing"

        //When the audio finishes playing
        mediaPlayer!!.setOnCompletionListener { mediaPlayer ->
            stopAudio()
            playerStatusBar!!.text = "Finished"
            playerPlayButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_play_btn))
        }

        //Controls seek bar status
        seekBar?.max = mediaPlayer!!.duration
        seekBarHandler = Handler()
        updateRunnable()
        seekBarHandler!!.postDelayed(seekBarRunnable!!, 0)

    }

    private fun pause() {
        mediaPlayer!!.pause()
        playerPlayButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_play_btn))
        isPlaying = false
    }

    private fun resume() {
        mediaPlayer!!.start()
        playerPlayButton!!.setImageDrawable(this.activity?.resources?.getDrawable(R.drawable.player_pause_btn))
        isPlaying = true
    }

    private fun stopAudio() {
        isPlaying = false
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
        //mediaPlayer!!.release()

    }

    //Used when updating seekbar position
    private fun updateRunnable() {
        seekBarRunnable = object : Runnable {
            override fun run() {
                seekBar!!.progress = mediaPlayer!!.currentPosition
                seekBarHandler!!.postDelayed(this, 5)
            }
        }
    }





    private fun getAssetsFile(context: Context, fileName: String, filePath: String) {
        var inputStream = context.assets.open(fileName)
        var outFileNameAndPath = "$filePath/$fileName"
        var outputStream: OutputStream = FileOutputStream(outFileNameAndPath)
        val buffer: ByteArray = ByteArray(8192)
        var length: Int? = 0

        while (true) {
            length = inputStream.read(buffer)
            if (length <= 0)
                break
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

}
