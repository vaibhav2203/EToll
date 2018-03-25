package com.example.vaibhavmitaliitian.etoll;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import in.juspay.godel.ui.uber.FloatingActionButton;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

import static android.app.Activity.RESULT_OK;


public class moneyFrag extends Fragment {

    private final int resultId =  236   ;
    private LinearLayout layout;
    private GridView grid;
    private XMLParser parser;
    TextView balanceView ;
    URL url;
    private ProgressBar progressBar;
    private Adapter2 adapter;
    private ArrayList<AdapterItem2> item;
    private AdapterItem2 items;
    private  Button addButton;

    public moneyFrag() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
         balanceView.setText(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Balance","100"));
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        layout = (LinearLayout)inflater.inflate(R.layout.fragment_money, container, false);
        balanceView = (TextView)layout.findViewById(R.id.amount_text_view) ;
        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) layout.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),fillForm.class) ;
                startActivityForResult(intent,resultId);
            }
        });
        grid = (GridView) layout.findViewById(R.id.grid);
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        items = new AdapterItem2();
        items.date = "Date";
        items.orderID = "Order ID";
        item = new ArrayList<>();
        item.add(items);
        adapter = new Adapter2(getActivity(),R.layout.item,item);
        grid.setAdapter(adapter);
        global g = (global) getActivity().getApplicationContext();
        parser = new XMLParser();
        try{
            url = new URL("http://shaanucomputers.com/webservice.asmx/ShowProjectTransactionDataByID?id=" +g.id);
            //url = new URL("http://shaanucomputers.com/webservice.asmx/ShowProjectTransaction");
            LongOperation operation = new LongOperation();
            operation.execute("");
        }catch (Exception e){
            e.printStackTrace();
        }
        return layout;
    }
    private class LongOperation extends AsyncTask<String, Void, Void> {
        String xml;
        NodeList nl;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(String... urls) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream _is;
                if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    _is = conn.getInputStream();
                } else {
                    _is = conn.getErrorStream();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(_is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "");
                }
                xml = sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            try {
                nl = parser.parseXML(xml, "ProjectTransaction");
                if (nl.getLength() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Failure,Please try again", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < nl.getLength(); i++) {
                        if(parser.getValue((Element) nl.item(i), "Bool").equals("1")){
                            continue;
                        }
                        items = new AdapterItem2();
                        items.date = String.valueOf(parser.getValue((Element) nl.item(i), "date"));
                        items.orderID= String.valueOf(parser.getValue((Element) nl.item(i), "TransactionID"));
                        item.add(items);
                    }
                    adapter.notifyDataSetChanged();
                    global g = (global) getActivity().getApplicationContext();
                }
                Toast.makeText(getActivity().getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                //getFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getActivity(), "No data returned from server", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case resultId :
               if( resultCode == RESULT_OK && data.getBooleanExtra("isSuccess",false) ){
                   String orderId = data.getStringExtra("orderId") ;
                   String transactionId = data.getStringExtra("transactionId") ;
               }

            default:
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
