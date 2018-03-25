package com.example.vaibhavmitaliitian.etoll;

import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Element;

public class profileActivity extends AppCompatActivity {
   TextView name , password,sex , address,vehicle,email,phone ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView) findViewById(R.id.profile_name) ;
        password =  (TextView) findViewById(R.id.profile_password) ;
        sex = (TextView) findViewById(R.id.profile_sex) ;
        address = (TextView) findViewById(R.id.profile_address) ;
        vehicle = (TextView) findViewById(R.id.profile_vehicle) ;
        email = (TextView) findViewById(R.id.profile_email) ;
        phone = (TextView) findViewById(R.id.profile_phone) ;

        name.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Name","Pranjal"));
        password.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Password","*********"));
        sex.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Sex","Male"));
        address.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Address","Street"));
        vehicle.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Vehicle","UP07 1234"));
        email.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Email","p@gmail.com"));
        phone.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Phone","9798990890"));



    }



}
