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

class MainActivity : AppCompatActivity(), ServiceConnection, MonsterFragment.OnMonsterListener, MonsterService.OnMonsterListener {

//    private val TAG = "MainActivity"

    private var mService: MonsterService? = null
    private var mBound: Boolean = false
    private var mMonsterFragment: MonsterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMonsterFragment = supportFragmentManager.findFragmentById(R.id.fragmentMonster) as MonsterFragment
        MonsterService.start(this)
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        val intent = Intent(this, MonsterService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (mBound) {
            unbindService(this)
            mBound = false
        }
    }

    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
        val binder = iBinder as MonsterService.LocalBinder
        mService = binder.service
        mBound = true
        mService?.setMonsterListener(this)
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
        mBound = false
    }

    override fun onMonsterStarted(monsterList : ArrayList<MonsterTimer>) {
        mService?.startMonster(monsterList)
    }

    override fun onUpdateTimer(millisUntilFinished: Long) {
        mMonsterFragment?.updateTimer(millisUntilFinished)
    }

    override fun onTimerFinish(monsterTimer: MonsterTimer) {
        //TODO Implement
    }

    override fun onMonsterFinished() {
        //TODO IMPLEMENT
    }
}
