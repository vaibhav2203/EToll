package com.example.vaibhavmitaliitian.etoll;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class fillForm extends AppCompatActivity {
    EditText name, email, contact, amount;
    global g;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        name = (EditText) findViewById(R.id.name_text_view);
        email = (EditText) findViewById(R.id.email_text_view);
        contact = (EditText) findViewById(R.id.phone_text_view);
        amount = (EditText) findViewById(R.id.amount_text_view);
        name.setText(sharedPreferences.getString("Name", "Guest"));
        email.setText(sharedPreferences.getString("Email", ""));
        g = (global) getApplicationContext();
        id = String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("ID","0" ));
        contact.setText(sharedPreferences.getString("Phone", ""));


    }

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            AlertDialog alertDialog = new AlertDialog.Builder(fillForm.this).create();

            @Override
            public void onSuccess(String response) {
                final String[] sa = response.split(":");
                alertDialog.setTitle("Payment Successful");
                String temp = sa[1] + "\n" + sa[3];
                alertDialog.setMessage(temp);

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        intent.putExtra("orderId", sa[1]);
                        intent.putExtra("transactionId", sa[3]);
                        intent.putExtra("isSuccess", true);
                        try {

                            Date date = Calendar.getInstance().getTime();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Balance", String.valueOf(Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Balance", "100")) + Integer.valueOf(amount.getText().toString()))).apply();
                            UpdateToll updateToll = new UpdateToll("http://shaanucomputers.com/webservice.asmx/InsertProjectTransaction?infoId=" + id + "&amount=" + amount.getText().toString() + "&boolean=0&numBoothID=0&numPaymentTransactID=" + sa[3] + "&date=" + date.toString(), getApplicationContext());

                            updateToll.execute("");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                });
                alertDialog.show();

               /* Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();*/
            }

            @Override
            public void onFailure(int code, String reason) {
                final String[] sa = reason.split(":");
                alertDialog.setTitle("Payment Failed");
                String temp = sa[1] + "\n" + sa[3];
                alertDialog.setMessage(temp);

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();

                        intent.putExtra("isSuccess", false);
                        finish();
                    }
                });
                alertDialog.show();
                /*
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            */
            }
        };
    }

    public void payAmount(View view) {

        callInstamojoPay(email.getText().toString(), contact.getText().toString(), amount.getText().toString(), "official", name.getText().toString());
    }

}
