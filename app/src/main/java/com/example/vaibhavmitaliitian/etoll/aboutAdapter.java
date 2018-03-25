package com.example.vaibhavmitaliitian.etoll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Vaibhav Mital IITian on 24-03-2018.
 */

public class aboutAdapter extends ArrayAdapter<aboutAdapterItem> {
    private Context mContext;
    private int resourceId;
    private ArrayList<aboutAdapterItem> data = new ArrayList<>();


    public aboutAdapter(Context context, int layoutResourceId, ArrayList<aboutAdapterItem> data) {
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
                holder.name= (TextView) itemView.findViewById(R.id.name);
                holder.info= (TextView) itemView.findViewById(R.id.info);
                itemView.setTag(holder);
            } else {
                holder = (aboutAdapter.ViewHolder) itemView.getTag();
            }
            aboutAdapterItem desc = data.get(position);
            holder.name.setText(desc.name.toString());
            holder.info.setText(String.valueOf(desc.info));
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return itemView;
    }

    public class ViewHolder {
        TextView name,info;
    }

}

