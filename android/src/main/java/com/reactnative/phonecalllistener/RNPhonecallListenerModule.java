package com.reactnative.phonecalllistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class RNPhonecallListenerModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    private final static String EVENT_PHONECALL_RECEIVED = "EVENT_PHONECALL_RECEIVED";

    public RNPhonecallListenerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    private void destroy() { }

    @Override
    public String getName() {
        return "RNPhonecallListener";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("EVENT_PHONECALL_RECEIVED", EVENT_PHONECALL_RECEIVED);
        return constants;
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        this.destroy();
    }

    // ----------------------------------------------------------------------------------
    // PHONE CALL LISTENER

    private void startListenForPhoneCall() {
        IntentFilter filter = new IntentFilter("android.intent.action.phone_number");
        reactContext.registerReceiver(phoneNumberReceiver, filter);
    }

    private void stopListenForPhoneCall() {
        reactContext.unregisterReceiver(phoneNumberReceiver);
    }

    private BroadcastReceiver phoneNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String phone_number = intent.getStringExtra("phone_number");
            if(phone_number != null && !phone_number.isEmpty() && !phone_number.equals("null")) {
                sendEvent(EVENT_PHONECALL_RECEIVED, phone_number);
            }
        }
    };

    // --------------------------------------------------------------------------------------------- -
    // REACT NATIVE METHODS

    @ReactMethod
    public void enable(final Promise promise) {
        this.startListenForPhoneCall();
        promise.resolve(null);
    }

    @ReactMethod
    public void disable(final Promise promise) {
        this.stopListenForPhoneCall();
        promise.resolve(null);
    }

    // --------------------------------------------------------------------------------------------- -
    // HELPERS

    private void sendEvent(String eventName, @Nullable Object params) {
        this.getReactApplicationContext()
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }
}

