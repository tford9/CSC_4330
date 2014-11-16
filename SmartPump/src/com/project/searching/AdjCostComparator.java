package com.project.searching;

import java.util.Comparator;

/**
 * Compares the price between two list items and returns the lowest of the two
 * StationSearchResults list items.
 * 
 * @Override Comparator
 * @version 1.0
 * @author SmartPump Team
 */

public class AdjCostComparator implements Comparator<StationSearchResult> {

	/** @return {1, 0 , -1} depending on comparison results. */
	@Override
	public int compare(StationSearchResult lhs, StationSearchResult rhs) {

		if (lhs.getAdjustedCost() < rhs.getAdjustedCost())
			return -1;
		else if (lhs.getAdjustedCost() > rhs.getAdjustedCost())
			return 1;
		else
			return 0;
	}

}
