package com.asus.voltagedetectservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VoltageDetectService extends Service {
    private final String TAG = "VoltageDetectService";
    private final String catAdcNode = "cat /sys/bus/iio/devices/iio:device0/in_voltage2_raw";
    //private final String catAdcNode = "cat /sys/devices/platform/ff100000.saradc/iio:device0/in_voltage2_raw";
    private static final String WARNING_ACTION = "com.asus.voltagedetectservice.WARNING_ACTION";
    private boolean detect_on = true;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                while (detect_on) {
                    try {
                        InputStream adcValIstream = Runtime.getRuntime().exec(catAdcNode).getInputStream();
                        InputStreamReader adcValIstreamReader = new InputStreamReader(adcValIstream);
                        BufferedReader adcValBufReader = new BufferedReader(adcValIstreamReader);
                        String adcValString = adcValBufReader.readLine();
                        //Log.i(TAG, "VoltageDetectService adcValString: " + adcValString);

                        int adcValue = Integer.parseInt(adcValString);
                        //Log.i(TAG, "adcValue: " + adcValue);
                        if ( (adcValue+16) < 718 ) { //4.65v => 4.65*((82/302)*1023)/1.8 = 718, offset 0.1v => 0.1*((82/302)*1023)/1.8 = 16
                            Intent intent = new Intent();
                            intent.setAction(WARNING_ACTION);
                            intent.setFlags(Intent.FLAG_RECEIVER_INCLUDE_BACKGROUND);
                            intent.setPackage("com.asus.voltagedetectservice");
                            sendBroadcastAsUser(intent, new UserHandle(UserHandle.USER_CURRENT));
                            //sendBroadcast(intent);
                            //Log.i(TAG, "sendBroadcast");
                            adcValBufReader.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error detail", e);
                    }

                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Error detail", e);
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
