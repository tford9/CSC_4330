package com.project.smartpumpgui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.project.adjcostcalculator.CostCalculator;
import com.project.auxilliary.PreferencesHelper;
import com.project.searching.GasStation;
import com.project.searching.StationRequest;
import com.project.searching.StationSearchResult;
import com.project.smartpumpgui.R;

/**
 * Note that Main Activity also GUI initiator.
 * 
 * @author SmartPump Team
 * @version 1.0 Controls the flow of SmartPump application execution.
 * 
 * @Override {@link LocationListener}
 * @Override {@literal GoogleApiClient.ConnectionCallbacks}
 * @Override {@literal GoogleApiClient.OnConnectionFailedListener}
 */

public class MainActivity extends Activity implements LocationListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {

	public static Context context;

	/** UI elements */
	static EditText numGallons, address;
	static Button searchWithAddress, searchWithLocation;
	static Spinner fuelType;
	private String provider;

	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private Location currentLocation;
	private double latitude, longitude;
	private double MPG;

	/**
	 * Returns the context for this Activity
	 * 
	 * @return context
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * onCreate
	 * <p>
	 * Generates the context and sets it to the savedInstanceState it also
	 * maintains shared preferences.
	 * 
	 * @param savedInstanceState
	 *            (required)
	 * @Override
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		context = getApplicationContext();

		/** Verifies that there is saved vehicle data */
		PreferencesHelper prefs = new PreferencesHelper(context);
		MPG = 0.0;
		String vId = prefs.GetPreferences("VehicleID");
		String mpg = vId.equals("") ? "" : prefs.GetPreferences("VehicleMPG");
		if (mpg.equals("")) {
			/** Prompt user to save a vehicle profile */
			profileErrorAlert();
		} else {
			MPG = Double.parseDouble(mpg);
		}

		/** Sets up spinner menu for selecting fuel type */
		fuelType = (Spinner) findViewById(R.id.fuelType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.fuel_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fuelType.setAdapter(adapter);
		fuelType.setSelection(0);

		/** Set Google API client needed for location management */
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addApi(LocationServices.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		/** Functionality for handling searches */
		address = (EditText) findViewById(R.id.address);
		numGallons = (EditText) findViewById(R.id.estimatedGallons);
		searchWithAddress = (Button) findViewById(R.id.searchWithAddress);
		searchWithLocation = (Button) findViewById(R.id.searchWithLocation);
		searchWithAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LatLng coords = StationRequest.getGeoCoordsFromAddress(context,
						address.getText().toString());
				if (coords == null) {
					couldNotFindLocationAlert();
				} else {
					getResults(coords.latitude, coords.longitude);
				}
			}
		});
		searchWithLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCoords();
				if (latitude == -1.0 || longitude == -1.0) {
					System.out.println("could not get coords");
					couldNotFindLocationAlert();
				} else {
					getResults(latitude, longitude);
				}
			}
		});
	}

	/** ---------------------------SEARCH HELPERS------------------------------- */
	private void profileErrorAlert() {
		String message = "It seems that you do not have a complete vehicle profile. Adjusted pump prices"
				+ " cannot be computed without the average MPG of your vehicle. Would you like to add a vehicle?";
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(context,
										CarInfoActivity.class);
								startActivity(intent);
								finish();
							}
						})
				.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void couldNotFindLocationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Could not find location").setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						address.setText("");
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void getResults(final double lat, final double lng) {
		if (fuelType.getSelectedItemPosition() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please select a fuel type").setPositiveButton(
					"OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		} else if ((numGallons.getText().toString()).equals("")) {
			String message = "If you wish to see adjusted fuel costs, please cancel this search"
					+ " and enter the number of gallons of fuel you expect to purchase.";
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(message)
					.setPositiveButton("CONTINUE",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									getResults(lat, lng, 0.0);
								}
							})
					.setNegativeButton("CANCEL",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			double gallons = Double
					.parseDouble(numGallons.getText().toString());
			getResults(lat, lng, gallons);
		}
	}

	private void getResults(double lat, double lng, double gallons) {
		/** Get selected fuel type */
		String selectedFuelType = fuelType.getSelectedItem().toString();

		/** Make search for stations */
		ArrayList<GasStation> stations = StationRequest.getNearbyGasStations(
				lat, lng, 10.0, selectedFuelType);

		/** Calculate adjusted prices and package into search results */
		ArrayList<StationSearchResult> stationResults = new ArrayList<StationSearchResult>();
		if (gallons == 0.0) {
			for (GasStation s : stations) {
				stationResults.add(new StationSearchResult(s, 0.0));
			}
		} else {
			for (GasStation s : stations) {
				double adjustedPrice = MPG == 0 ? 0.0 : CostCalculator
						.calculate(MPG, s.getSelectedFuelPrice().getPrice(),
								s.getDistance(), gallons);
				stationResults.add(new StationSearchResult(s, adjustedPrice));
			}
		}

		/** Stop listening for location */
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);

		/** Show search results */
		Intent i = new Intent(getContext(), SearchResultsActivity.class);
		i.putParcelableArrayListExtra("data", stationResults);
		i.putExtra("latitude", lat);
		i.putExtra("longitude", lng);
		startActivity(i);
	}

	/** ------------------------LOCATION MANAGEMENT---------------------------- */
	public void getCoords() {
		/**
		 * If current location has still not been found, attempt to request last
		 * location
		 */
		if (currentLocation == null) {
			currentLocation = LocationServices.FusedLocationApi
					.getLastLocation(mGoogleApiClient);
		}
		/** If still null (should be rare), set latitude and longitude to -1 */
		if (currentLocation == null) {
			longitude = -1.0;
			latitude = -1.0;
		} else {
			longitude = currentLocation.getLongitude();
			latitude = currentLocation.getLatitude();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {
		mLocationRequest = LocationRequest.create();
		mLocationRequest
				.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		mLocationRequest.setInterval(3000); // update every 3 seconds

		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onLocationChanged(Location loc) {
		currentLocation = loc;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Implement inherited method
	}

	@Override
	public void onConnectionSuspended(int i) {
		// TODO Implement inherited method
	}

	/** -------------------------- OPTIONS MENU---------------------------- */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.homescreen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/** Handle item selection */
		switch (item.getItemId()) {
		case R.id.favorites:
			System.out.println("star clicked");
			getCoords();
			Intent i = new Intent(getContext(), FavoritesActivity.class);
			i.putExtra("latitude", latitude);
			i.putExtra("longitude", longitude);
			i.putExtra("MPG", MPG);
			startActivity(i);
			return true;
		case R.id.newVehicle:
			System.out.println("New Vehicle Create Selected");
			Intent I = new Intent(getContext(), CarInfoActivity.class);
			startActivity(I);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
