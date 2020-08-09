package com.example.tweeter_v1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.readdbtest.*

class ReadDBTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.readdbtest)

        dbReference.child(user2!!.uid).addValueEventListener(object : ValueEventListener {
            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase Database rules. For more information on
             * securing your data, see: [ Security
 * Quickstart](https://firebase.google.com/docs/database/security/quickstart)
             *
             * @param error A description of the error that occurred
             */
            override fun onCancelled(error: DatabaseError) {
                Log.d("ValueEventListener", "Failed")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    var user5 = it.getValue(VerifyClassification.DBWrite::class.java)
                    user_name.setText(user5?.birdsType).toString()
                    user_mobile.setText(user5?.location).toString()
                }
            }
        })
    }
}
