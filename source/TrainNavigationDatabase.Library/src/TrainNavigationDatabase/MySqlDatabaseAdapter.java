package TrainNavigationDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for broker transactions with a given
 * MySQL Database Server.
 * @author Corey Sanders
 *
 */
public class MySqlDatabaseAdapter implements GenericDatabaseInterface {
    private Connection currentConnection = null;
    
    private Connection createNewConnection(){
    	Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://localhost:3306/TrainTrax";
		String user = "root";
		String password = "root";

		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(MySqlDatabaseAdapter.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(MySqlDatabaseAdapter.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

        return con;    	
    }

    private ResultSet sendQuery(Connection connection, String queryString){
		Statement st = null;
		ResultSet rs = null;

		if(connection != null){
			try {
				st = connection.createStatement();
				rs = st.executeQuery(queryString);

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(MySqlDatabaseAdapter.class
						.getName());
				lgr.log(Level.SEVERE, ex.getMessage(), ex);

			} finally {
				try {
					/*if (rs != null) {
						rs.close();
					} */
					if (st != null) {
						st.close();
					}
				} catch (SQLException ex) {
					Logger lgr = Logger.getLogger(MySqlDatabaseAdapter.class
							.getName());
					lgr.log(Level.WARNING, ex.getMessage(), ex);
				}
			}
		}
    	
    	return rs;
    }
    
    private void closeConnection(Connection connection){
    	try{
    		connection.close();
    	}
    	catch(Exception exception)
    	{
    	}
    }
    
    private DatabaseEntry createDatabaseEntry(ResultSet resultSet){
    	DatabaseEntry databaseEntry = null;
    	
    	
    	try {
			if(!resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
				ResultSetMetaData resultSetMetadata = resultSet.getMetaData();
				
				List<KeyValuePair> kvps = new ArrayList<KeyValuePair>();
				
				int columnCount = resultSetMetadata.getColumnCount();
				
				for(int i = 1; i <= columnCount; i++){
					String value = resultSet.getString(i);
					String key = resultSetMetadata.getColumnName(i);
					
					KeyValuePair kvp = new KeyValuePair(key, value);
					
					kvps.add(kvp);
				}
				
				databaseEntry = new DatabaseEntry(kvps);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return databaseEntry;
    }
  
	public void connect(){
		Statement st = null;
		ResultSet rs = null;

		currentConnection = createNewConnection();
		
		if(currentConnection != null){
			
			rs = sendQuery(currentConnection, "SELECT VERSION()");
			
			if(rs != null){
				try{
					if (rs.next()) {
						System.out.println(rs.getString(1));
					}
				}
				catch(Exception exception){
					closeConnection(currentConnection);
					currentConnection = null;
				}
				finally{
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else{
				closeConnection(currentConnection);
				currentConnection = null;
			}
		}
	}

	@Override
	public List<DatabaseEntry> sendQuery(String queryString) {

		List<DatabaseEntry> results = new ArrayList<DatabaseEntry>();
		
		if(currentConnection == null){
			currentConnection = createNewConnection();
		}

		Statement st = null;
		try {
			st = currentConnection.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = null;
		
		if(st != null){
			try {
				rs = st.executeQuery(queryString);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(rs != null){
			try{
				while(rs.next()){
					DatabaseEntry trackPoint = createDatabaseEntry(rs);
					
					if(trackPoint != null){
						results.add(trackPoint);
					}
				}
			}
			catch(Exception exception){
				closeConnection(currentConnection);
				currentConnection = null;
			}
			finally{
				try {
					st.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return results;
	}
	
	private int runUpdate(String queryString) {

		int val = -1;
		List<DatabaseEntry> results = new ArrayList<DatabaseEntry>();
		
		if(currentConnection == null){
			currentConnection = createNewConnection();
		}

		PreparedStatement st = null;
		try {
			st = currentConnection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = null;
		
		if(st != null){
			try {
				st.executeUpdate();
				
			      try  {
			    	  ResultSet generatedKeys = st.getGeneratedKeys();
			            if (generatedKeys.next()) {
			                val = generatedKeys.getInt(1);
			            }
			            else {
			                throw new SQLException("Creating user failed, no ID obtained.");
			            }
			        }
			      catch(Exception e){
			    	  
			      }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally{
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch blockentry.getPointId()
					e.printStackTrace();
				}
			}
		}	
		
		return val;
	}

	@Override
	public int sendAdd(String tableName, DatabaseEntry databaseEntry) {
		int val = -1;
		StringBuilder queryBuilder = new StringBuilder();
		String keyString = "";
		String valueString = "";

		queryBuilder.append("INSERT INTO ");
		queryBuilder.append(tableName);
		queryBuilder.append(" (");
		int numColumns = databaseEntry.getColumns().size();

		for (int i = 0; i < numColumns; i++) {
			KeyValuePair kvp = databaseEntry.getColumns().get(i);

			keyString += kvp.getKey();
			valueString += kvp.getValue();

			if (i < (numColumns - 1)) {
				keyString += ", ";
				valueString += ", ";
			}
		}
		queryBuilder.append(keyString);
		queryBuilder.append(") \n VALUES (");
		queryBuilder.append(valueString);
		queryBuilder.append(")");

		String queryString = queryBuilder.toString();

		val = runUpdate(queryString);
				
		return val;
	}

	@Override
	public void sendDelete(String queryString) {
		runUpdate(queryString);
	}
}
