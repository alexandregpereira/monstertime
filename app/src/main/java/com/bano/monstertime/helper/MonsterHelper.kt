package com.bano.monstertime.helper

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.annotation.RawRes
import com.bano.monstertime.constant.KeysContract
import com.bano.monstertime.model.MonsterTimer
import com.bano.monstertime.util.PreferencesUtils
import java.util.*

/**
 * Helper class for Monster classes.
 */

object MonsterHelper {

    fun getUserId(context: Context): String {
        var userId: String? = PreferencesUtils.getString(context, KeysContract.USER_ID_KEY)
        if (userId == null || userId.isEmpty()) {
            userId = UUID.randomUUID().toString()
            PreferencesUtils.putString(context, KeysContract.USER_ID_KEY, userId)
        }

        return userId
    }

    fun speak(textToSpeech: TextToSpeech?, speak: String?, handler: Handler, finish: () -> Unit) {
        if(speak == null) return
        val listener = CompletionListener(handler, finish)
        textToSpeech?.setOnUtteranceProgressListener(listener)
        if (Build.VERSION.SDK_INT >= 21)
            textToSpeech?.speak(speak, TextToSpeech.QUEUE_FLUSH, null, speak)
        else
            textToSpeech?.speak(speak, TextToSpeech.QUEUE_FLUSH, null)
    }

    fun speak(context: Context, @RawRes sound: Int, finish: () -> Unit) {
        val mp = MediaPlayer.create(context, sound)
        mp.setOnCompletionListener {
            mp.release()
            finish()
        }
        mp.start()
    }

    fun countTotalTimerInSeconds(monsterTimers: List<MonsterTimer>): Int{
        var count = 0
        monsterTimers.forEach {
            count += (it.time / 1000).toInt()
        }

        return count;
    }

    private class CompletionListener(val handler: Handler, val finish: () -> Unit): UtteranceProgressListener(){
        override fun onDone(p0: String?) {
            handler.post { finish() }
        }

        override fun onError(p0: String?) {
        }

        override fun onStart(p0: String?) {

        }
    }
}
