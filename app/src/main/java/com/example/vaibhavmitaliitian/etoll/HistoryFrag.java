package com.example.vaibhavmitaliitian.etoll;


import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFrag extends Fragment {


    private RelativeLayout layout;
    private GridView grid;
    private AdapterItem item;
    private ArrayList<AdapterItem> data;
    private URL url;
    private XMLParser parser;
    private GridViewAdapter adapter;
    private ProgressBar progressBar;

    public HistoryFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_history, container, false);
        global g = (global) getActivity().getApplicationContext();
        data = g.data.list;
        grid = (GridView) layout.findViewById(R.id.gridHistory);
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        data = new ArrayList<>();
        item = new AdapterItem();
        item.money = "Money Paid";
        item.transactionID = "Transaction ID";
        item.toll = "At TOLL";
        item.date = "Date";
        parser = new XMLParser();
        adapter = new GridViewAdapter(getActivity(), R.layout.item_layout, data);
        try {
            Integer id = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("ID",0);
            url = new URL("http://shaanucomputers.com/webservice.asmx?ShowProjectTransactionDataByID?id=" + String.valueOf(id));
            LongOperation operation = new LongOperation();
            operation.execute("");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
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
                        item = new AdapterItem();
                        item.money = String.valueOf(parser.getValue((Element) nl.item(i), "Amount"));
                        item.date = "hello";
                        //item.date = String.valueOf(parser.getValue((Element) nl.item(i), "FieldName"));
                        item.toll = String.valueOf(parser.getValue((Element) nl.item(i), "BoothID"));
                        item.transactionID = String.valueOf(parser.getValue((Element) nl.item(i), "TransactionID"));
                        data.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    global g = (global) getActivity().getApplicationContext();
                    g.data.list = data;
                }
                Toast.makeText(getActivity().getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No data returned from server", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

}
