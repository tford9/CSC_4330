package com.project.searching;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stores the street address parts of a gas station. 
 * This class implements the Parcelable interface, which is required 
 * since it is a dependency of the GasStation class, which must be Parcelable.
 * 
 * @author SmartPump Team
 * @version 1.0
 * @see GasStation
 */

public class StationAddress implements Parcelable
{
    private String street;
    private String city;
    private String state;
    private String zip;
    
    /**
     * Constructs a new StationAddress from street, city, state, and zip code
     * 
     * @param street the street name
     * @param city the city name
     * @param state the state name
     * @param zip the zip code
     */
    public StationAddress(String street, String city, String state, String zip)
    {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
    
    /**
     * Constructs a new StationAddress from a Parcel object
     * 
     * @param in the Parcel object to extract address data from
     */
    public StationAddress(Parcel in)
    {
        street = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
    }
    
    /**
     * Returns the street name of this address
     * @return street
     */
    public String getStreet()
    {
        return this.street;
    }

    /**
     * Returns the city name of this address
     * @return city
     */
    public String getCity()
    {
        return this.city;
    }
    
    /**
     * Returns the state name of this address
     * @return state
     */
    public String getState()
    {
        return this.state;
    }
    
    /**
     * Returns the zip code of this address
     * @return zip
     */
    public String getZip()
    {
        return this.zip;
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
     * Flattens this station address data into the destination Parcel
     * 
     * @param dest the destination Parcel
     * @param flags additional flags specifying how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getStreet());
        dest.writeString(getCity());
        dest.writeString(getState());
        dest.writeString(getZip());
    }
    
    /**
     * Implemented interface for generating StationAddress instances from a Parcel
     */
    public static final Parcelable.Creator<StationAddress> CREATOR = new Parcelable.Creator<StationAddress>() 
    {
        public StationAddress createFromParcel(Parcel in) 
        {
            return new StationAddress(in);
        }

        @Override
        public StationAddress[] newArray(int size) 
        {
            return new StationAddress[size];
        }
    };
}