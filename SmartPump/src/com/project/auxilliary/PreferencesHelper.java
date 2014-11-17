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

    /**
     * Constructs a new preferences helper with the context of the calling activity.
     * 
     * @param context context of the calling activity
     */
    public PreferencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("SmartPumpData",context.MODE_PRIVATE);    
        this.editor = sharedPreferences.edit();
    }

    /**
     * Retrieves the string value of a particular preference.
     * 
     * @param key the key for the reference to retrieve
     * @return the string value of the preference
     */
    public String GetPreferences(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * Saves a preference with a String key and value
     * 
     * @param key the key of the value to be stored
     * @param value the value to be stored
     */
    public void SavePreferences(String key, String value) {
        editor.putString(key, value);    
        editor.commit();  
    }
    
    /**
     * Retrieves a set of strings associated with the specified key.
     * 
     * @param key key of the preference set
     * @return Set<String> associated with the key
     */
    public Set<String> GetPreferenceStringSet(String key) {
        return sharedPreferences.getStringSet(key, new HashSet<String>());
    }
    
    /**
     * Saves a set of strings to the specified key
     * 
     * @param key key for the preference
     * @param set Set<String> to be assigned to the key
     */
    public void SavePreferenceStringSet(String key, Set<String> set) {
        editor.putStringSet(key, set);
        editor.commit();
    }
} 