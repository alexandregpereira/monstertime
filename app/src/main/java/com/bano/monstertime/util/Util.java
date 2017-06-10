package com.bano.monstertime.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 *
 * Created by Alexandre on 10/06/2017.
 */

public class Util {

    public static String getImei(Context context){
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        String imei = telManager.getDeviceId();
        if(imei == null) {
            tmDevice = "0";
            tmSerial = "" + telManager.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            return deviceUuid.toString();
        }
        else{
            return imei;
        }
    }
}
