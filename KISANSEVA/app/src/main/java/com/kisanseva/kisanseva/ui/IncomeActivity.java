package com.kisanseva.kisanseva.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kisanseva.kisanseva.KisanSevaApp;
import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.adapters.CustomSpinnetAdapter;
import com.kisanseva.kisanseva.adapters.IncomeAdapter;
import com.kisanseva.kisanseva.entities.Expenses;
import com.kisanseva.kisanseva.entities.Farmer;
import com.kisanseva.kisanseva.entities.Income;
import com.kisanseva.kisanseva.entities.Land;
import com.kisanseva.kisanseva.firedb.DatabaseUtils;
import com.kisanseva.kisanseva.utils.DialogManager;
import com.kisanseva.kisanseva.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class IncomeActivity extends Activity implements OnLocaleChangedListener {
    private static final String TAG = IncomeActivity.class.getSimpleName();
    private SharedPreferences prefs;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Dialog mSplashDialog;
    String userid = "";
    Farmer fm;
    String[] category, typeofcrops;
    String selectedExpense;
    ArrayList<String> lands;
    ArrayList<Land> landObjects;
    ArrayList<Income> incomeObjects;
    DatePicker datePicker;
    private Date selectedDate;
    private Button btnDate;
    Button saveexpense;
    ListView listofIncomes;
    String soiltypeImages[]={"alluvial","blacksoil","redoryellowsoil","laterite","desertsoil"};
    int soiltypeimages[] = {getDrawable(soiltypeImages[0]),getDrawable(soiltypeImages[1]),getDrawable(soiltypeImages[2]),getDrawable(soiltypeImages[3]),getDrawable(soiltypeImages[4])};

    int images[] = {R.drawable.apple, R.drawable.grapes, R.drawable.mango, R.drawable.pineapple, R.drawable.strawberry};
    Spinner landselected;
    Land selectedLand;
    public double totalIncomes=0.0;
    TextView incomeTillDate;
    int categoryImage[]={R.drawable.icons8light48,R.drawable.icons8experiment48,R.drawable.icons8worker48,R.drawable.icons8seed48,R.drawable.icons8robot48};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        ActionBar bar = getActionBar();
        bar.setTitle("Income");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#34af23")));
        mFirebaseInstance = DatabaseUtils.getDBinstance();
        SharedPreferences prefs=getSharedPreferences("kisanseva",0);
        userid=prefs.getString(Util.UserId,"");
        // get reference to 'users' node
        getDefaultsValuesFromFirebase();
        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.addnewexpense);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        incomeTillDate=(TextView)findViewById(R.id.totalexpenses);
        String language = prefs.getString("language", "");
        setLanguage(language);
        getActionBar().setTitle(getResources().getString(R.string.kisan_seva_income));


    }
    public void showDialog(){
        //Create a new Dialog with a custom style SplashScreen
        mSplashDialog = new Dialog(this, R.style.SplashScreen);
        // Apply no titlebar to the dialog
        mSplashDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        // Set the layout to the dialog created
        mSplashDialog.setContentView(R.layout.expenseview);
        // Set fullscreen flags for the dialog
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set Layout params for the dialog
        mSplashDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Set cancelable as true
        mSplashDialog.setCancelable(true);
        // Show the dialog
        mSplashDialog.show();

        Spinner categoryspinner = (Spinner) mSplashDialog.findViewById(R.id.category);
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (category != null) {
                    if (category.length > position) {
                        selectedExpense = category[position];
                    }
                }
                Toast.makeText(IncomeActivity.this, "You Select Position: " + position + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        CustomSpinnetAdapter customAdapter = new CustomSpinnetAdapter(getApplicationContext(), categoryImage, category);
        categoryspinner.setAdapter(customAdapter);
        categoryspinner.setVisibility(View.GONE);
        landselected=(Spinner) mSplashDialog.findViewById(R.id.landid);
        landselected.setVisibility(View.GONE);
        String[] array = new String[lands.size()];
        int[] img=new int[lands.size()];
        for (int i = 0; i < lands.size(); i++) {
            array[i] = lands.get(i);img[i]=images[i];
        }
        CustomSpinnetAdapter customLandAdapter = new CustomSpinnetAdapter(getApplicationContext(), img, array);
        landselected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG,"Selected land "+landObjects.get(i).toString());
                selectedLand=landObjects.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        landselected.setAdapter(customLandAdapter);
        LinearLayout subcategory=mSplashDialog.findViewById(R.id.showingSubcategory);
        subcategory.setVisibility(View.GONE);
        LinearLayout landrow=mSplashDialog.findViewById(R.id.landrow);
        landrow.setVisibility(View.GONE);
        LinearLayout categoryrow=mSplashDialog.findViewById(R.id.categoryrow);
        categoryrow.setVisibility(View.GONE);
        btnDate = (Button)mSplashDialog.findViewById(R.id.btn_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        saveexpense=(Button)mSplashDialog.findViewById(R.id.addexpense);
        saveexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrEditExpenseDetails(false,null);
            }
        });


    }
    public static int getDrawable(String name) {
        Context context = KisanSevaApp.getContext();
        int resourceId = context.getResources().getIdentifier(name, "drawable", KisanSevaApp.getContext().getPackageName());
        return resourceId;
    }
    public String getLandName(String landid){
        Log.e("getLandName",landid+"");
        for (int i=0;i<landObjects.size();i++){
            Land lm=landObjects.get(i);
            Log.e("getLandName",lm.toString());
            if(lm.getFirbaseObjectId().equals(landid)){
                return lm.getLandName();
            }
        }
        return "";
    }
    public void saveOrEditExpenseDetails(boolean isEdit,String id) {
        Log.e(TAG, "saveOrEditExpenseDetails called");
        if (fm != null) {
            Log.e(TAG, "farmer exists");
            EditText amountspent = (EditText) mSplashDialog.findViewById(R.id.amountspent);
            EditText expensedesc=(EditText) mSplashDialog.findViewById(R.id.expenseDesc);
            Button datebtn=mSplashDialog.findViewById(R.id.btn_date);


            if (amountspent == null || expensedesc == null) {
                Log.e(TAG, "" + (amountspent == null));


            } else {
                try {

                    String expensedate=datebtn.getText().toString();
                    expensedate=expensedate.replace("/","-");
                    Income ex= new Income();
                    ex.setCost(Double.parseDouble(amountspent.getText().toString()));
                    ex.setDate(expensedate);
                    ex.setExpenseDesc(expensedesc.getText().toString());
                    //ex.setLandid(selectedLand.getFirbaseObjectId());
                    mFirebaseInstance = FirebaseDatabase.getInstance();
                    // get reference to 'users' node

                    mFirebaseDatabase = mFirebaseInstance.getReference("farmers").child(userid).child("income").child(expensedate);
                    String expenseId;
                    if(isEdit){
                        expenseId=id;
                    }else {
                        expenseId = mFirebaseDatabase.push().getKey();
                    }
                    mFirebaseDatabase.child(expenseId).setValue(ex);
                    mSplashDialog.dismiss();
                    getDefaultsValuesFromFirebase();
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        } else {
            Log.e(TAG, "farmer doesnot exists");
        }
    }
    public void getDefaultsValuesFromFirebase() {


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference soiltypesref = mFirebaseDatabase.child("config").child("income").child("category");

        ValueEventListener soiltypesrefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.e(TAG, "category does not exists");
                } else {
                    Log.e(TAG, "category Exists");


                    Log.e(TAG, dataSnapshot.getValue().toString());
                    String cat = dataSnapshot.getValue().toString();
                    cat = cat.substring(1, cat.length() - 1);


                    try {

                        category = cat.split(",");
                        Log.e(TAG, Arrays.toString(category));
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        soiltypesref.addListenerForSingleValueEvent(soiltypesrefListener);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = rootRef.child("farmers").child(userid);

        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.e(TAG, "User does not exists");
                } else {
                    Log.e(TAG, "User Exists");
                    fm = dataSnapshot.getValue(Farmer.class);
                    System.out.println(fm);
                    incomeObjects =new ArrayList<Income>();
                    if (fm != null) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            Log.e(TAG + " Key1", d.getKey());

                            if (d.getKey().equals("lands")) {
                                lands=new ArrayList<String>();
                                landObjects=new ArrayList<Land>();

                                for (DataSnapshot d1 : d.getChildren()) {
                                    try {
                                        Log.e(TAG,d1.getKey());
                                        Land ll=d1.getValue(Land.class);
                                        Log.e(TAG,ll.toString());
                                        Log.e(TAG,ll.getSoiltype());
                                        ll.setFirbaseObjectId(d1.getKey());
                                        lands.add(ll.getLandName());
                                        landObjects.add(ll);


                                    }catch (Exception ex){
                                        Log.e(TAG,ex.getMessage());
                                    }
                                }

                            }
                            if (d.getKey().equals("income")) {
                                Log.e(TAG,"Entered into Income");
                                for (DataSnapshot dates:d.getChildren()){
                                    Log.e(TAG,"Entered into Income"+dates.getKey());
                                    for (DataSnapshot expenseobjects:dates.getChildren()){
                                        Log.e(TAG,"Entered into Income"+expenseobjects.getKey());
                                        Log.e(TAG+"Entered into Income",expenseobjects.getValue(Income.class).toString());
                                        incomeObjects.add(expenseobjects.getValue(Income.class));
                                    }
                                }
                            }
                        }
                        listofIncomes =(ListView)findViewById(R.id.list);
                        IncomeAdapter exp=new IncomeAdapter(incomeObjects,IncomeActivity.this);
                        for (Income eachIncome:incomeObjects){
                            totalIncomes=totalIncomes+eachIncome.getCost();
                        }
                        incomeTillDate.setText("+ \u20B9 "+totalIncomes);
                        incomeTillDate.setTextColor(Color.parseColor("#34af23"));
                        listofIncomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.e(TAG,"Expense Clicked");
                            }
                        });
                        View empty = findViewById(R.id.empty);
                        listofIncomes.setEmptyView(empty);
                        listofIncomes.setAdapter(exp);
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
    private void showDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        selectedDate = new Date();
        calendar.setTime(selectedDate);
        DialogManager.getInstance().showDatePickerDialog(IncomeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                selectedDate = calendar.getTime();
                updateDate();
            }
        }, calendar);
    }

    private void updateDate() {
        btnDate.setText(Util.formatDateToString(selectedDate, Util.getCurrentDateFormat()));
    }
    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }



}
