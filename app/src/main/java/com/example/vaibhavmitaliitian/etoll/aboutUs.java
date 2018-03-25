package com.example.vaibhavmitaliitian.etoll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.example.vaibhavmitaliitian.etoll.R;
import com.example.vaibhavmitaliitian.etoll.aboutAdapter;
import com.example.vaibhavmitaliitian.etoll.aboutAdapterItem;

import java.util.ArrayList;

public class aboutUs extends AppCompatActivity {

    private GridView grid;
    private aboutAdapter adapter;
    private ArrayList<aboutAdapterItem> adapterItem;
    private aboutAdapterItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        grid = (GridView) findViewById(R.id.grid1);
        adapterItem = new ArrayList<>();
        item = new aboutAdapterItem();
        item.name = "Q. Does this app work offline?";
        item.info = "A. No. The app needs a good internet connection as it needs to connect to a server to complete payments.";
        adapterItem.add(item);
        item = new aboutAdapterItem();
        item.name = "Q. What will happen if the user refuses to provide the necessary permissions";
        item.info = "A. The application continuously requests for the required permissions until the user accepts them, because location and bluetooth needs to stay live, in order for the proximity sensors to function";
        adapterItem.add(item);
        adapter = new aboutAdapter(this, R.layout.about_layout, adapterItem);
        grid.setAdapter(adapter);
    }
}
