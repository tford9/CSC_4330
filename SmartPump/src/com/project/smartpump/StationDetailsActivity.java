package com.project.smartpump;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.classes.Calculations;
import com.project.classes.FavoritesManager;
import com.project.classes.FuelPrice;
import com.project.classes.GasStation;
import com.project.classes.StationSearchResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class StationDetailsActivity extends Activity {
    public static Context context;
    private GoogleMap gMap;
    private static RelativeLayout priceSummary, 
                                  fuelSelection;
    private static TextView name, 
                            address, 
                            cityStateZip, 
                            phone, 
                            price,
                            adjustedPrice, 
                            distance;
    private static Button chooseStation;
    private static Spinner fuelType;
    private boolean spinnerInitialized = false;
    
    private FavoritesManager favoriteManager;
    private boolean isFavorite;
    
    private GasStation station;
    private double currentLat, 
                   currentLng, 
                   stationLat, 
                   stationLng;
    private double MPG, 
                   distanceAway,
                   adjustedCost;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_details_view);
        context = getApplicationContext();
        favoriteManager = new FavoritesManager(context);

        gMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.miniMap)).getMap();
        gMap.setMyLocationEnabled(true);

        priceSummary = (RelativeLayout) findViewById(R.id.priceSummary);
        fuelSelection = (RelativeLayout) findViewById(R.id.fuelSelection);
        fuelType = (Spinner) findViewById(R.id.fuelType);

        name = (TextView) findViewById(R.id.stationName);
        address = (TextView) findViewById(R.id.stationAddress);
        cityStateZip = (TextView) findViewById(R.id.cityStateZip);
        // phone = (TextView)findViewById(R.id.stationPhone);
        price = (TextView) findViewById(R.id.stationPrice);
        adjustedPrice = (TextView) findViewById(R.id.stationAdjustedCost);
        distance = (TextView) findViewById(R.id.distanceAway);
        chooseStation = (Button) findViewById(R.id.ChooseDestination);
        chooseStation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://maps.google.com/maps?" + "saddr="
                                + currentLat + "," + currentLng + "&daddr="
                                + stationLat + "," + stationLng));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        // Get station and price information from intent
        boolean fuelSelected = this.getIntent().getExtras()
                .getBoolean("fuelTypeSelected");
        if (fuelSelected)
        {
            StationSearchResult selected = this.getIntent().getExtras().getParcelable("resultSelected");
            station  = selected.getStation();
            adjustedCost = selected.getAdjustedCost();
        }
        else
        {
            station = this.getIntent().getExtras().getParcelable("stationSelected");
            MPG = this.getIntent().getExtras().getDouble("MPG");
        }
        
        // Get current location
        currentLat = this.getIntent().getExtras().getDouble("latitude");
        currentLng = this.getIntent().getExtras().getDouble("longitude");

        // Add marker to mini map
        stationLat = station.getCoords().latitude;
        stationLng = station.getCoords().longitude;
        String title = station.getStationName();
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(stationLat, stationLng)).title(title)
                .icon(BitmapDescriptorFactory.defaultMarker());
        gMap.addMarker(marker);

        // Adjust camera to marker location
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(new LatLng(
                stationLat, stationLng), 10);
        gMap.animateCamera(location);

        // Compute distance if necessary
        distanceAway = station.getDistance();
        if (distanceAway == 0.0) {
            Location start = new Location("Start");
            start.setLatitude(currentLat);
            start.setLongitude(currentLng);
            Location finish = new Location("Finish");
            finish.setLatitude(stationLat);
            finish.setLongitude(stationLng);
            // distanceTo() returns distance in meters, multiplication to
            // convert to miles
            double d = start.distanceTo(finish) * 0.000621371;
            distanceAway = Math.round(d * 100.0) / 100.0;
        }

        // Add station details to display
        name.setText(station.getStationName());
        address.setText(station.getStationAddress().getStreet());
        cityStateZip.setText(station.getStationAddress().getCity() + ", " + station.getStationAddress().getState()
                + " " + station.getStationAddress().getZip());
        // phone.setText(station.getPhoneNumber());
        distance.setText(String.valueOf(distanceAway) + " miles");

        // Price information
        if (fuelSelected) 
        {
            fuelSelection.setVisibility(View.GONE);
            priceSummary.setVisibility(View.VISIBLE);
            double fuelPrice = station.getSelectedFuelPrice().getPrice();
            setPriceDisplays(fuelPrice, adjustedCost);
        } 
        else 
        {
            // Hide price summary from display
            priceSummary.setVisibility(View.GONE);
            fuelSelection.setVisibility(View.VISIBLE);

            // Set up spinner menu for selecting fuel type
            ArrayAdapter<CharSequence> adapter = ArrayAdapter
                    .createFromResource(this, R.array.fuel_types,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fuelType.setAdapter(adapter);
            fuelType.setSelection(0);
            fuelType.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                        int position, long id) {
                    if (!spinnerInitialized) {
                        spinnerInitialized = true;
                    } else {
                        fuelSelection.setVisibility(View.GONE);
                        priceSummary.setVisibility(View.VISIBLE);
                        switch (position) {
                        case 1:
                            setPriceDisplays(station.getRegPrice());
                            break;
                        case 2:
                            setPriceDisplays(station.getMidPrice());
                            break;
                        case 3:
                            setPriceDisplays(station.getPrePrice());
                            break;
                        case 4:
                            setPriceDisplays(station.getDieselPrice());
                            break;
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }
    
    // -----------------------PRICE DISPLAY HELPERS--------------------------
    private void setPriceDisplays(FuelPrice price) {
        double fuelPrice = price.getPrice();
        double adjustPrice = MPG == 0 ? 0.0 : Calculations.calculate(MPG,
                fuelPrice, distanceAway, 15);
        setPriceDisplays(fuelPrice, adjustPrice);

    }

    private void setPriceDisplays(double fuelPrice, double adjustPrice) {
        // Determine price information
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        String sPrice, sAdjustPrice;
        if (fuelPrice == 0.0) {
            sPrice = "Not Available";
            price.setTextSize(12);
        } else {
            sPrice = currency.format(fuelPrice);
        }
        if (adjustPrice == 0.0) {
            sAdjustPrice = "Not Available";
            adjustedPrice.setTextSize(12);
        } else {
            sAdjustPrice = currency.format(adjustPrice);
        }

        // Add station details to display
        price.setText(sPrice);
        adjustedPrice.setText(sAdjustPrice);
    }
    
    // -------------------------- OPTIONS MENU----------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);

        // Check if the station is already saved as a favorite
        isFavorite = favoriteManager.checkForFavorite(station);
        if (isFavorite) {
            // If it is a favorite, display yellow star
            Drawable favorite = getResources().getDrawable(
                    android.R.drawable.btn_star_big_on);
            MenuItem star = menu.findItem(R.id.favorite);
            star.setIcon(favorite);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.favorite:
            String message = isFavorite ? "Remove station as a favorite?"
                    : "Save station as a favorite?";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    if (!isFavorite) {
                                        // Add to saved favorites
                                        favoriteManager.addFavorite(station);
                                        // Replace gray star with yellow star
                                        Drawable favorite = getResources()
                                                .getDrawable(
                                                        android.R.drawable.btn_star_big_on);
                                        item.setIcon(favorite);
                                    } else {
                                        // Remove from saved favorites
                                        favoriteManager.removeFavorite(station);
                                        // Replace yellow star with gray star
                                        Drawable favorite = getResources()
                                                .getDrawable(
                                                        android.R.drawable.btn_star_big_off);
                                        item.setIcon(favorite);
                                    }
                                }
                            })
                    .setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}