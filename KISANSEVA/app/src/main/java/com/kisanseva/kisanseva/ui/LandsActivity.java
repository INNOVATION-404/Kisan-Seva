package com.kisanseva.kisanseva.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.kisanseva.kisanseva.adapters.LandAdapter;
import com.kisanseva.kisanseva.basic.MainActivity;
import com.kisanseva.kisanseva.entities.Farmer;
import com.kisanseva.kisanseva.entities.Land;
import com.kisanseva.kisanseva.firedb.DatabaseUtils;
import com.kisanseva.kisanseva.utils.Util;
import com.thomashaertel.widget.MultiSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LandsActivity extends Activity implements OnLocaleChangedListener {
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    private static final String TAG = LandsActivity.class.getSimpleName();
    ArrayList<Land> lands;
    ListView listView;
    private static LandAdapter adapter;
    private Dialog mSplashDialog;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    String[] soiltypes, typeofcrops;
    String[] fruits = {"Apple", "Grapes", "Mango", "Pineapple", "Strawberry"};
    int images[] = {R.drawable.apple, R.drawable.grapes, R.drawable.mango, R.drawable.pineapple, R.drawable.strawberry};
    String soiltypeImages[]={"alluvial","blacksoil","redoryellowsoil","laterite","desertsoil"};
    int soiltypeimages[] = {getDrawable(soiltypeImages[0]),getDrawable(soiltypeImages[1]),getDrawable(soiltypeImages[2]),getDrawable(soiltypeImages[3]),getDrawable(soiltypeImages[4])};
    String userid = "";
    Farmer fm;
    SharedPreferences prefs;

    String selectedlandmeasureunit, selectedsoiltype,selectedsoiltypeimage, selectedcrop;
    private MultiSpinner spinner;
    private ArrayAdapter<String> sadapter;
    private String [] crops_grown;
    boolean[] selectedCropsarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lands);
        //getActionBar().setBackgroundDrawable(R.color.themeprimary);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setActionBar(toolbar);
        //category=getResources().getStringArray(R.array.)
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        getActionBar().setTitle("Kisan Seva - Lands");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseInstance = DatabaseUtils.getDBinstance();
         prefs=getSharedPreferences("kisanseva",0);
        userid=prefs.getString(Util.UserId,"");
        // get reference to 'users' node
        getDefaultsValuesFromFirebase();

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.addnewland);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox(false, null,0);
            }
        });
        crops_grown=getResources().getStringArray(R.array.crops_grown);
        String language = prefs.getString("language", "");
        setLanguage(language);
        getActionBar().setTitle(getResources().getString(R.string.kisan_seva_lands));



    }
    public static int getDrawable(String name) {
        Context context = KisanSevaApp.getContext();
        int resourceId = context.getResources().getIdentifier(name, "drawable", KisanSevaApp.getContext().getPackageName());
        return resourceId;
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
                Intent myIntent = new Intent(LandsActivity.this, SettingsActivity.class);
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
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("config");
        DatabaseReference soiltypesref = mFirebaseDatabase.child("soiltypes");

        ValueEventListener soiltypesrefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.e(TAG, "category does not exists");
                } else {
                    Log.e(TAG, "category Exists");


                    Log.e(TAG, dataSnapshot.getValue().toString());
                    String soiltyp = dataSnapshot.getValue().toString();
                    soiltyp = soiltyp.substring(1, soiltyp.length() - 1);


                    try {

                        soiltypes = soiltyp.split(",");
                        Log.e(TAG, Arrays.toString(soiltypes));
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
        DatabaseReference typeofcropsref = mFirebaseDatabase.child("config").child("typeofcrops");

        ValueEventListener typeofcropsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.e(TAG, "typeofcrops does not exists");
                } else {
                    Log.e(TAG, "typeofcrops Exists");


                    Log.e(TAG, dataSnapshot.getValue().toString());
                    String soiltyp = dataSnapshot.getValue().toString();
                    soiltyp = soiltyp.substring(1, soiltyp.length() - 1);


                    try {

                        typeofcrops = soiltyp.split(",");
                        Log.e(TAG, Arrays.toString(typeofcrops));
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
        typeofcropsref.addListenerForSingleValueEvent(typeofcropsListener);


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
                            if (d.getKey().equals("lands")) {
                                lands = new ArrayList<Land>();
                                for (DataSnapshot d1 : d.getChildren()) {
                                    Log.e(TAG + "key2", d1.getKey());
                                    Land newland=d1.getValue(Land.class);
                                    newland.setFirbaseObjectId(d1.getKey());
                                    lands.add(newland);
                                    Log.e(TAG + " value2", d1.getValue(Land.class).toString());
                                }
                                Log.e(TAG, "landsempty? " + (lands.isEmpty()));
                                listView = (ListView) findViewById(R.id.list);

                                adapter = new LandAdapter(lands, LandsActivity.this);
                                View empty = findViewById(R.id.empty);
                                listView.setEmptyView(empty);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Land dataModel = lands.get(position);

                                        System.out.println(dataModel.getAcresofLand() + "\n" + dataModel.getCrops() + " Soil Type: " + dataModel.getSoiltype());
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

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            Log.e(TAG,Arrays.toString(selected));
            selectedCropsarr=selected;
        }
    };

    public void showDialogBox(boolean isEdit,final Land ll,int position) {
        Log.e(TAG, "show Dialog box");
        //Create a new Dialog with a custom style SplashScreen
        mSplashDialog = new Dialog(this, R.style.SplashScreen);
        // Apply no titlebar to the dialog
        mSplashDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
        // Set the layout to the dialog created
        mSplashDialog.setContentView(R.layout.landview);
        // Set fullscreen flags for the dialog
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Set Layout params for the dialog
        mSplashDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Set cancelable as true
        mSplashDialog.setCancelable(true);
        // Show the dialog
        mSplashDialog.show();
// create spinner list elements
        sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        String []listcrops=getResources().getStringArray(R.array.crops_grown);
        for (String crp:listcrops){
            sadapter.add(crp);
        }

        // get spinner and set adapter
        spinner = (MultiSpinner) mSplashDialog.findViewById(R.id.spinnerMulti);
        spinner.setAdapter(sadapter, false, onSelectedListener);

        // set initial selection
        boolean[] selectedItems = new boolean[adapter.getCount()];
        selectedItems[1] = true; // select second item
        spinner.setSelected(selectedItems);
        Button savedata = (Button) mSplashDialog.findViewById(R.id.addland);
        TextView tx = (TextView) mSplashDialog.findViewById(R.id.titleforaddeditland);
        Button btx = (Button) mSplashDialog.findViewById(R.id.addland);
        Spinner crops = (Spinner) mSplashDialog.findViewById(R.id.crops);
        Spinner soiltype = (Spinner) mSplashDialog.findViewById(R.id.soiltype);
        EditText landName=(EditText) mSplashDialog.findViewById(R.id.landname);
        EditText landArea=(EditText)mSplashDialog.findViewById(R.id.acresOfLand);
        CustomSpinnetAdapter customAdapter = new CustomSpinnetAdapter(getApplicationContext(), soiltypeimages, soiltypes);
        CustomSpinnetAdapter cropadapter = new CustomSpinnetAdapter(getApplicationContext(), images, fruits);

        soiltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (soiltypes != null) {
                    if (soiltypes.length > position) {
                        selectedsoiltype = soiltypes[position];
                        selectedsoiltypeimage=soiltypeImages[position];
                    }
                }
                //Toast.makeText(LandsActivity.this, "You Select Position: " + position + " " + fruits[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        crops.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (typeofcrops != null) {
                    if (typeofcrops.length > position) {
                        selectedcrop = typeofcrops[position];
                    }

                }
               // Toast.makeText(LandsActivity.this, "You Select Position: " + position + " " + fruits[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        crops.setAdapter(cropadapter);
        soiltype.setAdapter(customAdapter);


        if (isEdit) {
            tx.setText("Edit Land Details");
            btx.setText("Update Land Details");
            Log.e(TAG,ll.toString());
            landName.setText(ll.getLandName());
            try {
                landArea.setText(ll.getAcresofLand()+"");
            }catch (Exception ex){
                Log.e(TAG,ex.getMessage());
            }
            savedata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveOrEditLandDetails(true,ll.getFirbaseObjectId());
                }
            });

        } else {
            tx.setText("Add Land Details");
            btx.setText("Save Land Details");
            savedata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveOrEditLandDetails(false,null);
                }
            });
        }
    }

    public void saveOrEditLandDetails(boolean isEdit,String id) {
        Log.e(TAG, "saveOrEditLandDetails called");
        if (fm != null) {
            Log.e(TAG, "farmer exists");
            TextView acres = (TextView) mSplashDialog.findViewById(R.id.acresOfLand);
            Spinner landmeasureunit = (Spinner) mSplashDialog.findViewById(R.id.areameasureunit);
            Spinner soiltyp = (Spinner) mSplashDialog.findViewById(R.id.soiltype);
            Spinner crops = (Spinner) mSplashDialog.findViewById(R.id.crops);
            TextView landName = (TextView) mSplashDialog.findViewById(R.id.landname);
            if (acres == null || landmeasureunit == null || soiltyp == null || crops == null || landName == null) {
                Log.e(TAG, "" + (acres == null));
                Log.e(TAG, "" + (landmeasureunit == null));
                Log.e(TAG, "" + (soiltyp == null));
                Log.e(TAG, "" + (crops == null));
                Log.e(TAG, "" + (landName == null));

            } else {
                try {
                    Land l = new Land();
                    l.setAcresofLand(Integer.parseInt(acres.getText().toString()));
                    l.setMeasureunit(landmeasureunit.getSelectedItem().toString());
                    l.setSoiltype(selectedsoiltype);
                    l.setSoiltypeImage(selectedsoiltypeimage);
                    l.setLandName(landName.getText().toString());
                    List<String> cropsz = new ArrayList<>();
                    cropsz.add(selectedcrop);
                    l.setCrops(cropsz);
                    int idx=0;
                    ArrayList<String> cropZ=new ArrayList<>();
                    for (boolean crps:selectedCropsarr){
                        if(crps){
                            cropZ.add(crops_grown[idx]);
                        }
                        idx++;
                    }
                    String crps=cropZ.toString();
                    crps=crps.replace("[","");
                    crps=crps.replace("]","");
                    l.setCropsGrown(crps);
                    mFirebaseInstance = FirebaseDatabase.getInstance();
                    // get reference to 'users' node
                    mFirebaseDatabase = mFirebaseInstance.getReference("farmers").child(userid).child("lands");
                    String landId;
                    if(isEdit){
                        landId=id;
                    }else {
                        landId = mFirebaseDatabase.push().getKey();
                    }
                    mFirebaseDatabase.child(landId).setValue(l);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
