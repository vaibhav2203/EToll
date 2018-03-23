package com.example.vaibhavmitaliitian.etoll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {

    private TextView mTextMessage;
    private BeaconRegion region;
    private BeaconManager beaconManager;
    private global Global;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout,new moneyFrag()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout,new HistoryFrag()).commit();
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout,new nearbyTollFrag()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mTextMessage = (TextView) findViewById(R.id.message);
        beaconManager = new BeaconManager(this);
        final global application = (global) getApplicationContext();

        region = new BeaconRegion("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {

            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if (!beacons.isEmpty()) {
                    Beacon nearestBeacon = beacons.get(0);
                    application.showNotification("data sent", nearestBeacon.getMajor() + "h" + nearestBeacon.getMinor() + "f" + nearestBeacon.getRssi());
                }
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        update();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void update() {
        Global = (global) getApplicationContext();
        File defaultFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/E Toll");
        try {
            if (!defaultFile.exists()) {
                defaultFile.mkdirs();
            }
            global g = (global) getApplicationContext();
            String filename = "Data.ser";
            FileInputStream file = new FileInputStream(new File(defaultFile + File.separator, filename));
            File f = new File(defaultFile, filename);
            if (f.exists()) {
                ObjectInputStream in = new ObjectInputStream(file);
                DataSave data = (DataSave) in.readObject();
                in.close();
                file.close();
                Global.data = data;
            }
        } catch (Exception e) {
            enablePermission.startInstalledAppDetailsActivity(MainActivity2.this);
            Toast.makeText(this, "Please enable permissions", Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager = new BeaconManager(this);
        region = new BeaconRegion("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.stopRanging(region);
    }


}
