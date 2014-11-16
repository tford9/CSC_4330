package com.project.smartpumpgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.project.adjcostcalculator.CostCalculator;
import com.project.favorites.FavoritesManager;
import com.project.searching.GasStation;
import com.project.searching.StationRequest;
import com.project.smartpumpgui.R;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * Activity for displaying the Stations Favorites results.
 * 
 * @author SmartPump Team
 * @version 1.0
 * 
 * @see FavoritesManager
 */

public class FavoritesActivity extends ListActivity {

	public static Context context;
	private FavoritesManager manager;
	private ArrayList<GasStation> favoriteStations;
	private double currentLat, currentLng;
	private double MPG;
	static ListView lvFavs;

	/**
	 * Returns the context for this Activity
	 * 
	 * @return context
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * Overrides the Activity onCreate method to perform the required setup for
	 * the favorites results list
	 * 
	 * @param savedInstanceState
	 *            can be used when re-initializing the activity if the app was
	 *            shut down
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites);
		context = getApplicationContext();

		/* Activate Clickable Icon Button */
		ActionBar smartPumpIcon = getActionBar();
		smartPumpIcon.setDisplayHomeAsUpEnabled(true);

		lvFavs = (ListView) findViewById(android.R.id.list);
		manager = new FavoritesManager(context);
		currentLat = this.getIntent().getExtras().getDouble("latitude");
		currentLng = this.getIntent().getExtras().getDouble("longitude");
		MPG = this.getIntent().getExtras().getDouble("MPG");

		ArrayList<String> favId = manager.getFavorites();
		favoriteStations = new ArrayList<GasStation>();
		ArrayList<Map<String, String>> favDetails = new ArrayList<Map<String, String>>();

		System.out.println("Got Ids");
		if (favId != null) {
			for (String id : favId) {
				GasStation s = StationRequest.getStationById(id);
				favoriteStations.add(s);
				favDetails.add(putStation(s.getStationName(), s
						.getStationAddress().getStreet()));
				;
			}
			String[] from = { "Name", "Address" };
			int[] to = { android.R.id.text1, android.R.id.text2 };

			SimpleAdapter adapter = new SimpleAdapter(this, favDetails,
					android.R.layout.simple_list_item_2, from, to);
			lvFavs.setAdapter(adapter);
		}

		lvFavs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setSelected(true);
				GasStation selected = favoriteStations.get(position);
				Intent i = new Intent(getContext(),
						StationDetailsActivity.class);
				i.putExtra("stationSelected", selected);
				i.putExtra("fuelTypeSelected", false);
				i.putExtra("latitude", currentLat);
				i.putExtra("longitude", currentLng);
				i.putExtra("MPG", MPG);
				System.out.println("MPG:" + MPG);
				startActivity(i);
			}
		});
	}

	/**
	 * This Hash Table contained all the information from the saved favorites
	 * Gas Stations.
	 * 
	 * @param name
	 * @param address
	 * 
	 */
	private HashMap<String, String> putStation(String name, String address) {
		HashMap<String, String> newItem = new HashMap<String, String>();
		newItem.put("Name", name);
		newItem.put("Address", address);
		return newItem;
	}

	// -------------------------- OPTIONS MENU----------------------------
	/**
	 * 
	 * Overrides the Activity onCreateOptionMenu method to set up the SmartPump
	 * option menu
	 * 
	 * @param menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.favorites_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 
	 * Overrides the Activity onOptionsItemSelected method, handles clicking the
	 * home button or changing the sort option
	 * 
	 * @param item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return true;
	}

}
