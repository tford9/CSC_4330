package com.project.auxilliary;

import java.util.HashSet;
import java.util.Set;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
* 
* <p> Serves as an interaction layer between other classes and android devices on
* board shared preferences. Contains methods for all currently used shared
* preferences interactions.</p>
*   
* @author SmartPump Team
* @version 1.0
*/

public class PreferencesHelper {

    private SharedPreferences sharedPreferences;
    private Editor editor;

    
    public PreferencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("SmartPumpData",context.MODE_PRIVATE);    
        this.editor = sharedPreferences.edit();
    }

    public String GetPreferences(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void SavePreferences(String key, String value) {
        editor.putString(key, value);    
        editor.commit();  
    }
    
    public Set<String> GetPreferenceStringSet(String key) {
        return sharedPreferences.getStringSet(key, new HashSet<String>());
    }
    
    public void SavePreferenceStringSet(String key, Set<String> set) {
        editor.putStringSet(key, set);
        editor.commit();
    }
} 