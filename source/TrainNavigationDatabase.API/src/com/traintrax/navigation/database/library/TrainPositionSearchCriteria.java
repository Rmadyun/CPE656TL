package com.traintrax.navigation.database.library;

/**
 * Class describes how to filter searches
 * for TrainPosition objects within a collection or
 * repository.
 * @author Corey Sanders
 *
 */
public class TrainPositionSearchCriteria {
	
	private String xValue;
	private String yValue;
	private String zValue;
	private String trainId;

	/**
	 * @return the xValue
	 */
	public String getXValue() {
		return xValue;
	}
	/**
	 * @param xValue the xValue to set
	 */
	public void setXValue(String xValue) {
		this.xValue = xValue;
	}
	/**
	 * @return the yValue
	 */
	public String getYValue() {
		return yValue;
	}
	/**
	 * @param yValue the yValue to set
	 */
	public void setYValue(String yValue) {
		this.yValue = yValue;
	}
	/**
	 * @return the zValue
	 */
	public String getZValue() {
		return zValue;
	}
	/**
	 * @param zValue the zValue to set
	 */
	public void setZValue(String zValue) {
		this.zValue = zValue;
	}
	
	/**
	 * @return the trainId
	 */
	public String getTrainId() {
		return trainId;
	}
	/**
	 * @param trainId the trainId to set
	 */
	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}

}
