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
import com.kisanseva.kisanseva.adapters.ExpenseAdapter;
import com.kisanseva.kisanseva.entities.Expenses;
import com.kisanseva.kisanseva.entities.Farmer;
import com.kisanseva.kisanseva.entities.Land;
import com.kisanseva.kisanseva.firedb.DatabaseUtils;
import com.kisanseva.kisanseva.utils.DialogManager;
import com.kisanseva.kisanseva.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ExpenseActivity extends Activity implements OnLocaleChangedListener{
    private static final String TAG = ExpenseActivity.class.getSimpleName();
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
    ArrayList<Expenses> expensesObjects;
    DatePicker datePicker;
    private Date selectedDate;
    private Button btnDate;
    Button saveexpense;
    ListView listofExpenses;
    int images[] = {R.drawable.apple, R.drawable.grapes, R.drawable.mango, R.drawable.pineapple, R.drawable.strawberry};
    Spinner landselected;
    Land selectedLand;
    public double totalExpenses=0.0;
    TextView expensesTillDate;
    LinearLayout showingSubcategory;
    Spinner subcat;
    String soiltypeImages[]={"alluvial","blacksoil","redoryellowsoil","laterite","desertsoil"};
    int soiltypeimages[] = {getDrawable(soiltypeImages[0]),getDrawable(soiltypeImages[1]),getDrawable(soiltypeImages[2]),getDrawable(soiltypeImages[3]),getDrawable(soiltypeImages[4])};
    int categoryImage[]={R.drawable.icons8light48,R.drawable.icons8experiment48,R.drawable.icons8worker48,R.drawable.icons8seed48,R.drawable.icons8robot48};
    String selectedSubcategory,selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Kisan Seva - Expenses");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#34af23")));
        mFirebaseInstance = DatabaseUtils.getDBinstance();
        SharedPreferences prefs=getSharedPreferences("kisanseva",0);
        userid=prefs.getString(Util.UserId,"");
        expensesTillDate=(TextView)findViewById(R.id.totalexpenses);
        // get reference to 'users' node
        getDefaultsValuesFromFirebase();
        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.addnewexpense);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        String language = prefs.getString("language", "");
        setLanguage(language);
        getActionBar().setTitle(getResources().getString(R.string.kisan_seva_expenses));

    }
    public static int getDrawable(String name) {
        Context context = KisanSevaApp.getContext();
        int resourceId = context.getResources().getIdentifier(name, "drawable", KisanSevaApp.getContext().getPackageName());
        return resourceId;
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
        showingSubcategory=(LinearLayout)mSplashDialog.findViewById(R.id.showingSubcategory);
        subcat=(Spinner)mSplashDialog.findViewById(R.id.subcategory);
        final Spinner categoryspinner = (Spinner) mSplashDialog.findViewById(R.id.category);
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (category != null) {
                    if (category.length > position) {
                        selectedExpense = category[position];
                    }
                }
                selectedCategory=category[position];
               }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CustomSpinnetAdapter customAdapter = new CustomSpinnetAdapter(getApplicationContext(), categoryImage, category);
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0 || i==2 ){
                    showingSubcategory.setVisibility(View.GONE);
                }else{
                    showingSubcategory.setVisibility(View.VISIBLE);
                    switch (i){
                        case 1:
                            final String[] typeofChemicals={"pesticides","herbicides","insecticides","fungicides"};
                            int arrofImg[]={getDrawable(typeofChemicals[0]),getDrawable(typeofChemicals[1]),getDrawable(typeofChemicals[2]),getDrawable(typeofChemicals[3])};
                            CustomSpinnetAdapter typofChem=new CustomSpinnetAdapter(getApplicationContext(),arrofImg,typeofChemicals);
                            subcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    selectedSubcategory=typeofChemicals[i];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            subcat.setAdapter(typofChem);
                            break;
                        case 4:
                            break;
                        case 3:
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        categoryspinner.setAdapter(customAdapter);
        landselected=(Spinner) mSplashDialog.findViewById(R.id.landid);
        String[] array = new String[lands.size()];
        int[] arrayOfLandImages = new int[lands.size()];
        int[] img=new int[lands.size()];
        for (int i = 0; i < lands.size(); i++) {
            array[i] = lands.get(i);img[i]=images[i];
            for(int j=0;j<soiltypeImages.length;j++){
                if(landObjects.get(i).getSoiltypeImage().equals(soiltypeImages[j])){
                    arrayOfLandImages[i]=getDrawable(soiltypeImages[j]);
                    break;
                }
            }
        }

        CustomSpinnetAdapter customLandAdapter = new CustomSpinnetAdapter(getApplicationContext(), arrayOfLandImages, array);
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
                    Expenses ex= new Expenses();
                    ex.setCost(Double.parseDouble(amountspent.getText().toString()));
                    ex.setDate(expensedate);
                    ex.setExpenseDesc(expensedesc.getText().toString());
                    ex.setLandid(selectedLand.getFirbaseObjectId());
                    ex.setCategory(selectedCategory);
                    ex.setSubcategory(selectedSubcategory);
                    mFirebaseInstance = FirebaseDatabase.getInstance();
                    // get reference to 'users' node

                    mFirebaseDatabase = mFirebaseInstance.getReference("farmers").child(userid).child("expenses").child(expensedate);
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
        DatabaseReference soiltypesref = mFirebaseDatabase.child("config").child("expenses").child("category");

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
                    expensesObjects=new ArrayList<Expenses>();
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
                            if (d.getKey().equals("expenses")) {
                                Log.e(TAG,"Entered into Expenses");
                                for (DataSnapshot dates:d.getChildren()){
                                    Log.e(TAG,"Entered into Expenses"+dates.getKey());
                                    for (DataSnapshot expenseobjects:dates.getChildren()){
                                        Log.e(TAG,"Entered into Expenses"+expenseobjects.getKey());
                                        Log.e(TAG+"Entered into Expenses",expenseobjects.getValue(Expenses.class).toString());
                                        expensesObjects.add(expenseobjects.getValue(Expenses.class));
                                    }
                                }
                            }
                        }
                        listofExpenses=(ListView)findViewById(R.id.list);
                        for (Expenses eachExpense:expensesObjects){
                            totalExpenses=totalExpenses+eachExpense.getCost();
                        }
                        expensesTillDate.setText("- \u20B9 "+totalExpenses);
                        ExpenseAdapter exp=new ExpenseAdapter(expensesObjects,ExpenseActivity.this);
                        listofExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.e(TAG,"Expense Clicked");
                            }
                        });
                        View empty = findViewById(R.id.empty);
                        listofExpenses.setEmptyView(empty);
                        listofExpenses.setAdapter(exp);
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
        DialogManager.getInstance().showDatePickerDialog(ExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {
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
