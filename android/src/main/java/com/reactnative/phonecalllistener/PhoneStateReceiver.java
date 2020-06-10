package com.reactnative.phonecalllistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String lastEightDigits = "";

            //ringing
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                if(incomingNumber != null && !incomingNumber.isEmpty() && !incomingNumber.equals("null")) {
                    // lastEightDigits = incomingNumber.substring(incomingNumber.length() - 8);
                    Intent i = new Intent("android.intent.action.phone_number").putExtra(
                            "phone_number", incomingNumber);
                    context.sendBroadcast(i);
                }
            }

            //answered
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Intent doVerify = new Intent("android.intent.action.verify").putExtra(
                        "verify", "yes");
                context.sendBroadcast(doVerify);
            }

            //rejectted
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Intent doVerify = new Intent("android.intent.action.verify").putExtra(
                        "verify", "yes");
                context.sendBroadcast(doVerify);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
