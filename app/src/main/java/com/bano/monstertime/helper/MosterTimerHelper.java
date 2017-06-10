package com.bano.monstertime.helper;

import android.content.Context;

import com.bano.monstertime.constant.KeysContract;
import com.bano.monstertime.util.PreferencesUtils;

import java.util.UUID;

/**
 *
 * Created by Alexandre on 10/06/2017.
 */

public final class MosterTimerHelper {

    public static String getUserId(Context context){
        String userId = PreferencesUtils.getString(context, KeysContract.USER_ID_KEY);
        if(userId == null || userId.isEmpty()){
            userId = UUID.randomUUID().toString();
            PreferencesUtils.putString(context, KeysContract.USER_ID_KEY, userId);
        }

        return userId;
    }
}
