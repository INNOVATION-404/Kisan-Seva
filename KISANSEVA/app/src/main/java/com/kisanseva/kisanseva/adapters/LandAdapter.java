package com.kisanseva.kisanseva.adapters;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisanseva.kisanseva.KisanSevaApp;
import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.entities.Land;
import com.kisanseva.kisanseva.ui.ExpenseActivity;
import com.kisanseva.kisanseva.ui.LandsActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class LandAdapter extends ArrayAdapter<Land> implements View.OnClickListener{

    private ArrayList<Land> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView landname;
        TextView soiltype;
        TextView landarea;
        TextView crops;
        ImageView info;
    }

    public LandAdapter(ArrayList<Land> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Land Land=(Land)object;

        switch (v.getId())
        {
            default:
                Log.e("Instance ",(mContext instanceof LandsActivity)+"");
                if(mContext instanceof LandsActivity){ ((LandsActivity)mContext).showDialogBox(true,Land,position); }
                Snackbar.make(v, "Land Area " +Land.getAcresofLand(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;
    public static int getDrawable(String name) {
        Context context = KisanSevaApp.getContext();
        int resourceId = context.getResources().getIdentifier(name, "drawable", KisanSevaApp.getContext().getPackageName());
        return resourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Land Land = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.soiltype = (TextView) convertView.findViewById(R.id.soiltype);
            viewHolder.landname = (TextView) convertView.findViewById(R.id.landname);
            viewHolder.landarea = (TextView) convertView.findViewById(R.id.landarea);
            viewHolder.crops = (TextView) convertView.findViewById(R.id.crops);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.soiltype.setText(Land.getSoiltype());
        viewHolder.landname.setText(Land.getLandName());
        viewHolder.landarea.setText(Land.getAcresofLand()+" "+Land.getMeasureunit());
        try{
        viewHolder.info.setImageResource(getDrawable(Land.getSoiltypeImage()));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        String str="";
        if(Land.getCrops()!=null) {
            for (String s : Land.getCrops()) {
                str = str + ",";
            }
        }
        viewHolder.crops.setText("Crops:\n"+Land.getCropsGrown());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
