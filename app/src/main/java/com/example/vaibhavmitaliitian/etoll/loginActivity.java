package com.example.vaibhavmitaliitian.etoll;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {


    private EditText user, pass;
    private Button signin, signup, fp;
    private String userid, passid;
    private ProgressDialog progress;
    private TextView resp, response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        resp = (TextView) findViewById(R.id.tvresponse);
        response = (TextView) findViewById(R.id.tvresp);
        user = (EditText) findViewById(R.id.etuser);
        pass = (EditText) findViewById(R.id.etpass);
        signup = (Button) findViewById(R.id.bsignup);
        fp = (Button) findViewById(R.id.bfp);
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

                    Intent intent = new Intent(loginActivity.this,MainActivity2.class);
                    startActivity(intent);
//                    c = 0;
//                    CheckLogin checkLogin = new CheckLogin();
//                    checkLogin.execute("");
                } else {
                    Toast.makeText(loginActivity.this, "Please enter credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
