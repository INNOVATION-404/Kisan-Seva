package com.kisanseva.kisanseva.adapters;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.entities.Loan;
import com.kisanseva.kisanseva.ui.LoanActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class LoanAdapter extends ArrayAdapter<Loan> implements View.OnClickListener {
    private ArrayList<Loan> dataSet;
    Context mContext;

    /*public LoanAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }*/

    // View lookup cache
    private static class ViewHolder {
       // TextView bankname;
        TextView loanamount;
        TextView interestrate;
        TextView loandate;
        TextView loantype;
        TextView tenuretime;
        TextView loanname;
        ImageView image;
        //LayoutInflater loanitem;
        LinearLayout loanItem;

    }

    public LoanAdapter(ArrayList<Loan> data, Context context) {
        super(context, R.layout.loan_item, data);
        this.dataSet = data;
        this.mContext=context;
        for (Loan l:data
             ) {
            System.out.println(l);
        }
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Loan Loan=(Loan)object;

        switch (v.getId())
        {
            default:
                Log.e("Instance ",(mContext instanceof LoanActivity)+"");
                if(mContext instanceof LoanActivity){ ((LoanActivity)mContext).showDialogBox(true,Loan,position); }
                Snackbar.make(v, "Bank Name " +Loan.getBankName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Loan Loan = getItem(position);
        System.out.println("getView called");
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.loan_item, parent, false);
            viewHolder.loanamount = (TextView) convertView.findViewById(R.id.tvAmt);
            viewHolder.interestrate = (TextView) convertView.findViewById(R.id.tvInterest);
            viewHolder.loandate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tenuretime =(TextView)convertView.findViewById(R.id.tvTime);
            viewHolder.loantype=(TextView)convertView.findViewById(R.id.tvLoanType);
            viewHolder.loanname=(TextView)convertView.findViewById(R.id.tvDesc);
            viewHolder.image=(ImageView)convertView.findViewById(R.id.testImage);
            viewHolder.loanItem=(LinearLayout)convertView.findViewById(R.id.loan_item);

            result=convertView;
            System.out.println(viewHolder);
            convertView.setTag(viewHolder);
        } else {

                viewHolder = (ViewHolder) convertView.getTag();
                result = convertView;

        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.loanname.setText(Loan.getDescription());
        viewHolder.loanamount.setText("- \u20B9 "+Loan.getLoanAmt()+"");
        viewHolder.loanamount.setTextColor(Color.parseColor("#fa0000"));
        viewHolder.loandate.setText(Loan.getLoanDate());


        viewHolder.interestrate.setText(Loan.getInterestrate()+" % Interest Rate");
        viewHolder.loantype.setText("Source: "+Loan.getLoantype());
        viewHolder.tenuretime.setText(Loan.getTenureTime()+" Months");////issue
       // viewHolder.image.setOnClickListener(this);
        viewHolder.loanItem.setOnClickListener(this);
        viewHolder.loanItem.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
