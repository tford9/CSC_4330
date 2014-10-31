package com.project.searching;

import java.util.Comparator;

public class DistanceComparator implements
		Comparator<StationSearchResult> {

	@Override
	public int compare(StationSearchResult lhs, StationSearchResult rhs) {
		return lhs.getStation().getDistance().compareTo(rhs.getStation().getDistance());
	}

}
