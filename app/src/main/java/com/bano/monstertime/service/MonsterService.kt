package com.bano.monstertime.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.bano.monstertime.helper.MonsterHelper
import com.bano.monstertime.model.MonsterTimer
import java.util.*

/**

 * Created by Alexandre on 10/06/2017.
 */

class MonsterService : Service() {

    private val mBinder = LocalBinder()
    private var textToSpeech: TextToSpeech? = null
    private var mTextInit: Boolean = false
    private var mListener: OnMonsterListener? = null
    private var mMonsterTimerList: ArrayList<MonsterTimer>? = null

    interface OnMonsterListener

    override fun onCreate() {
        super.onCreate()
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status -> mTextInit = status != TextToSpeech.ERROR })
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    fun startMonster(monsterTimers: ArrayList<MonsterTimer>) {
        if (!mTextInit) {
            Toast.makeText(this, "Not initialize", Toast.LENGTH_LONG).show()
            return
        }

        MonsterHelper.startMonster(this, textToSpeech, monsterTimers)
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    fun setMonsterTimerList(monsterTimerList: ArrayList<MonsterTimer>) {
        mMonsterTimerList = monsterTimerList
    }

    fun setMonsterListener(listener: OnMonsterListener) {
        mListener = listener
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        val service: MonsterService
            get() = this@MonsterService
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MonsterService::class.java)
            context.startService(intent)
        }
    }
}
