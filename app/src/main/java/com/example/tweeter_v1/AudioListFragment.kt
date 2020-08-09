package com.example.tweeter_v1

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import layout.AudioListAdapter
import java.io.File

class AudioListFragment : Fragment() {

    private var playerSheet: ConstraintLayout? = null
    private var audioFileList: RecyclerView? = null
    private var fileList: Array<File> = emptyArray()
    private var audioListAdapter: AudioListAdapter? = null
    private lateinit var audioListView: ListView


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
        var bottomSheetBehavior = BottomSheetBehavior.from(playerSheet!!)

        audioFileList = view.findViewById<RecyclerView>(R.id.audio_list_view)

        var audioFilePath: String = requireActivity().getExternalFilesDir("/")!!.absolutePath
        var directory: File = File(audioFilePath)
        fileList = directory.listFiles()

//        audioListAdapter = AudioListAdapter(fileList)
        audioFileList!!.setHasFixedSize(true)
        audioFileList!!.layoutManager = LinearLayoutManager(context)
        audioFileList!!.adapter = AudioListAdapter(fileList)



        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }
}