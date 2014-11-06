package com.project.searching;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stores the value and the last update date of a fuel price.
 * SmartPump version 1.0 does not make use of the date last updated,
 * but a future version may use this data as an indication of
 * the accuracy of the price value. This class implements the Parcelable
 * interface, which is required since it is a dependency of the GasStation
 * class, which must be Parcelable.
 * 
 * @author SmartPump Team
 * @version 1.0
 * @see GasStation
 */
public class FuelPrice implements Parcelable{

    private double price;
    private String lastUpdated;
    
    /**
     * Constructs a new FuelPrice with default values.
     */
    public FuelPrice()
    {
        this.price = 0.0;
        this.lastUpdated = "";
    }
    
    /**
     * Constructs a new FuelPrice with a price value and date last updated.
     * 
     * @param price the price value as a double
     * @param lastUpdated the date last updated as a string
     */
    public FuelPrice(double price, String lastUpdated)
    {
        this.price = price;
        this.lastUpdated = lastUpdated;
    }
    
    /**
     * Constructs a new FuelPrice from a Parcel object.
     * 
     * @param in the Parcel object to extract fuel price data from
     */
    public FuelPrice(Parcel in) {
        this.price = in.readDouble();
        this.lastUpdated = in.readString();
    }
    
    /**
     * Returns the value of this FuelPrice
     * 
     * @return price value
     */
    public double getPrice() {
        return this.price;
    }
    
    /**
     * Returns the date last updated for this FuelPrice
     * 
     * @return the date last updated
     */
    public String getLastUpdated() {
        return this.lastUpdated;
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
     * Flattens this fuel price's data into the destination Parcel
     * 
     * @param dest the destination Parcel
     * @param flags additional flags specifying how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(getPrice());
        dest.writeString(getLastUpdated());
    }
    
    /**
     * Implemented interface for generating FuelPrice instances from a Parcel
     */
    public static final Parcelable.Creator<FuelPrice> CREATOR = new Parcelable.Creator<FuelPrice>() 
    {
        public FuelPrice createFromParcel(Parcel in) 
        {
            return new FuelPrice(in);
        }

        @Override
        public FuelPrice[] newArray(int size) 
        {
            return new FuelPrice[size];
        }
    };
    
}
