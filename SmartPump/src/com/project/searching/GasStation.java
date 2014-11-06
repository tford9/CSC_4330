package com.project.searching;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

 /**
 * Stores the data associated with a gas station. 
 * Implements the Parcelable interface, allowing it 
 * to be added to the Bundle of extended data passed
 * between activities.
 * 
 * @author SmartPump Team
 * @version 1.0
 */
public class GasStation implements Parcelable {
    
    private String stationId;
    private String stationName;
    private LatLng coords;
    private double distance; //in Miles
    private StationAddress stationAddress;
    private FuelPrice selectedFuelPrice;
    private FuelPrice regPrice;
    private FuelPrice midPrice;
    private FuelPrice prePrice;
    private FuelPrice dieselPrice;

    /**
     * Constructs a new GasStation with a myGasFeed id, name, coordinates, street address, FuelPrice, and
     * distance from start location. Used when the data was obtained for a user search request
     * 
     * @param stationId the myGasFeed ID for the station
     * @param stationName the name of the station
     * @param coords the (latitude, longitude) coordinate of the station
     * @param address the street address of the station
     * @param selectedPrice the FuelPrice (based on the user's selected fuel type)
     * @param distance the distance of the station from the start point used to make the myGasFeed request
     */
    GasStation(String stationId, String stationName, LatLng coords, StationAddress address, 
            FuelPrice selectedPrice, double distance)
    {
        this.stationId = stationId;
        this.stationName = stationName;
        this.coords = coords;
        this.stationAddress = address;
        this.selectedFuelPrice = selectedPrice;
        this.distance = distance;
    }
    
    /**
     * Constructs a new GasStation with a myGasFeed id, name, coordinates, street address, and the FuelPrice
     * for all of the fuel types (reg, mid, pre and diesel). Used when the data was obtained by retrieving a
     * station by its myGasFeed id.
     * 
     * @param stationId the myGasFeed ID for the station
     * @param stationName the name of the station
     * @param coords the (latitude, longitude) coordinate of the station
     * @param address the street address of the station
     * @param reg the FuelPrice for regular fuel
     * @param mid the FuelPrice for mid fuel
     * @param pre the FuelPrice for premium fuel
     * @param diesel the FuelPrice for diesel fuel
     */
    GasStation(String stationId, String stationName, LatLng coords, StationAddress address, 
            FuelPrice reg, FuelPrice mid, FuelPrice pre, FuelPrice diesel)
    {
        this.stationId = stationId;
        this.stationName = stationName;
        this.coords = coords;
        this.stationAddress = address;
        this.regPrice = reg;
        this.midPrice = mid;
        this.prePrice = pre;
        this.dieselPrice = diesel;
    }
         
    /**
     * Constructs a new GasStation from a Parcel object. This is a required
     * method from the Parcelable interface.
     * 
     * @param in the Parcel object to extract station data from
     */
    public GasStation(Parcel in) {
        stationId = in.readString();
        stationName = in.readString();
        distance = in.readDouble();
        
        coords = in.readParcelable(LatLng.class.getClassLoader());
        stationAddress = in.readParcelable(StationAddress.class.getClassLoader());
        selectedFuelPrice = in.readParcelable(FuelPrice.class.getClassLoader());
        regPrice = in.readParcelable(FuelPrice.class.getClassLoader());
        midPrice = in.readParcelable(FuelPrice.class.getClassLoader());
        prePrice = in.readParcelable(FuelPrice.class.getClassLoader());
        dieselPrice = in.readParcelable(FuelPrice.class.getClassLoader());
    }

    /**
     * Returns the myGasFeed ID of this station
     * 
     * @return the stationId 
     */
    public String getStationId()
    {
        return this.stationId;
    }
    
    /**
     * Returns the name of this station
     * 
     * @return stationName
     */
    public String getStationName()
    {
        return this.stationName;
    }
    
    /**
     * Returns the (latitude, longitude) coordinate of this station
     * 
     * @return coords
     */
    public LatLng getCoords()
    {
        return this.coords;
    }
    
    /**
     * Returns the street address of this station
     * 
     * @return stationAddress
     * @see StationAddress
     */
    public StationAddress getStationAddress()
    {
        return this.stationAddress;
    }
    
    /**
     * Sets the distance of this station from a start location
     * 
     * @param value the distance in miles between the station and a start location
     */
    public void setDistance(double value)
    {
        this.distance = value;
    }
    
    /**
     * Returns the distance in miles between this station and the start location
     * 
     * @return the distance
     */
    public Double getDistance()
    {
        return this.distance;
    }
    
    /**
     * Returns the FuelPrice of the selected fuel type (null if the station was retrieved by ID).
     * 
     * @return selectedFuelPrice
     * @see FuelPrice
     */
    public FuelPrice getSelectedFuelPrice()
    {
        return this.selectedFuelPrice;
    }
    
    /**
     * Returns the FuelPrice of regular fuel from this station (null if the station was retrieved from a search).
     * 
     * @return regPrice
     * @see FuelPrice
     */
    public FuelPrice getRegPrice()
    {
        return this.regPrice;
    }   

    /**
     * Returns the FuelPrice of mid fuel from this station (null if the station was retrieved from a search).
     * 
     * @return midPrice
     * @see FuelPrice
     */
    public FuelPrice getMidPrice()
    {
        return this.midPrice;
    }
    
    /**
     * Returns the FuelPrice of premium fuel from this station (null if the station was retrieved from a search).
     * 
     * @return prePrice
     * @see FuelPrice
     */
    public FuelPrice getPrePrice()
    {
        return this.prePrice;
    }
    
    /**
     * Returns the FuelPrice of diesel fuel from this station (null if the station was retrieved from a search).
     * 
     * @return dieselPrice
     * @see FuelPrice
     */
    public FuelPrice getDieselPrice()
    {
        return this.dieselPrice;
    }
    
    /**
     * Returns the resource ID of the associated logo icon for this station. This ID is
     * used to retrieve the logo from the SmartPump resources for display in the
     * search results or station details.
     * 
     * @param context the Context of the calling activity
     * 
     * @return the integer resource ID of the logo image
     */
    public int getLogoId(Context context)
    {
        String name = getStationName();
        name = (name.replace(" ","")).toLowerCase();
        int logoId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return logoId;
    }
    
    /**
     * Returns bit masks which can be used to represent significant objects for this Parcel.
     * Required implementation, but functionality is not currently necessary.
     * 
     * @return bit masks for the Parcel
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens this station's data into the destination Parcel
     * 
     * @param dest the destination Parcel
     * @param flags additional flags specifying how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getStationId());
        dest.writeString(getStationName());
        dest.writeDouble(getDistance());
        
        dest.writeParcelable(getCoords(), flags);
        dest.writeParcelable(getStationAddress(), flags);
        dest.writeParcelable(getSelectedFuelPrice(), flags);
        dest.writeParcelable(getRegPrice(), flags);
        dest.writeParcelable(getMidPrice(), flags);
        dest.writeParcelable(getPrePrice(), flags);
        dest.writeParcelable(getDieselPrice(),  flags);
    }

    /**
     * Implemented interface for generating GasStation instances from a Parcel
     */
    public static final Parcelable.Creator<GasStation> CREATOR = new Parcelable.Creator<GasStation>() 
    {
        public GasStation createFromParcel(Parcel in) 
        {
            return new GasStation(in);
        }

        @Override
        public GasStation[] newArray(int size) 
        {
            return new GasStation[size];
        }
    };
    
    @Override
    public String toString() 
    {
        StringBuilder result = new StringBuilder();
        String nl = System.getProperty("line.separator");

        result.append("Name: " + getStationName() + nl);
        result.append("Station Id:" + getStationId() + nl);
        result.append(nl);
        return result.toString();
    }
}