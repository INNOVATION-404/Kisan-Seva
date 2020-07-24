package com.kisanseva.kisanseva.firedb;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Saikrishna on 18/02/2019.
 */

public class DatabaseUtils {
    private static FirebaseDatabase mFirebaseInstance;
    public static FirebaseDatabase getDBinstance(){
        if(mFirebaseInstance==null){
            mFirebaseInstance = FirebaseDatabase.getInstance();
        }
        return mFirebaseInstance;
    }

}
