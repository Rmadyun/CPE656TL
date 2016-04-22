package edu.uah.cpe.traintrax;



/* Class is used to retrieve and set fields for the Settings Menu */

final public class MainSettings {
    /* Private variable for port number */
    private int portNum;
    /* private variable for IP address */
    private String hostName;
    /* private variable to keep track of which dialog is being edited */
    private Boolean portFlag;

    /* Gets the port number */
    public int getPortNum() {
        return portNum;
    }

    /* Gets the IP address */
    public String getHostName() {
        return hostName;
    }

    /* Sets the search interval value */
    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /* Gets the portFlag  */
    public Boolean getportFlag() {
        return portFlag;
    }

    public void setportFlag(Boolean portFlag) {
        this.portFlag = portFlag;
    }


    /**
     * Constructor
     *
     * @param portNum
     * @param hostName &param portFlag
     */

        /* Default Constructor */
    public MainSettings() {
        //set to a default local ip address and port num 8182
        this.portNum = 8182;
        this.hostName = "127.0.0.1";
        this.portFlag = false;
    }

}
