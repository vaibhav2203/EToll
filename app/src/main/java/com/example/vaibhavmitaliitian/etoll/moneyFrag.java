package com.example.vaibhavmitaliitian.etoll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;


public class moneyFrag extends Fragment {


    private RelativeLayout layout;
    private  Button addButton;

    public moneyFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = (RelativeLayout)inflater.inflate(R.layout.fragment_money, container, false);
        addButton = (Button) layout.findViewById(R.id.btn_add) ;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),fillForm.class) ;
                startActivity(intent)   ;
            }
        });
        return layout;
    }


}
