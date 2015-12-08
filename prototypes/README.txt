The 'MotionDetectionUnit' folder describes the results of testing conducted to evaluate all of the sensors planned to be used on the Motion Detection Unit.

The 'Gui' folder describes prototyping efforts to demonstrate what the UI for Train Trax will look like and operate in practice.

The 'Database' folder demonstrates how data is planned to be organized in Train Trax's database. It includes exported information from an actual prototype database that was created and included actual measurements from the test bed. Plus, it includes scripts used to create the database from scratch using SQL.

The 'Matlab' folder demonstrates research conducted to test the train postion estimation algorithm using semi-interpreted measurements (decoded into units but still from the device reference frame) from a Nexus 7 accelerometer and gyroscope. The position estimation algorithm is shown in the 'positionread.m'.
It also has a 'positiontriangulaton.m' file that shows how the positions reported in the sample track database were calculated. Lastly it includes a subfolder of the last collected measurements for the track.

The 'TestNavigation' folder demonstrates work done to prepare for translating the Matlab scripts into a Java implementation. So far work has been done to verify the conversion of gyroscope measurements from the device reference frame to the inertial reference frame. It is still a work in progress and may not yet show anything meaningful other than active development is already underway.



