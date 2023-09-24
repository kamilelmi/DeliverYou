package com.example.deliveryou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] type;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] type) {
        this.context = applicationContext;
        this.type = type;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return type.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_layout, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(type[i]);
        return view;
    }
}