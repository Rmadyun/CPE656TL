package com.traintrax.navigation.database.rest.service;

/**
 * Class represents the information necessary to initialize the
 * Train Navigation Database and run it as a service.
 * @author Corey Sanders
 *
 */
public class TrainNavigationDatabaseConfiguration {
	/**
	 * Default network port to use to host the service
	 */
	public static final int DefaultServicePort = 8182;
	
	/**
	 * Default user name to use to access database information
	 */
	public static final String DefaultDbUsername = "root";
	
	/**
	 * Default password to use to access database information
	 */
	public static final String DefaultDbPassword = "root";
	
	/**
	 * Default name of the database to access
	 */
	public static final String DefaultDbName = "TrainTrax";
	
	/**
	 * Default network hostname or address to use to access the database
	 */
	public static final String DefaultHost = "localhost";
	
	/**
	 * Default network port to use to access the database
	 */
	public static final int DefaultDbPort = 3306;
	
	private int hostPort;
	private String dbUsername;
	private String dbPassword;
	private String dbName;
	private String dbHost;
	private int dbPort;
	
	/**
	 * Default Constructor
	 */
	public TrainNavigationDatabaseConfiguration(){
		hostPort = DefaultServicePort;
		dbUsername = DefaultDbUsername;
		dbPassword = DefaultDbPassword;
		dbName = DefaultDbName;
		dbHost = DefaultHost;
		dbPort = DefaultDbPort;
	}
	
	
	/**
	 * Retreives the host port
	 * @return Network port to use to host the service
	 */
	public int getHostPort() {
		return hostPort;
	}
	
	/**
	 * Assigns the host port
	 * @param hostPort Network port to use to host the service
	 */
	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}
	
	/**
	 * Retrieves the database username to use
	 * @return User name to use to access database information provided by the service
	 */
	public String getDbUsername() {
		return dbUsername;
	}
	
	/**
	 * Assigns the database username to use
	 * @param dbUsername User name to use to access database information provided by the service
	 */
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	
	/**
	 * Retrieves the database password to use
	 * @return Password to use to access database information provided by the service
	 */
	public String getDbPassword() {
		return dbPassword;
	}
	
	/**
	 * Assigns the database password to use
	 * @param dbPassword Password to use to access database information provided by the service
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}


	/**
	 * Retrieves the name of the database that stores the train navigation information
	 * @return Name of the database that stores the train navigation information
	 */
	public String getDbName() {
		return dbName;
	}


	/**
	 * Assigns the name of the database used for train navigation information
	 * @param dbName Name of the database that stores the train navigation information
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}


	/**
	 * Retreives the network host name of the machine that is running the database.
	 * @return Network hostname or address of where the database is located
	 */
	public String getDbHost() {
		return dbHost;
	}


	/**
	 * Assigns the network host name of the machine that is running the database.
	 * @param dbHost Network hostname or address of where the database is located
	 */
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}


	/**
	 * Retrieves the network port used to access the database
	 * @return Network port used to access the database
	 */
	public int getDbPort() {
		return dbPort;
	}


	/**
	 * Assigns the network port used to access the database
	 * @param dbPort Network port used to access the database
	 */
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

}
