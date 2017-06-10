package com.bano.monstertime.activity

import android.app.Application

import com.google.firebase.database.FirebaseDatabase

/**

 * Created by Alexandre on 10/06/2017.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
