package com.example.vaibhavmitaliitian.etoll;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Vaibhav Mital IITian on 23-03-2018.
 */


public class global extends Application implements TextToSpeech.OnInitListener {
    private BeaconManager beaconManager;
    private TextToSpeech myTTS;
    String balance = "Rs 100",id;
    String words, amount, numBoothID;
    com.example.vaibhavmitaliitian.etoll.DataSave data = new com.example.vaibhavmitaliitian.etoll.DataSave();

    public void speakWords(String speech) {
        words = speech;
        //speak straight away
        try {
            myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        } catch (Error e) {
            Toast.makeText(getApplicationContext(), "Sorry we are unable to generate response because of some internal errors.", Toast.LENGTH_LONG).show();
        }
    }

    private final int resultId = 236;

    @Override
    public void onCreate() {
        super.onCreate();
        EstimoteSDK.initialize(getApplicationContext(), "a-6k7", "20f5b48bd916fb07af88590a4669e1cf");
        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {

            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if (beacons.get(0).getMinor() == 49999) {
                    amount = "10";
                    numBoothID = "1";
                } else {
                    amount = "20";
                    numBoothID = "2";
                }
                try {

                    Date date = Calendar.getInstance().getTime();
                    balance = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Balance","100") ;
                    //balance = balance.substring(3) ;
                    balance  = String.valueOf(Integer.valueOf(balance) - Integer.valueOf(amount));
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Balance",balance).apply();
                    UpdateToll updateToll = new UpdateToll("http://shaanucomputers.com/webservice.asmx/InsertProjectTransaction?infoId=" + id + "&amount=" + amount + "&boolean=1&numBoothID=" + numBoothID + "&numPaymentTransactID=0&date=" + date.toString(), getApplicationContext());

                    updateToll.execute("");

                     Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                showNotification("BYe", "BYE");
                Toast.makeText(getApplicationContext(),"Exit",Toast.LENGTH_LONG).show();
            }
        });


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion("monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null));
            }
        });
    }


    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, com.example.vaibhavmitaliitian.etoll.MainActivity2.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, notification);
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                myTTS.setLanguage(Locale.US);
                speakWords(words);
            }
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }

    }
}
