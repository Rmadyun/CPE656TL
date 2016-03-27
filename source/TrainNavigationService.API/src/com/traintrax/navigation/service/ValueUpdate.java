package com.traintrax.navigation.service;

import java.util.Calendar;

/**
 * Class describes a recorded snapshot of a value
 * that is being monitored over time.
 * @author Corey Sanders
 *
 * @param <TValue> Type of value being monitored.
 */
public class ValueUpdate<TValue> implements Comparable<ValueUpdate<TValue>> {
	
	private TValue value;
	private Calendar timeObserved;
	
	/**
	 * Constructor
	 * @param value the value observed with this update
	 * @param timeObserved the time and date that the value reported as observed
	 */
	public ValueUpdate(TValue value, Calendar timeObserved) {
		super();
		this.value = value;
		this.timeObserved = timeObserved;
	}
	
	/**
	 * Retrieves the value observed with this update.
	 * @return the value observed with this update.
	 */
	public TValue getValue() {
		return value;
	}
	/**
	 * Retrieves the time and date that the value reported as observed
	 * @return the time and date that the value reported as observed
	 */
	public Calendar getTimeObserved() {
		return timeObserved;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timeObserved == null) ? 0 : timeObserved.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueUpdate<?> other = (ValueUpdate<?>) obj;
		if (timeObserved == null) {
			if (other.timeObserved != null)
				return false;
		} else if (!timeObserved.equals(other.timeObserved))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(ValueUpdate<TValue> otherMeasurement) {
		
	    return Long.compare(this.timeObserved.getTimeInMillis(), otherMeasurement.timeObserved.getTimeInMillis());
	}
	
}
