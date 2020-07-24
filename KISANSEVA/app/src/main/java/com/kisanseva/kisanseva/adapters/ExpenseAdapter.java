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

import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.entities.Expenses;
import com.kisanseva.kisanseva.entities.Land;
import com.kisanseva.kisanseva.ui.ExpenseActivity;
import com.kisanseva.kisanseva.ui.LandsActivity;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<Expenses> implements View.OnClickListener{

    private ArrayList<Expenses> dataSet;
    Context mContext;

    // View lookup cache

    private static class ViewHolder {
        TextView landname;
        TextView dateofExpense;
        TextView expenseamount;
        TextView category;
        TextView subcategory;
        TextView expensedescription;
        ImageView info;
    }

    public ExpenseAdapter(ArrayList<Expenses> data, Context context) {
        super(context, R.layout.expense_row_item, data);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Expenses expense = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.expense_row_item, parent, false);
            viewHolder.dateofExpense = (TextView) convertView.findViewById(R.id.dateofamountspent);
            viewHolder.landname = (TextView) convertView.findViewById(R.id.landName);
            viewHolder.expenseamount = (TextView) convertView.findViewById(R.id.cost);
            viewHolder.category = (TextView) convertView.findViewById(R.id.category);
            viewHolder.subcategory=(TextView)convertView.findViewById(R.id.subcategory);
            viewHolder.expensedescription=(TextView)convertView.findViewById(R.id.descr);
            //viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.dateofExpense.setText(expense.getDate());
        String landname="";
        if(mContext instanceof ExpenseActivity){ landname=((ExpenseActivity) mContext).getLandName(expense.getLandid());}
        viewHolder.landname.setText(landname);
        viewHolder.expenseamount.setText("- \u20B9 "+expense.getCost());
        viewHolder.category.setText(expense.getCategory());
        viewHolder.subcategory.setText(expense.getSubcategory());
        viewHolder.expensedescription.setText(expense.getExpenseDesc());
//        viewHolder.info.setOnClickListener(this);
  //      viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
