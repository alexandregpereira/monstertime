package com.bano.monstertime.helper

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.speech.tts.TextToSpeech
import com.bano.monstertime.R
import com.bano.monstertime.constant.KeysContract
import com.bano.monstertime.model.MonsterTimer
import com.bano.monstertime.util.PreferencesUtils
import java.util.*

/**

 * Created by Alexandre on 10/06/2017.
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

    fun speak(textToSpeech: TextToSpeech, speak: String) {
        if (Build.VERSION.SDK_INT >= 21)
            textToSpeech.speak(speak, TextToSpeech.QUEUE_FLUSH, null, speak)
        else
            textToSpeech.speak(speak, TextToSpeech.QUEUE_FLUSH, null)
    }

    fun startMonster(context: Context, textToSpeech: TextToSpeech?, items: ArrayList<MonsterTimer>) {
        val mp = MediaPlayer.create(context, R.raw.evil_laugh)
        mp.start()


    }
}
