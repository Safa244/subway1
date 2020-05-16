package com.example.subway_two;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.TextView;

import org.json.JSONException;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<JSONObject>{

    int vg;

    ArrayList<JSONObject> list;

    Context context;

    public ListAdapter(Context context, int vg, int id, ArrayList<JSONObject> list){

        super(context,vg, id,list);

        this.context=context;

        this.vg=vg;

        this.list=list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        TextView txtId=(TextView)itemView.findViewById(R.id.driver_name);

        try {

            txtId.setText(list.get(position).getString("user"));




        } catch (JSONException e) {

            e.printStackTrace();

        }



        return itemView;

    }

}
