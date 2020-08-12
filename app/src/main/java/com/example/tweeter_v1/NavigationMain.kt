package com.example.tweeter_v1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tweeter_v1.fragments.*
import kotlinx.android.synthetic.main.navigation_main.*

class NavigationMain: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.navigation_main )

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