#November 3, 2015
#CPE 656 Train Trax Project
#Corey Sanders
#Script is responsible for estimating the position of an object based on collected IMU measurements.

#Method responsible for determining the position from a collection of measurements from 3 points on the train track table.
function retval = triangulatePositionFromTable (v)
    tempVar = ones(rows(v), 2);

    for(i= 1:rows(v))
        a1 = v(i, 1);
        b1 = v(i, 2);
        a2 = v(i, 3);
        b2 = v(i, 4);
        a3 = v(i, 5);
        b3 = v(i, 6);
        r1 = v(i, 7);
        r2 = v(i, 8);
        r3 = v(i, 9);

        equations = [ (-2*a1 + 2*a2) (-2*b1 + 2*b2) ; (-2*a2 + 2*a3) (-2*b2 + 2*b3) ];
        answers = [( (r1*r1 - r2*r2) - (a1*a1 - a2*a2 + b1*b1 - b2*b2)) ; ((r2*r2 - r3*r3) - (a2*a2 - a3*a3 + b2*b2 - b3*b3)) ];
        tempVar(i, :) = transpose(inverse(equations)*answers);
    endfor

    retval = tempVar;
endfunction

#Function is responsible for determining the Table coordinate system position of all of the points in the
#Trilateration CSV File.
function retval = calculateTableCoordinatesFromTrilaterationFile (trilaterationFile)
    d=csvread(trilaterationFile);
    manual_position = d(:, 1:3);
    manual_orientation_pt1 = [ d(:, 1) d(:, 4:5)];
    manual_orientation_pt2 = [ d(:, 1) d(:, 6:7)];
    position_triangulation = [ d(:, 1) d(:, 8:10) ];
    orientation_triangulation_pt1 = [ d(:, 1) d(:, 11:13) ];
    orientation_triangulation_pt2 = [ d(:, 1) d(:, 14:16) ];
    reference_pt1_position = d(:,17:18);
    reference_pt2_position = d(:, 19:20);
    reference_pt3_position = d(:, 21:22);

    position_triangulation_vector = [ reference_pt1_position reference_pt2_position reference_pt3_position position_triangulation(:, 2:4) ];

    pos = triangulatePositionFromTable(position_triangulation_vector);
    retval =   pos;
endfunction

arg_list = argv ();
if(nargin > 0)
    dataCollectionFile = arg_list {1};

    printf("Processing trilaterationFile: %s\n", dataCollectionFile);    
    #dataCollectionFile="/home/death/Documents/CPE658/Track Switch Measurements.csv";
    #dataCollectionFile = "/home/death/Documents/CPE658/Measurements/Train Trax Track Marker Measurements Part 2.csv";
    #dataCollectionFile = "/home/death/Documents/CPE658/Measurements/Train Trax Track Marker Measurements Part 3 - Inner Loop.csv";
    #dataCollectionFile = "/home/death/Documents/CPE658/Measurements/Train Trax Track Marker Measurements Part 4 Second Outer Most Loop.csv";
    #dataCollectionFile = "/home/death/Documents/CPE658/Measurements/Train Trax Track Marker Measurements Part 5 Mid Section Part 1v2.csv";
    #dataCollectionFile = "/home/death/Documents/CPE658/Measurements/Train Trax Track Marker Measurements Part 5 Mid Section Part 2.csv";

    tableCoordinates = calculateTableCoordinatesFromTrilaterationFile(dataCollectionFile);

    #Print out table coordinates in the order that they appear in the CSV
    printf("Processing complete.\nTable Coordinates: ");
    tableCoordinates
else
    printf("Data collection file was not input. Aborting processing.\n");
endif
