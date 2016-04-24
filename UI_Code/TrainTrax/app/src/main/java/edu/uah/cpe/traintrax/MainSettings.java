package edu.uah.cpe.traintrax;

/**
 * Class is used to retrieve and set fields for the Settings Menu
 */
final public class MainSettings {

    /**
     * Default Port Number assigned for connecting to the Train Navigation Database Rest Service
     */
    public static final int DefaultDatabasePortNumber = 8182;

    /**
     * Default Port Number assigned for connecting to the Train Navigation Service Rest Service
     */
    public static final int DefaultNavServicePortNumber = 8183;

    /**
     * Default HostName assigned for the machine that hosts both the Train Navigation Service Rest Service and
     * the Train Navigation Database Rest Service
     */
    public static final String DefaultHostname = "localhost";

    /* Private variable for the train database rest service port number */
    private int databasePortNumber;

    /* Private variable for the train navigation service rest service port number */
    private int navigationServicePortNumber;

    /* private variable for IP address */
    private String hostName;


    /* private variable to keep track of which dialog is being edited */
    private Boolean portFlag;

    /* Gets the port number for the train database rest service */
    public int getDatabasePortNumber() {
        return databasePortNumber;
    }

    /* Gets the IP address */
    public String getHostName() {
        return hostName;
    }

    /* Sets the search interval value */
    public void setDatabasePortNumber(int databasePortNumber) {
        this.databasePortNumber = databasePortNumber;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getNavigationServicePortNumber() {
        return navigationServicePortNumber;
    }

    public void setNavigationServicePortNumber(int navigationServicePortNumber) {
        this.navigationServicePortNumber = navigationServicePortNumber;
    }

    /**
     * Default Constructor
     */
    public MainSettings() {
        this.databasePortNumber = DefaultDatabasePortNumber;
        this.navigationServicePortNumber = DefaultNavServicePortNumber;
        this.hostName = DefaultHostname;
        this.portFlag = false;
    }
}
