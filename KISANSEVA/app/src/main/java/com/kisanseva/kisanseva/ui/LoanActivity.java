package com.kisanseva.kisanseva.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kisanseva.kisanseva.R;
//import com.kisanseva.kisanseva.adapters.LandAdapter;
import com.kisanseva.kisanseva.adapters.LoanAdapter;

import com.kisanseva.kisanseva.basic.MainActivity;
import com.kisanseva.kisanseva.entities.Farmer;

import com.kisanseva.kisanseva.entities.Loan;
import com.kisanseva.kisanseva.firedb.DatabaseUtils;
import com.kisanseva.kisanseva.utils.DateUtils;
import com.kisanseva.kisanseva.utils.DialogManager;
import com.kisanseva.kisanseva.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LoanActivity extends Activity {
    private static final String TAG = LoanActivity.class.getSimpleName();
    ArrayList<Loan> loans;
    ListView listView;
    private static LoanAdapter adapter;
    private Dialog mSplashDialog;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Date selectedDate;
    private Button loandate;
    int tenure1;
    int[] tenure={3,6,8,12};
    String loantype1;
    String[] loanType={"Bank Loan","Money Lender"};
    /* String[] soiltypes, typeofcrops;
     String[] fruits = {"Apple", "Grapes", "Mango", "Pineapple", "Strawberry"};
     int images[] = {R.drawable.apple, R.drawable.grapes, R.drawable.mango, R.drawable.pineapple, R.drawable.strawberry};*/
    String userid = "7331134951";
    Farmer fm;
    SharedPreferences prefs;
    String selectedlandmeasureunit, selectedsoiltype, selectedcrop;
    TableLayout table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        //getActionBar().setBackgroundDrawable(R.color.themeprimary);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setActionBar(toolbar);
        //category=getResources().getStringArray(R.array.)
        mFirebaseInstance = DatabaseUtils.getDBinstance();
        // RelativeLayout Rlayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
        // get reference to 'users' node
        getDefaultsValuesFromFirebase();


        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.addnewloan);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox(false, null,0);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.e(TAG,"Clicked on Menu Item");
                Intent myIntent = new Intent(LoanActivity.this, SettingsActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
                return false;
            }
        });
        MenuItem item = menu.findItem(R.id.logout);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                logOut();
                return false;
            }
        });

        return true;
    }
    private void logOut(){
        prefs = getApplicationContext().getSharedPreferences("kisanseva", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(myIntent);
        finish();
    }

    public void getDefaultsValuesFromFirebase() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = rootRef.child("farmers").child(userid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.e(TAG, "User does not exists");
                } else {
                    Log.e(TAG, "User Exists");
                    fm = dataSnapshot.getValue(Farmer.class);
                    System.out.println(fm);
                    if (fm != null) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            Log.e(TAG + " Key1", d.getKey());
                            if (d.getKey().equals("loans")) {
                                loans = new ArrayList<Loan>();
                                for (DataSnapshot d1 : d.getChildren()) {
                                    Log.e(TAG + "key2", d1.getKey());
                                    Loan newloan=d1.getValue(Loan.class);
                                    newloan.setFirbaseObjectId(d1.getKey());
                                    loans.add(newloan);
                                    Log.e(TAG + " value2", d1.getValue(Loan.class).toString());
                                }
                                Log.e(TAG, "loansempty? " + (loans.isEmpty()));
                                listView = (ListView) findViewById(R.id.list);

                                adapter = new LoanAdapter(loans, LoanActivity.this);
                                View empty = findViewById(R.id.empty);
                                listView.setEmptyView(empty);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { ////////////////////////////doubt
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        showDialogBox(true,null,position);
                                        // Loan dataModel = loans.get(position);

                                        // System.out.println(dataModel.getAcresofLand() + "\n" + dataModel.getCrops() + " Soil Type: " + dataModel.getSoiltype());
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);


    }
    Date date1=selectedDate;


    public void initTable(Context ctx,Dialog d,int position) {
        try {
            table=(TableLayout)d.findViewById(R.id.table);
            Log.e(TAG,"initTable");
            loandate = (Button) mSplashDialog.findViewById(R.id.etDate);

            // String arr[] = loandate.getText().toString().split("/");
            EditText loanamt = (EditText) mSplashDialog.findViewById(R.id.etAmt);
            EditText interest = (EditText) mSplashDialog.findViewById(R.id.etInterest);
            double p = loans.get(position).getLoanAmt();

            double r = loans.get(position).getInterestrate();
            double t = loans.get(position).getTenureTime();
            Log.e(TAG,p+"");
            Log.e(TAG,t+"");
            Log.e(TAG,r+"");
            double total = (((p * t * r) / 100) + p) / t;
            ArrayList<Date> al = new ArrayList<>();
            Log.e(TAG,loans.get(position).getLoanDate());

            Date loandate= DateUtils.getDate(loans.get(position).getLoanDate());
            Log.e(TAG,loandate.toString());
            Date tempdate=null;
            for(int i=0;i<loans.get(position).getTenureTime();i++){
                if(tempdate==null){
                    tempdate=loandate;
                    tempdate=DateUtils.getNextMonthDate(tempdate,1);
                }else
                {
                    tempdate=DateUtils.getNextMonthDate(tempdate,1);
                }

                al.add(tempdate);
            }

            TableRow tbrow0 = new TableRow(ctx);
            TextView tv0 = new TextView(ctx);
            tv0.setText(" Date of payment ");
            tv0.setTextColor(Color.parseColor("#000000"));
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(ctx);
            tv1.setText(" Amount ");
            tv1.setTextColor(Color.parseColor("#000000"));
            tbrow0.addView(tv1);
            table.addView(tbrow0);
            int selectedTenure=0;
            for (int x=0;x<tenure.length;x++){
                if(tenure[x]==loans.get(position).getTenureTime()){
                    selectedTenure=x;
                    break;
                }
            }
            for (int i = 0; i < tenure[selectedTenure]; i++) {

                TableRow tbrow = new TableRow(ctx);
                TextView t1v = new TextView(ctx);
                t1v.setText(Util.formatDateToString(al.get(i), Util.getCurrentDateFormat()));
                t1v.setTextColor(Color.parseColor("#000000"));
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(ctx);
                t2v.setText(""+String.format("%.2f", total));
                t2v.setTextColor(Color.parseColor("#fa0000"));
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                table.addView(tbrow);
                Log.e(TAG, "created table");


            }
        }catch (Exception ex){
            ex.printStackTrace();
            //System.out.println();
            System.out.println(ex.getMessage());
        }

    }

    public Date addOneMonth () {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();


    }


    public void showDialogBox(boolean isEdit, final Loan ll, final int position) {
        Log.e(TAG, "show Dialog box");
        //Create a new Dialog with a custom style SplashScreen
        mSplashDialog = new Dialog(this, R.style.SplashScreen);
        // Apply no titlebar to the dialog
        mSplashDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        // Set the layout to the dialog created
        mSplashDialog.setContentView(R.layout.loanview);
        // Set fullscreen flags for the dialog
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set Layout params for the dialog
        mSplashDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Set cancelable as true
        mSplashDialog.setCancelable(true);
        // Show the dialog
        mSplashDialog.show();
        Button savedata = (Button) mSplashDialog.findViewById(R.id.btnSave);
        TextView tx = (TextView) mSplashDialog.findViewById(R.id.loantracker);
        Button btx = (Button) mSplashDialog.findViewById(R.id.btnSave);
        Spinner loantype =(Spinner)mSplashDialog.findViewById(R.id.spinnerType);
        EditText bankname=(EditText) mSplashDialog.findViewById(R.id.etBank);
        EditText loanamt=(EditText)mSplashDialog.findViewById(R.id.etAmt);
        Spinner tenuretime=(Spinner) mSplashDialog.findViewById(R.id.spinnerTenure);
        EditText interest=(EditText) mSplashDialog.findViewById(R.id.etInterest);
        loandate=(Button) mSplashDialog.findViewById(R.id.etDate);
        TableLayout table=(TableLayout)mSplashDialog.findViewById(R.id.table);
        loandate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        EditText description=(EditText) mSplashDialog.findViewById(R.id.etDesc);


        //TableLayout table=(TableLayout)mSplashDialog.findViewById(R.id.table);
        // RelativeLayout rlayout =(RelativeLayout)mSplashDialog.findViewById(R.id.RelativeLayout);
//////table




        tenuretime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tenure1 =tenure[position];
                Toast.makeText(LoanActivity.this, "You Selected: " +  tenure[position]  , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         /* ArrayAdapter aa=new ArrayAdapter(this,android.R.layout.simple_spinner_item,tenure);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        tenuretime.setAdapter(aa);*/

        loantype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loantype1 =loanType[position];
                Toast.makeText(LoanActivity.this, "You Selected: " +  loanType[position]  , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        ArrayAdapter a1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, loanType);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        loantype.setAdapter(a1);


        if (isEdit) {

            initTable(LoanActivity.this,mSplashDialog,position);
            /*


             */
            tx.setText("Edit Loan Details");
            btx.setText("Update Loan Details");
            // table.setVisibility(View.VISIBLE);
            Log.e(TAG,ll.toString());///////////////////
            bankname.setText(ll.getBankName());
           /* try {
                landArea.setText(ll.getAcresofLand()+"");
            }catch (Exception ex){
                Log.e(TAG,ex.getMessage());
            }*/
            savedata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveOrEditLandDetails(true,ll.getFirbaseObjectId(),position);

                }
            });

        } else {
            tx.setText("Add Loan Details");
            btx.setText("Save Loan Details");
            // table.setVisibility(View.INVISIBLE);
            savedata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveOrEditLandDetails(false,null,position);
                }
            });

        }

    }

    public void saveOrEditLandDetails(boolean isEdit,String id,int position) {
        Log.e(TAG, "saveOrEditLoanDetails called");
        if (fm != null) {
            Log.e(TAG, "farmer exists");

            Spinner tenuretime = (Spinner) mSplashDialog.findViewById(R.id.spinnerTenure);
            TextView description = (TextView) mSplashDialog.findViewById(R.id.etDesc);
            Spinner loantype =(Spinner)mSplashDialog.findViewById(R.id.spinnerType);
            TextView bankname = (TextView) mSplashDialog.findViewById(R.id.etBank);
            TextView loanamt = (TextView) mSplashDialog.findViewById(R.id.etAmt);
            loandate = (Button) mSplashDialog.findViewById(R.id.etDate);
            loandate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDateDialog();
                }
            });
            TextView interest = (TextView) mSplashDialog.findViewById(R.id.etInterest);
            TableLayout table=(TableLayout)mSplashDialog.findViewById(R.id.table);
            initTable(LoanActivity.this,mSplashDialog,position);


            //  Log.e(TAG, "tenure time intitialized" + (tenuretime.getText()));
            Log.e(TAG, "description intitialized" + (loanamt.getText()));
            Log.e(TAG, "bankname intitialized" + (bankname.getText()));
            //  Log.e(TAG, "EMI intitialized" + (EMI.getText()));
            Log.e(TAG, "loandate intitialized" + (loandate.getText()));


            if (tenuretime == null || description == null || bankname == null || null == loanamt || loandate == null || interest==null) {
                Log.e(TAG, "" + (tenuretime == null));
                Log.e(TAG, "" + (description == null));
                Log.e(TAG, "" + (bankname == null));
                Log.e(TAG, "" + (loanamt == null));
                Log.e(TAG, "" + (loandate == null));
                Log.e(TAG, "" + (interest == null));

            } else {
                try {
                    Loan l = new Loan();
                    l.setBankName((bankname.getText().toString()));
                    l.setLoanAmt(Double.parseDouble(loanamt.getText().toString()));
                    l.setLoanDate((loandate.getText().toString()));
                    l.setInterestrate(Double.parseDouble(interest.getText().toString()));
                    l.setTenureTime(tenure1);
                    l.setLoantype(loantype1);
                    l.setDescription((description.getText().toString()));
                    /*
                    l.setBankName("HDFC");
                    l.setInstallments(Double.parseDouble("600"));
                    l.setLoanDate("20-02-2019");
                    l.setTenureTime(Double.parseDouble("900")+"");
                    l.setDescription("some desc");
*/
                    mFirebaseInstance = FirebaseDatabase.getInstance();
                    // get reference to 'users' node
                    mFirebaseDatabase = mFirebaseInstance.getReference("farmers").child(userid).child("loans");
                    String loanId;
                    if(isEdit){
                        loanId=id;
                    }else {
                        loanId = mFirebaseDatabase.push().getKey();
                    }
                    mFirebaseDatabase.child(loanId).setValue(l);
                    mSplashDialog.dismiss();
                    getDefaultsValuesFromFirebase();
                } catch (Exception ex) {
                    ex.getStackTrace();
                    Log.e("Exception", ex.getMessage());
                }
            }
        } else {
            Log.e(TAG, "farmer doesnot exists");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    public void showDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        selectedDate = new Date();
        calendar.setTime(selectedDate);
        DialogManager.getInstance().showDatePickerDialog(LoanActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                selectedDate = calendar.getTime();
                updateDate();
            }
        }, calendar);
    }

    private void updateDate() {
        loandate.setText(Util.formatDateToString(selectedDate, Util.getCurrentDateFormat()));
    }
}


/*date
Calendar calendar = Calendar.getInstance();

    Calendar calFebruary = Calendar.getInstance();
    calFebruary.set(Calendar.MONTH, Calendar.FEBRUARY);

    if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {// if first day of month
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
    Date nextMonthFirstDay = calendar.getTime();
    System.out.println(nextMonthFirstDay);
}
    //if last day of the month
      else if ((calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))) {// if last day of month
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    Date nextMonthLastDay = calendar.getTime();
    System.out.println(nextMonthLastDay);
    }
 else { // any day
    calendar.add(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    Date theNextDate = calendar.getTime();
    System.out.println(theNextDate);
    }


//////bestttt
public Date  addOneMonth(Date d)  {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
}`

 */
