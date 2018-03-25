package com.example.vaibhavmitaliitian.etoll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pranjal on 24/03/2018.
 */

public class Adapter2 extends ArrayAdapter<AdapterItem2> {
    private Context mContext;
    private int resourceId;
    private ArrayList<AdapterItem2> data = new ArrayList<>();


    public Adapter2(Context context, int layoutResourceId, ArrayList<AdapterItem2> data) {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        Adapter2.ViewHolder holder = null;
        try {

            if (itemView == null) {
                final LayoutInflater layoutInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = layoutInflater.inflate(resourceId, parent, false);
                holder = new Adapter2.ViewHolder();
                holder.date = (TextView) itemView.findViewById(R.id.date);
                holder.orderID = (TextView) itemView.findViewById(R.id.orderID);
                itemView.setTag(holder);
            } else {
                holder = (Adapter2.ViewHolder) itemView.getTag();
            }
            AdapterItem2 desc = data.get(position);
            holder.date.setText(desc.date.toString());
            holder.orderID.setText(String.valueOf(desc.orderID));
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return itemView;
    }

    public class ViewHolder {
        TextView orderID,date;
    }

}

