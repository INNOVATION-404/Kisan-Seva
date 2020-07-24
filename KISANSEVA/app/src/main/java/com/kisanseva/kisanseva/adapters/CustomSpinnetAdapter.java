package com.kisanseva.kisanseva.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisanseva.kisanseva.R;

public class CustomSpinnetAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {
    Context context;
    int images[];
    String[] names;
    LayoutInflater inflter;
    public static final String TAG=CustomSpinnetAdapter.class.getSimpleName();

    public CustomSpinnetAdapter(Context applicationContext, int[] flags, String[] names) {
        this.context = applicationContext;
        this.images = flags;
        this.names = names;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long  getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_custom_layout, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);

        icon.setImageResource(images[i]);
        try{
            names.setText(this.names[i]);
        }
        catch (Exception ex){
            Log.e("EXCEPTION",ex.getMessage());
        }
        return view;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.e(TAG,position+" itemid");

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
