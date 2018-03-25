package com.example.vaibhavmitaliitian.etoll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vaibhav Mital IITian on 23-03-2018.
 */

public class GridViewAdapterPayment extends ArrayAdapter<AdapterItem> {
    private Context mContext;
    private int resourceId;
    private ArrayList<AdapterItem> data = new ArrayList<>();


    public GridViewAdapterPayment(Context context, int layoutResourceId, ArrayList<AdapterItem> data) {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;
        try {

            if (itemView == null) {
                final LayoutInflater layoutInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = layoutInflater.inflate(resourceId, parent, false);
                holder = new ViewHolder();
                holder.date = (TextView) itemView.findViewById(R.id.date);
                holder.transactionID = (TextView) itemView.findViewById(R.id.transactionID);
                holder.toll = (TextView) itemView.findViewById(R.id.toll);
                holder.money = (TextView) itemView.findViewById(R.id.money);
                itemView.setTag(holder);
            } else {
                holder = (GridViewAdapterPayment.ViewHolder) itemView.getTag();
            }
            AdapterItem desc = data.get(position);
            holder.date.setText(desc.date.toString());
            holder.money.setText(String.valueOf(desc.money));
            holder.transactionID.setText(String.valueOf(desc.transactionID));
            holder.toll.setText(desc.toll);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return itemView;
    }

    public class ViewHolder {
        TextView date, money, toll,transactionID;
    }

}
