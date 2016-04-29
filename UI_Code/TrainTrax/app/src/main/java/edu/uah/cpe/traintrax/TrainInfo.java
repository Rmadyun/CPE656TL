package edu.uah.cpe.traintrax;


/**
 * Class that stores information about a train that is being monitored
 */
public class TrainInfo {

    private String trainID; // unique ID mapped to the train
    private Float xposition; // x position of train
    private Float yposition; // y position of train
    private Float xvelocity; // x velocity of train
    private Float yvelocity; // y velocity of train

    /**
     * Constructor
     * @param trainID Unique ID mapped to the train
     */
    public TrainInfo(String trainID){
        this(trainID, Float.valueOf(0), Float.valueOf(0));
    }

    /**
     * Constructor
     * @param trainID Unique ID mapped to the train
     * @param xposition X position of the train
     * @param yposition Y position of the train
     * @param xvelocity X velocity of the train
     * @param yvelocity Y velocity of the train
     */
    public TrainInfo(String trainID, Float xposition, Float yposition){

        this.trainID = trainID;
        this.xposition = xposition;
        this.yposition = yposition;
        this.xvelocity = 0.0f;
        this.yvelocity = 0.0f;
    }

    /**
     * Retrieves the unique ID mapped to the train
     * @return the Unique ID mapped to the train
     */
    public String getTrainID() {
        return trainID;
    }

        /**
     * Retrieves the x position of the train
     * @return the x position of the train
     */
    public Float getXposition() {
        return xposition;
    }

    /**
     * Assigns the x position of the train
     * @param xposition the x position of the train
     */
    public void setXposition(Float xposition) {
        this.xposition = xposition;
    }

    /**
     * Retrieves the y position of the train
     * @return the y position of the train
     */
    public Float getYposition() {
        return yposition;
    }

    /**
     * Assigns the y position of the train
     * @param yposition the y position of the train
     */
    public void setYposition(Float yposition) {
        this.yposition = yposition;
    }

    /**
     * Retrieves the x velocity of the train
     * @return the x velocity of the train
     */
    public Float getXvelocity() {
        return xvelocity;
    }

    /**
     * Assigns the x velocity of the train
     * @param xvelocity the x velocity of the train
     */
    public void setXvelocity(Float xposition) {
        this.xvelocity = xvelocity;
    }

    /**
     * Retrieves the y velocity of the train
     * @return the y velocity of the train
     */
    public Float getYvelocity() {
        return yvelocity;
    }

    /**
     * Assigns the y velocity of the train
     * @param yvelocity the y velocity of the train
     */
    public void setYvelocity(Float yposition) {
        this.yvelocity = yvelocity;
    }
}
