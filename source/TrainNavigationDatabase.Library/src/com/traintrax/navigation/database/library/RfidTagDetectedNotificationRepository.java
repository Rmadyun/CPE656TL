package com.traintrax.navigation.database.library;

import java.util.List;

/**
 * Class responsible for storing all of the known
 * RFID Tag Detected events reported to the system.
 * @author Corey Sanders
 *
 */
public class RfidTagDetectedNotificationRepository implements FilteredSearchRepositoryInterface<RfidTagDetectedNotification, RfidTagDetectedNotificationSearchCriteria>  {

	@Override
	public RepositoryEntry<RfidTagDetectedNotification> find(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepositoryEntry<RfidTagDetectedNotification> add(
			RfidTagDetectedNotification entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String id, RfidTagDetectedNotification entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<RepositoryEntry<RfidTagDetectedNotification>> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepositoryEntry<RfidTagDetectedNotification>> find(
			RfidTagDetectedNotificationSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
