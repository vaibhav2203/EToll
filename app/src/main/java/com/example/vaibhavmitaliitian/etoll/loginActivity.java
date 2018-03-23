package com.example.vaibhavmitaliitian.etoll;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class loginActivity extends AppCompatActivity {


    private EditText user, pass;
    private Button signin, signup, fp;
    private String userid, passid;
    private ProgressDialog progress;
    private XMLParser parser;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    private TextView resp, response;
    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        edit = preferences.edit();
        progress.setMessage("Please Wait...");
        edit.putString("Name","Guest");
        edit.putString("Email","");
        edit.putString("Phone","");
        edit.putString("Address","");
        edit.putString("Vehicle","");
        edit.apply();
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        resp = (TextView) findViewById(R.id.tvresponse);
        response = (TextView) findViewById(R.id.tvresp);
        user = (EditText) findViewById(R.id.etuser);
        pass = (EditText) findViewById(R.id.etpass);
        signup = (Button) findViewById(R.id.bsignup);
        fp = (Button) findViewById(R.id.bfp);
        parser = new XMLParser();
        signin = (Button) findViewById(R.id.bsignin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = user.getText().toString();
                passid = pass.getText().toString();
                if (!userid.equals("")) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    Intent intent = new Intent(loginActivity.this, MainActivity2.class);
                    startActivity(intent);
                    try {
                        url = new URL("http://shaanucomputers.com/webservice.asmx?ShowDetails?txtUserName="+user.getText().toString()+"&txtPassword=" + pass.getText().toString());
                        LongOperation checkLogin = new LongOperation();
                        checkLogin.execute("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }catch (Error e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(loginActivity.this, "Please enter credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private class LongOperation extends AsyncTask<String, Void, Void> {
        String xml;
        NodeList nl;

        protected void onPreExecute() {

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
                nl = parser.parseXML(xml, "ProjectInfo");
                if (nl.getLength() == 0) {
                    Toast.makeText(getApplicationContext(), "Failure,Please try again", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < nl.getLength(); i++) {

                        edit.putString("Name",parser.getValue((Element) nl.item(i), "Name"));
                        edit.putString("Name",parser.getValue((Element) nl.item(i), "Name"));
                        edit.putString("Address",parser.getValue((Element) nl.item(i), "Address"));
                        edit.putString("ID",parser.getValue((Element) nl.item(i), "InfoID"));
                        edit.putString("Email",parser.getValue((Element) nl.item(i), "Email"));
                        edit.putString("Phone",parser.getValue((Element) nl.item(i), "Mobile"));
                        edit.putString("Vehicle",parser.getValue((Element) nl.item(i), "VehicleNo"));
                        edit.apply();
                        Intent intent = new Intent(loginActivity.this,MainActivity2.class);
                        finish();
                        startActivity(intent);
                    }
                }
                Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(loginActivity.this, "No data returned from server", Toast.LENGTH_LONG).show();
            }
        }
    }

}
