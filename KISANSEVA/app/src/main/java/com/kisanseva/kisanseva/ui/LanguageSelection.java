package com.kisanseva.kisanseva.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kisanseva.kisanseva.R;
import com.kisanseva.kisanseva.basic.MainActivity;

public class LanguageSelection extends Activity implements View.OnClickListener{

    String lang="";
    LinearLayout english,hindi,telugu;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        english=(LinearLayout)findViewById(R.id.english);
        hindi=(LinearLayout)findViewById(R.id.hindi);
        telugu=(LinearLayout)findViewById(R.id.telugu);
        english.setOnClickListener(this);
        hindi.setOnClickListener(this);
        telugu.setOnClickListener(this);
        Button b=(Button)findViewById(R.id.next);
        b.setOnClickListener(this);
        getActionBar().setTitle("Kisan Seva - Language Selection");
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#34af23")));
        prefs=getSharedPreferences("kisanseva",0);
        if(prefs.contains("language")){
            String lang=prefs.getString("language","");
            switch (lang){
                case "en":
                    Toast.makeText(getApplicationContext(),"Language Set: English",Toast.LENGTH_SHORT).show();
                    startMainActivity();
                    break;
                case "hi":
                    Toast.makeText(getApplicationContext(),"Language Set: Hindi",Toast.LENGTH_SHORT).show();
                    startMainActivity();
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Language Set: None",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
    private void startMainActivity(){
        Intent myIntent = new Intent(LanguageSelection.this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(myIntent);
        finish();
    }
    @Override
    public void onClick(View v) {
        System.out.println("On Click");
        switch (v.getId()){
            case R.id.english:
                lang="english";
                Log.d("Language","English");
                english.setBackgroundColor(getResources().getColor(R.color.selected));
                hindi.setBackgroundColor(getResources().getColor(R.color.unselected));
                telugu.setBackgroundColor(getResources().getColor(R.color.unselected));
                break;
            case R.id.hindi:
                lang="hindi";
                Log.d("Language","Hindi");
                english.setBackgroundColor(getResources().getColor(R.color.unselected));
                hindi.setBackgroundColor(getResources().getColor(R.color.selected));
                telugu.setBackgroundColor(getResources().getColor(R.color.unselected));
                break;
            case R.id.telugu:
                lang="telugu";
                Log.d("Language","Telugu");
                english.setBackgroundColor(getResources().getColor(R.color.unselected));
                hindi.setBackgroundColor(getResources().getColor(R.color.unselected));
                telugu.setBackgroundColor(getResources().getColor(R.color.selected));
                break;
            case R.id.next:
                editor = prefs.edit();
                switch (lang){
                    case "english":
                        Toast.makeText(getApplicationContext(),"English Selected",Toast.LENGTH_SHORT).show();
                        editor.putString("language","en");
                        editor.commit();
                        startMainActivity();
                        break;
                    case "hindi":
                        Toast.makeText(getApplicationContext(),"Hindi Selected",Toast.LENGTH_SHORT).show();
                        editor.putString("language","hi");
                        editor.commit();
                        startMainActivity();
                        break;
                    case "telugu":
                        Toast.makeText(getApplicationContext(),"Coming soon! For now, we support only English and Hindi",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Please select language",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
