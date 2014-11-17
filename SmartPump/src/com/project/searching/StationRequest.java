package com.project.searching;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

/**
* Provides helper methods for performing a gas station search 
* and for looking up a station by its myGasFeed station ID.
* 
* <p>
* All methods are static, so a StationRequest object does not need to be created to call a request method.
* Gas station requests are completed with an HTTP GET to the myGasFeed API, which returns JSON responses.
* </p>
* 
* @author SmartPump Team
* @version 1.0
* @see <a href="http://www.mygasfeed.com/keys/intro">myGasFeed</a>
*/
public class StationRequest {
    private static String myGasFeedDevkey = "rfej9napna";
    private static String myGasFeedkey = "jgzifo2p0g";
    
    private static String myGasFeedDevUrl = "http://devapi.mygasfeed.com/";
    private static String myGasFeedUrl = "http://api.mygasfeed.com/";
    
    private static String requestedFuelType;
    
    /**
     * Transforms a street address into a (latitude, longitude) coordinate.
     * 
     * @param c the Context of the calling Activity, required to construct a GeoCoder
     * @param address the address string to be geocoded
     * @return the LatLng (coordinate) of the address
     * @throws IOException if the network is unavailable
     */
    public static LatLng getGeoCoordsFromAddress(Context c, String address)
    {
        Geocoder geocoder = new Geocoder(c);
        List<Address> addresses;
        try 
        {
            addresses = geocoder.getFromLocationName(address, 1);
            if(addresses.size() > 0)
            {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                System.out.println(latitude);
                System.out.println(longitude);
                return new LatLng(latitude, longitude);
            }
            else
            {
                return null;
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * Retrieves gas stations from myGasFeed which match the provided search parameters.
     * 
     * @param latitude the latitude of the location around which to search
     * @param longitude the longitude of the location around which to search
     * @param radius the distance in miles radius over which to search
     * @param fuelType the desired fuel type (reg, mid, pre or diesel)
     * @return ArrayList of GasStation objects
     * @throws MalformedURLException if an invalid request URL is generated
     * @see #getJson(URL requestUrl)
     * @see #mapJsonToStation(JSONObject station, boolean requestById) 
     */
    public static ArrayList<GasStation> getNearbyGasStations(double latitude, double longitude, double radius, String fuelType)
    {
        String sortby = "Price";
        StringBuilder url = new StringBuilder(myGasFeedUrl);
        url.append("stations/radius/");
        url.append(latitude + "/" + longitude + "/" + radius + "/");
        url.append(fuelType + "/" + sortby + "/");
        url.append(myGasFeedkey + ".json");
        
        requestedFuelType = fuelType;
        
        JSONArray stations = new JSONArray();
        try 
        {
            URL requestUrl = new URL(url.toString());
            JSONObject stationInfo = getJson(requestUrl);
            stations = (JSONArray) stationInfo.get("stations"); 
        } 
        catch (MalformedURLException e1) 
        {
            e1.printStackTrace();
        }
        
        Iterator i = stations.iterator();
        ArrayList<GasStation> gasStations = new ArrayList<GasStation>();
        while(i.hasNext())
        {
            JSONObject station = (JSONObject) i.next();
            gasStations.add(mapJsonToStation(station, false));
        }
        
        return gasStations;
    }
    
    /**
     * Retrieves details for the gas station with the provided myGasFeed station id.
     * 
     * @param id the myGasFeed station id
     * @return GasStation object
     * @throws MalformedURLException if an invalid request URL is generated
     * @see #getJson(URL requestUrl)
     * @see #mapJsonToStation(JSONObject station, boolean requestById)
     */
    public static GasStation getStationById(String id)
    {
        StringBuilder url = new StringBuilder(myGasFeedUrl);
        url.append("stations/details/" + id);
        url.append("/" + myGasFeedkey + ".json");
        
        try 
        {
            URL requestUrl = new URL(url.toString());
            JSONObject stationInfo = getJson(requestUrl);
            JSONObject details = (JSONObject) stationInfo.get("details");
            
            return mapJsonToStation(details, true);
        } 
        catch (MalformedURLException e1) 
        {
            e1.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Executes the HTTP request specified by the provided URL, parses the JSON response,
     * and returns the parsed JSON object.
     * 
     * @param requestUrl the URL specifying where to retrieve the data
     * @return JSONObject with the parsed response
     * @throws InterruptedException if the current thread was interrupted while waiting for the 
     * AsyncTask to complete
     * @throws ExecutionException if the AyncTask threw an exception
     * @throws ParseException if the JSON response could not be parsed
     * @see GetJsonTask
     */
    private static JSONObject getJson(URL requestUrl)
    {
        String json = "";
        try 
        {
            GetJsonTask getJson = new GetJsonTask();
            json = getJson.execute(requestUrl).get();
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        } 
        catch (ExecutionException e) 
        {
            e.printStackTrace();
        }
        
        JSONParser parser = new JSONParser();
        Object responseObject;
        try 
        {
            responseObject = parser.parse(json);
            return (JSONObject) responseObject;
        } 
        catch (ParseException e) 
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Returns a GasStation object representation of a single JSONObject from myGasFeed.
     * A GasStation includes its id, name, street address (as a StationAddress object), 
     * coordinates, and fuel price information (as a FuelPrice object).
     * 
     * @param station the JSONObject representation of the gas station
     * @param requestById indicates if the station was retrieved by id. The JSON
     * keys are different depending on whether the station was retrieved by id or requested 
     * through a search.
     * @return GasStation object
     * @see #getPriceValue(JSONObject s, String priceKey, String dateKey)
     * @see GasStation
     * @see FuelPrice
     * @see StationAddress
     */
    private static GasStation mapJsonToStation(JSONObject station, boolean requestById)
    {
        String id = (String) station.get("id");
        
        //Get address details
        String address = (String) station.get("address");
        String city = (String) station.get("city");
        String state = (String) station.get("region");
        String zip = (String) station.get("zip");
        StationAddress stationAddress = new StationAddress(address, city, state, zip);
        
        //Get station coordinates
        double lat = Double.parseDouble((String) station.get("lat"));
        double lng = Double.parseDouble((String) station.get("lng"));
        LatLng coords = new LatLng(lat,lng);

        if(requestById)
        {
            String name = (String) station.get("station_name");
            FuelPrice reg = getPriceValue(station, "reg_price", "reg_date");
            FuelPrice mid = getPriceValue(station, "mid_price", "mid_date");
            FuelPrice pre = getPriceValue(station, "pre_price", "pre_date");
            FuelPrice diesel = getPriceValue(station, "diesel_price", "diesel_date");
            return new GasStation(id, name, coords, stationAddress, reg, mid, pre, diesel);
        }
        else
        {
            String name = (String) station.get("station");
            /** Testing with MyGasFeed update to response format */
            String priceKey = requestedFuelType + "_price";
            String dateKey = requestedFuelType + "_date";
            FuelPrice price = getPriceValue(station, priceKey, "date");
            String distanceInfo = (String) station.get("distance");
            String[] distanceParts = distanceInfo.split(" ");
            double distance = Double.parseDouble(distanceParts[0]);
            return new GasStation(id, name, coords, stationAddress, price, distance);
        }
    }

    /**
     * Helper method which extracts FuelPrice from a gas station JSONObject. A FuelPrice
     * object includes the price value and the last date that the price value was updated.
     * 
     * @param s the station JSONObject
     * @param priceKey the hash key for the price value
     * @param dateKey the hash key for the last update
     * @return FuelPrice from the given station
     */
    private static FuelPrice getPriceValue(JSONObject s, String priceKey, String dateKey)
    {
        String sPrice = (String)s.get(priceKey);
        double price = sPrice.equals("N/A")? 0.0 : Double.parseDouble(sPrice);
        String lastUpdate = (String) s.get(dateKey);
        return new FuelPrice(price, lastUpdate);
    }


}
