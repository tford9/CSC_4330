package com.project.searching;

import java.util.Comparator;

/**
 * Compares the distance from the users location to two stations, and return the
 * closest station.
 * 
 * @Override Comparator
 * 
 * @version 1.0
 * @author SmartPump Team
 */

public class DistanceComparator implements Comparator<StationSearchResult> {

	@Override
	/**
	 * @return lhs...compareTo(rhs...getDistance) depending on comparison results
	 *         either lhs or rhs.*/
	public int compare(StationSearchResult lhs, StationSearchResult rhs) {
		return lhs.getStation().getDistance()
				.compareTo(rhs.getStation().getDistance());
	}

}
