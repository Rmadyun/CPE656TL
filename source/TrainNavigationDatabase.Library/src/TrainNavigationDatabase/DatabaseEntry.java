package TrainNavigationDatabase;

import java.util.List;

public class DatabaseEntry {
	
	private List<KeyValuePair> columns;

	public DatabaseEntry(List<KeyValuePair> columns){
		this.columns = columns;
	}

	public List<KeyValuePair> getColumns(){
		return columns;
	}
	

}
