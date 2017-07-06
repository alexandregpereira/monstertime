package com.bano.monstertime.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.bano.monstertime.R
import com.bano.monstertime.fragment.MonsterFragment
import com.bano.monstertime.model.MonsterTimer
import com.bano.monstertime.service.MonsterService

class MainActivity : AppCompatActivity() {

//    private val TAG = "MainActivity"

    private var mMonsterFragment: MonsterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMonsterFragment = supportFragmentManager.findFragmentById(R.id.fragmentMonster) as MonsterFragment
    }

}
