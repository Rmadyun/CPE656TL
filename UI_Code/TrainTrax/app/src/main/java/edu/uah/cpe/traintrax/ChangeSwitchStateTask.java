package edu.uah.cpe.traintrax;

import android.os.AsyncTask;

import com.traintrax.navigation.database.library.AdjacentPoint;
import com.traintrax.navigation.database.library.RepositoryEntry;
import com.traintrax.navigation.database.library.TrackBlock;
import com.traintrax.navigation.database.library.TrackPoint;
import com.traintrax.navigation.database.library.TrackSwitch;
import com.traintrax.navigation.database.rest.client.RemoteAdjacentPointRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackBlockRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackPointRepository;
import com.traintrax.navigation.database.rest.client.RemoteTrackSwitchRepository;
import com.traintrax.navigation.service.TrainNavigationServiceInterface;
import com.traintrax.navigation.trackswitch.SwitchState;

import java.util.List;

/**
 * Class is responsible for sending an instruction to the Train Navigation Service to
 * change a switch
 */
public class ChangeSwitchStateTask extends AsyncTask<String,Void,ChangeSwitchStateTaskResult> {
    @Override
    protected ChangeSwitchStateTaskResult doInBackground(String... params) {

        String switchNumber = params[0];
        String switchStateString = params[1];
        String failureMessage = "";
        SwitchState switchState = SwitchState.Pass;


        try {
            TrainNavigationServiceInterface trainNavigationService = SharedObjectSingleton.getInstance().getTrainNavigationServiceInterface();

            if(trainNavigationService != null) {

                switchState = SwitchState.valueOf(switchStateString);

                trainNavigationService.SetSwitchState(switchNumber, switchState);
            }
            else{
                failureMessage = "Unable to control switch. TrainNavigationService reference is null. Check to see if it was initialized properly.";
            }
        }
        catch(Exception exception){
            failureMessage = "Unable to control switch. Exception: "+exception.getMessage();
        }

        return new ChangeSwitchStateTaskResult(failureMessage, switchState, switchNumber);
    }
}
