package com.bano.monstertime.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import com.bano.monstertime.helper.MonsterHelper
import com.bano.monstertime.model.MonsterTimer
import java.util.*

/**

 * Created by Alexandre on 10/06/2017.
 */

class MonsterService : Service() {

    private val TAG = "MonsterService"

    private val mBinder = LocalBinder()
    private var textToSpeech: TextToSpeech? = null
    private var mTextInit: Boolean = false
    private var mListener: OnMonsterListener? = null
    private var mMonsterTimerList: ArrayList<MonsterTimer>? = null
    private var mCountTimer: CountDownTimer? = null

    interface OnMonsterListener{
        fun onUpdateTimer(millisUntilFinished: Long)
        fun onTimerFinish(monsterTimer: MonsterTimer)
        fun onMonsterFinished()
    }

    override fun onCreate() {
        super.onCreate()
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status -> mTextInit = status != TextToSpeech.ERROR })
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    fun startMonster(monsterTimers: ArrayList<MonsterTimer>) {
        if(monsterTimers.isEmpty()) return
        if (!mTextInit) {
            Toast.makeText(this, "Not initialize", Toast.LENGTH_LONG).show()
            return
        }
        mMonsterTimerList = monsterTimers
        val iterator = monsterTimers.iterator()
        MonsterHelper.startMonster(this) { startMonsterTimer(iterator.next(), iterator) }
    }

    private fun startMonsterTimer(monsterTimer: MonsterTimer, iterator: MutableIterator<MonsterTimer>) {
        MonsterHelper.speak(textToSpeech, monsterTimer.name)
        mCountTimer = CountTimer(monsterTimer, iterator).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountTimer?.cancel()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
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

    inner class CountTimer(val monsterTimer: MonsterTimer, val iterator: MutableIterator<MonsterTimer>) : CountDownTimer(monsterTimer.time, 1000) {
        override fun onFinish() {
            Log.d(TAG, "0")
            if(!iterator.hasNext()) {
                mListener?.onMonsterFinished()
                stopSelf()
                return
            }
            MonsterHelper.startMonster(this@MonsterService) { startMonsterTimer(iterator.next(), iterator) }
            mListener?.onTimerFinish(monsterTimer)
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, millisUntilFinished.toString())
            if(millisUntilFinished - 1000 < 1000) {
                Handler().postDelayed({mListener?.onUpdateTimer(1000)}, 1000)
            }
            mListener?.onUpdateTimer(millisUntilFinished)
        }
    }
}
