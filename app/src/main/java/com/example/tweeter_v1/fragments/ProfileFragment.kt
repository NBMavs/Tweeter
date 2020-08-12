package com.example.tweeter_v1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tweeter_v1.R
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.manage_account.*


class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buttonChangeUsername.setOnClickListener {
            Toast.makeText(context, "Sup, dood", Toast.LENGTH_LONG).show()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


}