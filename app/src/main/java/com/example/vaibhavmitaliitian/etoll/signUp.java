package com.example.vaibhavmitaliitian.etoll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import java.net.URL;

public class signUp extends AppCompatActivity {

    private EditText name, email, pass, repass, phone, add, vehicle;
    private Button submit;
    private XMLParser parser;
    private URL url;
    private TextView resp;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        parser = new XMLParser();
        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);
        repass = (EditText) findViewById(R.id.repass);
        phone = (EditText) findViewById(R.id.phone);
        resp = (TextView) findViewById(R.id.response);
        edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        add = (EditText) findViewById(R.id.address);
        vehicle = (EditText) findViewById(R.id.vehicleNo);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("") || !repass.getText().toString().equals(repass.getText().toString())) {
                    resp.setText("Please enter same password");
                    return;
                }
                try {
                    url = new URL("http://shaanucomputers.com/webservice.asmx/InsertProjectInfo?txtName=" + name.getText().toString() + "&txtEmail=" + email.getText().toString() + "&txtMobile=" + phone.getText().toString() + "&txtGender=male&txtAddress=" + add.getText().toString() + "&txtVehicleNumber=" + vehicle.getText().toString() + "&numBalance=0&password=" + pass.getText().toString());
                    LongOperation operation = new LongOperation();
                    operation.execute("");
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error e) {
                    e.printStackTrace();
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
                nl = parser.parseXML(xml, "OneLinerSuccess");
                if (nl.getLength() == 0) {
                    Toast.makeText(getApplicationContext(), "Failure,Please try again", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < nl.getLength(); i++) {
                        edit.putString("Name", name.getText().toString());
                        edit.putString("Address", add.getText().toString());
                        String id = parser.getValue((Element) nl.item(i), "statuscode");
                        edit.putString("ID", id);
                        global g = (global) getApplicationContext();
                        g.id = id;
                        edit.putString("Email", email.getText().toString());
                        edit.putString("Phone", phone.getText().toString());
                        edit.putString("Vehicle", vehicle.getText().toString());
                        edit.apply();
                        Intent intent = new Intent(signUp.this, MainActivity2.class);
                        finish();
                        startActivity(intent);
                    }
                }
                Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(signUp.this, "No data returned from server", Toast.LENGTH_LONG).show();
            }
        }
    }
}
