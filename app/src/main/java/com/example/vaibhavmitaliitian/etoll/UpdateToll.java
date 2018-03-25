package com.example.vaibhavmitaliitian.etoll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

/**
 * Created by Vaibhav Mital IITian on 24-03-2018.
 */

public class UpdateToll extends AsyncTask<String, Void, Void> {
    String xml;
    NodeList nl;
    XMLParser parser;
    Context context;
    
    UpdateToll(String url, final Context context) throws MalformedURLException {
        this.url = new URL(url);
        parser = new XMLParser();
        this.context = context;
    }
    
    protected void onPreExecute() {

    }

    private URL url;
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
    }
}
