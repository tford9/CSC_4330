package com.project.searching;

import java.util.Comparator;

public class PriceComparator implements
Comparator<StationSearchResult> {

@Override
public int compare(StationSearchResult lhs, StationSearchResult rhs) {

if (lhs.getStation().getSelectedFuelPrice().getPrice() < rhs.getStation().getSelectedFuelPrice().getPrice())
	return -1;
else if(lhs.getStation().getSelectedFuelPrice().getPrice() > rhs.getStation().getSelectedFuelPrice().getPrice())
	return 1;
else return 0; 
}

}
