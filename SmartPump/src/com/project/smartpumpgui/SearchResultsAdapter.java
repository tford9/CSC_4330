package com.project.smartpumpgui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.project.searching.AdjCostComparator;
import com.project.searching.DistanceComparator;
import com.project.searching.GasStation;
import com.project.searching.PriceComparator;
import com.project.searching.StationSearchResult;
import com.project.smartpumpgui.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Extends the BaseAdapter class and provides a custom adapter for mapping a
 * StationSearchResult to a list item.
 * 
 * @author SmartPump Team
 * @version 1.0
 * @see SearchResultsActivity
 */
public class SearchResultsAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<StationSearchResult> stationResults;

	/**
	 * Constructs a new SearchResultsAdapter with StationSearchResult objects
	 * and a context.
	 * 
	 * @param results
	 *            an ArrayList of StationSearchResult objects
	 * @param context
	 *            the context of the calling Activity
	 */
	public SearchResultsAdapter(ArrayList<StationSearchResult> results,
			Context context) {
		super();
		this.context = context;
		this.stationResults = results;
	}

/**
* Gets the view of the map in relation to the user
*
* @param position
*			responsible for the position of the user
* @param convertView
*			adjusts the view of the map to the position
* @param parent
*
* @return v
*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = mInflater.inflate(R.layout.search_result_item, null);
		}

		ImageView logo = (ImageView) v.findViewById(R.id.stationLogo);
		TextView name = (TextView) v.findViewById(R.id.stationName);
		TextView distance = (TextView) v.findViewById(R.id.distanceAway);
		TextView pumpPrice = (TextView) v.findViewById(R.id.pumpPrice);
		TextView adjustedCost = (TextView) v.findViewById(R.id.adjustedCost);

		StationSearchResult r = stationResults.get(position);
		GasStation s = r.getStation();

		int logoId = s.getLogoId(context);
		if (logoId != 0) {
			logo.setImageDrawable(context.getResources().getDrawable(logoId));
		} else {
			logo.setImageDrawable(context.getResources().getDrawable(
					R.drawable.gasstation));
		}
		name.setText(s.getStationName());
		distance.setText(String.valueOf(s.getDistance()) + " mi");

		NumberFormat currency = NumberFormat.getCurrencyInstance();
		double fuelPrice = s.getSelectedFuelPrice().getPrice();
		double adjustCost = r.getAdjustedCost();
		pumpPrice
				.setText(fuelPrice == 0.0 ? "N/A" : currency.format(fuelPrice));
		adjustedCost.setText(adjustCost == 0.0 ? "N/A" : currency
				.format(adjustCost));

		return v;
	}

/**
* Sorts the gas stations results by distance 
*
* @return this
*/
	public SearchResultsAdapter DistanceSort() {
		Collections.sort(stationResults, new DistanceComparator());
		return this;
	}

/**
* Sorts the gas station results by adjusted cost
*
* @return this
*/
	public SearchResultsAdapter AdjSort() {
		Collections.sort(stationResults, new AdjCostComparator());
		return this;
	}

/**
* Sorts the gas stations results by price 
*
* @return this
*/
	public SearchResultsAdapter PriceSort() {
		Collections.sort(stationResults, new PriceComparator());
		return this;
	}

/**
* Gets the count
*
* @return stationResults.size()
*/
	@Override
	public int getCount() {
		return stationResults.size();
	}

/**
* Gets the item
*
* @param position
* 			can be used for marking the position of a gas station on the map
*
* @return this
*/
	@Override
	public Object getItem(int position) {
		return stationResults.get(position);
	}

/**
* Gets the item id which may be used for further functionality in the future
*
* @param arg0
*
* @return 0
*/
	@Override
	public long getItemId(int arg0) {
		return 0;
	}
}
