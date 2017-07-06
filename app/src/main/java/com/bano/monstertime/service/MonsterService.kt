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
import com.bano.monstertime.R
import com.bano.monstertime.constant.KeysContract
import com.bano.monstertime.helper.MonsterHelper
import com.bano.monstertime.model.MonsterTimer
import com.bano.monstertime.viewmodel.MonsterTimerViewModel

/**

 * Created by Alexandre on 10/06/2017.
 */

class MonsterService : Service() {

    private val mBinder = LocalBinder()
    private val mHandler = Handler()

    private var mTextToSpeech: TextToSpeech? = null
    private var mTextInit: Boolean = false
    private var mCountTimer: CountDownTimer? = null
    private var mModel: MonsterTimerViewModel? = null

    override fun onCreate() {
        super.onCreate()
        mTextToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status -> mTextInit = status != TextToSpeech.ERROR })
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(KeysContract.TAG, "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    fun startMonster(model: MonsterTimerViewModel) {
        Log.d(KeysContract.TAG, "startMonster")
        start(this)
        mModel = model
        val monsterTimerList = model.monsterTimersObservable.value ?: return
        if(monsterTimerList.isEmpty()) return
        if (!mTextInit) {
            Toast.makeText(this, "Not initialize", Toast.LENGTH_LONG).show()
            return
        }

        val iterator = monsterTimerList.iterator()
        MonsterHelper.speak(this, R.raw.evil_laugh) { startMonsterTimer(iterator.next(), iterator) }
    }

    private fun startMonsterTimer(monsterTimer: MonsterTimer, iterator: Iterator<MonsterTimer>) {
        MonsterHelper.speak(mTextToSpeech, monsterTimer.name, mHandler) { mCountTimer = CountTimer(monsterTimer, iterator).start() }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountTimer?.cancel()
        mTextToSpeech?.stop()
        mTextToSpeech?.shutdown()
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

    inner class CountTimer(monsterTimer: MonsterTimer, val iterator: Iterator<MonsterTimer>) : CountDownTimer(monsterTimer.time, 1000) {
        private var mCount = monsterTimer.time

        override fun onFinish() {
            Log.d(KeysContract.TAG, "0")
            mModel?.timerObservable?.value = "0"
            if(!iterator.hasNext()) {
                MonsterHelper.speak(this@MonsterService, R.raw.evil_laugh) {}
                stopSelf()
                return
            }

            startMonsterTimer(iterator.next(), iterator)
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d(KeysContract.TAG, millisUntilFinished.toString())
            if(millisUntilFinished - 1000 < 1000) {
                mHandler.postDelayed({ mModel?.timerObservable?.value = "1" }, 1000)
            }
            else if(millisUntilFinished in 5000..6000){
                MonsterHelper.speak(this@MonsterService, R.raw.countdown_five_seconds) {}
            }
            mCount -= 1000
            mModel?.timerObservable?.value = (mCount / 1000).toString()
        }
    }
}
