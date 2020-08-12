package com.example.tweeter_v1

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tweeter_v1.fragments.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.navigation_main.*

val user = Firebase.auth.currentUser

class NavigationMain: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.navigation_main )

        onBackPressedDispatcher.addCallback { this }

        val profileFragment = ProfileFragment()
        val androidFragment = AndroidFragment()
        val bookFragment = BookFragment()
        val recorderFragment = RecorderFragment()
        val statsFragment = StatsFragment()

        makeCurrentFragment( recorderFragment )

        top_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId ){
                R.id.ic_account -> makeCurrentFragment( profileFragment )
                R.id.ic_android -> makeCurrentFragment( androidFragment )
                R.id.ic_book -> makeCurrentFragment( bookFragment )
                R.id.ic_recorder -> makeCurrentFragment( recorderFragment )
                R.id.ic_stats -> makeCurrentFragment( statsFragment )
            }
            true
        }


    }

    private fun makeCurrentFragment( fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace( R.id.fl_wrapper, fragment )
            commit()
        }

}