package com.project.smartpumpgui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.project.searching.GasStation;
import com.project.searching.StationRequest;
import com.project.searching.StationSearchResult;
import com.project.smartpump.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * Activity for displaying station search results in a ListView.
 * The custom SearchResultsAdapter is used to map a search result to a list item,
 * and options are provided to sort the list by distance, pump price, or adjusted cost.
 * 
 * @author SmartPump Team
 * @version 1.0
 * 
 * @see SearchResultsAdapter
 */
public class SearchResultsActivity extends Activity 
{
	public static Context context;
    private ArrayList<StationSearchResult> stations;
    private double currentLat, currentLng;
    static ListView results;

    SearchResultsAdapter adapter;


    /**
     * Returns the context for this Activity
     * @return context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * Overrides the Activity onCreate method to perform the
     * required set-up for the search results list.
     * 
     * @param savedInstanceState can be used when re-initializing the activity if the app was shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_view);
        context = getApplicationContext();
        
        // Activate clickable home Icon Button
        ActionBar smartPumpIcon = getActionBar();
        smartPumpIcon.setDisplayHomeAsUpEnabled(true);
        
        // Get a handle for the ListView
        results = (ListView)findViewById(android.R.id.list);
        
        // Extract the extended data from the Intent
        stations = this.getIntent().getExtras().getParcelableArrayList("data");
        currentLat = this.getIntent().getExtras().getDouble("latitude");
        currentLng = this.getIntent().getExtras().getDouble("longitude");
        
        // Set up the adapter using the custom SearchResultsAdapter,
        // with default sort option set to sort by distance
        adapter = new SearchResultsAdapter(stations, context);
        adapter.DistanceSort();
    	adapter.notifyDataSetChanged();
        
        results.setAdapter(adapter);  
        
        // Assign a listener to handle the user selecting a station from the results list
        results.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
            {
            	// A station was selected, create an Intent to start the StationDetailsActivity, 
            	// and add the station details along with other relevant information as extended data
                view.setSelected(true);
                StationSearchResult selected = stations.get(position);
                Intent i = new Intent(getContext(), StationDetailsActivity.class);
                i.putExtra("resultSelected", selected);
                i.putExtra("fuelTypeSelected", true);
                i.putExtra("latitude", currentLat);
                i.putExtra("longitude", currentLng);
                startActivity(i);
            }
        });

    }
    
    // -------------------------- OPTIONS MENU----------------------------

    /**
     * Overrides the Activity onCreateOptionsMenu method to set up the SmartPump options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.results_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Overrides the Activity onOptionsItemSelected method, handles clicking the home button
     * or changing the sort option.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case android.R.id.home:
        	this.finish();
        	return true;
        case R.id.adj_sort:
        	adapter.AdjSort();
        	adapter.notifyDataSetChanged();
            return true;
        case R.id.dis_sort:
        	adapter.DistanceSort();
        	adapter.notifyDataSetChanged();
        	return true;
        case R.id.price_sort:
        	adapter.PriceSort();
        	adapter.notifyDataSetChanged();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}

