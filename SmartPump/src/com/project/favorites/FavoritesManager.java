package com.project.favorites;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.project.auxilliary.PreferencesHelper;
import com.project.searching.GasStation;

import android.content.Context;
import android.content.SharedPreferences;

/**
* Stores a fuel station as a "favorite". Smartpump will save the station's
* MyGasFeedID to the user's device. After retrieving the station's ID, MyGasFeed will
* return the price per gallon for all the available grades at the station.
*
* @author SmartPump Team
* @version 1.0
* @see FavoritesActivity
*/

public class FavoritesManager 
{
    private PreferencesHelper helper;
    private Set<String> favorites;
    private String favoritesKey = "stationId";
    
    /**
    *  Contructs Favorites Manager from Context
    *
    *  @param context The Context
    */
    public FavoritesManager(Context context)
    {
        this.helper = new PreferencesHelper(context);
        this.favorites = helper.GetPreferenceStringSet(favoritesKey);
    }

    /**
    *  Adds the GasStation Object to Favorites
    *
    *  @param station The Gas Station
    */
    public void addFavorite(GasStation station)
    {
        favorites.add(station.getStationId());
        helper.SavePreferenceStringSet(favoritesKey, favorites);
    }

    /**
    *  Removes the GasStation Object from Favorites
    *
    *  @param station The Gas Station
    */
    public void removeFavorite(GasStation station)
    {
        favorites.remove(station.getStationId());
        helper.SavePreferenceStringSet(favoritesKey, favorites);
    }

    /**
    *   Returns True or False if the GasStation is contained in Favorites.
    *
    *  @param station The Gas Station
    *
    *  @return True or False 
    */
    public boolean checkForFavorite(GasStation station)
    {
        return favorites.contains(station.getStationId());
    }
    
    /**
    *  Returns the whole list of Gas Stations that are in Favorites 
    *
    *   @return ArrayList of Gas Stations
    */
    public ArrayList<String> getFavorites()
    {
        if (favorites != null)
        {
            return new ArrayList<String> (favorites);
        }
        
        return null;
    }
}
