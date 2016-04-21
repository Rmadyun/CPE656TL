import java.util.Calendar;
import com.traintrax.navigation.database.library.*;

public class DatabaseTestProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MySqlDatabaseAdapter mySqlDatabaseAdapter = new MySqlDatabaseAdapter();

		mySqlDatabaseAdapter.connect();
		
		RfidTagDetectedNotificationRepository rfidTagRepo = new RfidTagDetectedNotificationRepository(mySqlDatabaseAdapter);
		
		rfidTagRepo.add(new RfidTagDetectedNotification("00:00:00:00:01", Calendar.getInstance()));
		
		rfidTagRepo.find("1");
	}
}
