package com.project.searching;

import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * StationSearchResult lists the Results of the Gas Stations located within a 5
 * mile radius of the location. This class implements the Parcelable and
 * Comparable interfaces, which are required since they are dependencies of the
 * GasStation class.
 * 
 * @author SmartPump Team
 * @version 1.0
 * 
 * @Override {@link Parcelable}
 * @Override {@link Comparable}
 * @see GasStation
 */

public class StationSearchResult implements Parcelable,
		Comparable<StationSearchResult> {
	private GasStation station;
	private double adjustedCost;

	/**
	 * Constructs a StationSearchResult from the GasStation and the adjustedCost
	 * 
	 * @param station
	 *            The Gas Station
	 * @param adjustedCost
	 *            The adjusted cost
	 */
	public StationSearchResult(GasStation station, double adjustedCost) {
		this.station = station;
		this.adjustedCost = adjustedCost;
	}

	/**
	 * Constructs a StationSearchResult from the Parcel object
	 * 
	 * @param in
	 *            the in Parcel
	 */
	public StationSearchResult(Parcel in) {
		adjustedCost = in.readDouble();
		station = in.readParcelable(GasStation.class.getClassLoader());
	}

	/**
	 * Returns the gas station
	 * 
	 * @return station the gas station
	 */
	public GasStation getStation() {
		return this.station;
	}

	/**
	 * Returns the adjusted cost for the gas station selected
	 * 
	 * @return adjustedCost the adjusted cost
	 */
	public double getAdjustedCost() {
		return this.adjustedCost;
	}

	/**
	 * Returns bit masks which can be used to represent significant objects for
	 * this Parcel. Required implementation, but functionality is not currently
	 * necessary.
	 * 
	 * @return bit mask for the Parcel
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Flattens the adjusted cost's data into the Parcel
	 * 
	 * @param dest
	 *            the destination Parcel
	 * @param flags
	 *            additional flags specifying how the object should be written
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(getAdjustedCost());
		dest.writeParcelable(getStation(), flags);
	}

	/**
	 * Implemented interface for generating StationSearchResult instances from a
	 * Parcel
	 */
	public static final Parcelable.Creator<StationSearchResult> CREATOR = new Parcelable.Creator<StationSearchResult>() {
		public StationSearchResult createFromParcel(Parcel in) {
			return new StationSearchResult(in);
		}

		@Override
		public StationSearchResult[] newArray(int size) {
			return new StationSearchResult[size];
		}
	};

	/**
	 * Compares this StationSearchResult with the argument
	 * based on the adjusted cost property. Used for
	 * sorting results by adjuste cost
	 * 
	 * @param that the StationSearchResult to compare this to
	 * @return integer value representing comparison. EQUAL = 0, BEFORE = -1, AFTER = 1
	 */
	@Override
	public int compareTo(StationSearchResult that)
	{
		final int EQUAL = 0;
		final int BEFORE = -1;
		final int AFTER = 1;

		if (this.adjustedCost < that.adjustedCost)
			return BEFORE;
		else if (this.adjustedCost > that.adjustedCost)
			return AFTER;
		else
			return EQUAL;
	}

	public static Comparator<StationSearchResult> StationSearchResultComparator = new Comparator<StationSearchResult>() {
		@Override
		public int compare(StationSearchResult station1,
				StationSearchResult station2) {
			String stationName1 = station1.station.getStationName()
					.toUpperCase();
			String stationName2 = station2.station.getStationName()
					.toUpperCase();
			return stationName1.compareTo(stationName2);
		}

	};

}
