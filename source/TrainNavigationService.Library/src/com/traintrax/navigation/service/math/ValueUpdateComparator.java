package com.traintrax.navigation.service.math;

import java.util.Comparator;

import com.traintrax.navigation.service.ValueUpdate;

/**
 * Custom class for comparing ValueUpdate values based on time.
 * @author Corey Sanders
 *
 * @param <T> Type of value associated with the ValueUpdate.
 */
public class ValueUpdateComparator<T> implements Comparator<ValueUpdate<T>> {

	@Override
	public int compare(ValueUpdate<T> arg0, ValueUpdate<T> arg1) {
		long firstValue = arg0.getTimeObserved().getTimeInMillis();
		long secondValue = arg1.getTimeObserved().getTimeInMillis();
		
		return Long.compare(firstValue, secondValue);
	}

}
