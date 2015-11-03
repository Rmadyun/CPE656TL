#October 28, 2015
#CPE 656 Train Trax Project
#Corey Sanders
#Script is responsible for estimating the position of an object based on collected IMU measurements.

#gyroscopeFile="/home/death/nexus7Measurements/StaticGyroscopeMeasurements.csv";
#accelerometerFile="/home/death/nexus7Measurements/StaticAccelerometerMeasurements.csv";
#positionFile="/home/death/nexus7Measurements/StaticAccelerometerMeasurements.csv";

gyroscopeFile="/home/death/nexus7Measurements/XAxisRotationNexus7GyroscopeMeasurements.csv";
accelerometerFile="/home/death/nexus7Measurements/XAxisRotationNexus7AccelerometerMeasurements.csv";
positionFile="/home/death/nexus7Measurements/XAxisRotationNexus7PositionCalculation.csv";


#function retval = convertToInertialFrameFromBodyFrame(bodyFrameInEulerAngles, initialInertialFrameInEulerAngles) 
# p = bodyFrameInEulerAngles(1, 1);
# q = bodyFrameInEulerAngles(2, 1);
# r = bodyFrameInEulerAngles(3, 1);
# fee = initialInertialFrameInEulerAngles(1,1);
# theta = initialInertialFrameInEulerAngles(2, 1);
# aroundX = p + q*sin(fee)*tan(theta) + r*cos(fee)*tan(theta);
# aroundY = q*cos(fee) - r*sin(fee);
# aroundZ = q*sin(fee)/cos(theta) + r*cos(fee)/cos(theta);

# retval = [ aroundX aroundY aroundZ];
#endfunction

   function retval = convertToInertialFrameFromBodyFrame (bodyFrameEulerAngleVector, initialBodyOrientationOnInertialFrameEulerAngleVector)
     tempVar = ones(3,1);
     #Vector describing the rotation of the device along its axises
     p = bodyFrameEulerAngleVector(1,1);
     q = bodyFrameEulerAngleVector(2,1);
     r = bodyFrameEulerAngleVector(3,1);
     
     #Orientation Along X
     fee = initialBodyOrientationOnInertialFrameEulerAngleVector(1, 1);
     #Orientation Along Y
     theta = initialBodyOrientationOnInertialFrameEulerAngleVector(2, 1);
     #Frame Conversion
     inertialFrameAlongX = p + q*sin(fee)*tan(theta) + r*cos(fee)*tan(theta);
     inertialFrameAlongY = q*cos(fee) - r*sin(fee);
     inertialFrameAlongZ = q*sin(fee)/cos(theta) + r*cos(fee)/cos(theta);
     tempVar(1, 1) = inertialFrameAlongX;
     tempVar(2, 1) = inertialFrameAlongY;
     tempVar(3, 1) = inertialFrameAlongZ;
     retval = tempVar;
   endfunction

   #Creates a rotation matrix from 
   #Euler Angles
   #Assumes roll, pitch, and yaw are in radians
function retval = createRotationMatrix (roll, pitch, yaw)
     tempVar = ones(3,3);
     cx = cos(roll);
     cy = cos(pitch);
     cz = cos(yaw);
     sx = sin(roll);
     sy = sin(pitch);
     sz = sin(yaw);
     tempVar(1,1) = cz*cy;
     tempVar(1,2) = cz*sx*sy - cx*sz;
     tempVar(1,3) = sx*sz + cx*cz*sy;
     tempVar(2,1) = cy*sz;
     tempVar(2,2) = cx*cz + sx*sz*sy;
     tempVar(2,3) = cx*sz*sy - cz*sx;
     tempVar(3,1) = -1*sy;
     tempVar(3,2) = cy*sx;
     tempVar(3,3) = cx*cy;
     retval = tempVar;
 endfunction


function retval = filter (v,tolerance)
  tempVar = ones(rows(v), columns(v));

  for(j= 1:rows(v))
    for i = 1:columns(v)
      if (abs(v(j,i)) > tolerance)
        tempVar(j, i) = v(j, i);
      else
        tempVar(j, i) = 0;
      endif
    endfor
  endfor
  retval = tempVar;
endfunction

function retval = speed (acceleration, time, initialSpeed)
  tempVar = ones(rows(acceleration), 1);

  currentSpeed = initialSpeed
  for(i= 1:rows(acceleration))
    tempVar(i, 1) = acceleration(i)*time(i) + currentSpeed;
    currentSpeed = tempVar(i,1);
  endfor

  retval = tempVar;

endfunction

function retval = distance (speed, time, initialPosition)
  tempVar = ones(rows(speed), 1);

  currentPosition = initialPosition
  for(i= 1:rows(speed))
    tempVar(i, 1) = speed(i)*time(i) + currentPosition;
    currentPosition = tempVar(i,1);
  endfor

  retval = tempVar;

endfunction

function retval = orientation (angularVelocity, time, initialOrientation)
  tempVar = ones(rows(angularVelocity), 1);

  currentOrientation = initialOrientation
  for(i= 1:rows(angularVelocity))
    tempVar(i, 1) = angularVelocity(i)*time(i) + currentOrientation;
    currentOrientation = tempVar(i,1);
  endfor

  retval = tempVar;

endfunction

#Calculates measurements as observed from inertialframe
#Parameter 1 (bodyFrameMeasurementMatrix)
#Matrix with the following columns:
#1: Orientation Along Body X
#2: Orientation Along Body Y
#3: Orientation Along Body Z
#4: Acceleration Along Body X
#5: Acceleration Along Body Y
#6: Acceleration Along Body Z
#7: Absolute Time
#8: Relative Time
#Parameter 2 (initialIntertialFrameOrientation)
#Column with the following rows:
#1: Orientation Along Intertial X
#2: Orientation Along Intertial Y
#3: Orientation Along Intertial Z
function retval = calculateInertialFrameMatrix (bodyFrameMeasurementMatrix, initialInertialFrameOrientation)

   tempVar = ones(rows(bodyFrameMeasurementMatrix), columns(bodyFrameMeasurementMatrix));

  for(i= 1:rows(bodyFrameMeasurementMatrix))
    
    tempVar(i, 1:3) = convertToInertialFrameFromBodyFrame(transpose(bodyFrameMeasurementMatrix(i, 1:3)), initialInertialFrameOrientation);

    current_roll = tempVar(i, 1);
    current_pitch = tempVar(i, 2);
    current_yaw = tempVar(i, 3);
    rotationMatrix = createRotationMatrix(current_roll, current_pitch, current_yaw);
    #Calculate acceleration vector relative to inertial frame
    rotated_a = transpose(rotationMatrix * transpose(bodyFrameMeasurementMatrix(i, 4:6)));
    tempVar(i, 4:6) = rotated_a;
    #Copy time measurements
    tempVar(i, 7:8) = bodyFrameMeasurementMatrix(i, 7:8);
  endfor

  retval = tempVar;

endfunction

g=csvread(gyroscopeFile);
a=csvread(accelerometerFile);

gt = (g(:,4)/1000000000);
at = (a(:,4)/1000000000);

#a_round = [(round(a(:, 1:3) .* 10)./10) at];
#g_round = [(round(g(:, 1:3) .* 100)./100) gt];

a_time = a(:,4);
g_time = g(:,4);

im1 = [interp1(g(:,4), g(:,1), a_time) interp1(g(:,4), g(:,2), a_time) interp1(g(:,4), g(:,3), a_time) a];

im2 = [g(:, 1:3) interp1(a(:,4), a(:,1), g_time) interp1(a(:,4), a(:,2), g_time) interp1(a(:,4), a(:,3), g_time) g_time];

fm = [im1;im2];

#fm_round = [(round(fm(:, 1:3) .* 100)./100) (round(fm(:, 4:6) .* 10)./10) fm(:, 7)];
fm_round = [(floor(fm(:, 1:3) .* 100)./100) (floor(fm(:, 4:6) .* 10)./10) fm(:, 7)/1000000000];


#Sort 
#t = fm_round(:, 7);
#diff(t)

fm_sort = sortrows(fm_round, 7);
fm_sort2 = [fm_sort [ 0 ; diff(fm_sort(:, 7))] ];

#Assume zero for items that do not have a valid number
fm_sort2(isnan(fm_sort2)) = 0;

#Filter
fm_sort2(:, 1:3) = filter(fm_sort2(:,1:3), 0.002);
fm_sort2(:, 4:6) = filter(fm_sort2(:,4:6), 0.2);

#Calculate Orientation
initialOrientationOnBodyFrame = [ 0; 0; 0];
initialOrientationOnInertialFrame = [ 0; 0; 0];

orientation_x = orientation(fm_sort2(:,1), fm_sort2(:,8), initialOrientationOnBodyFrame(1, 1));
orientation_y = orientation(fm_sort2(:,2), fm_sort2(:,8), initialOrientationOnBodyFrame(2, 1));
orientation_z = orientation(fm_sort2(:,3), fm_sort2(:,8), initialOrientationOnBodyFrame(3, 1));

bodyFrameMeasurements = [orientation_x orientation_y orientation_z fm_sort2(:, 4) fm_sort2(:, 5) fm_sort2(:, 6) fm_sort2(:, 7) fm_sort2(:, 8)];
inertialFrameMeasurements = calculateInertialFrameMatrix(bodyFrameMeasurements, initialOrientationOnInertialFrame);


 #Estimate the velocity and position of the device
   speed_x = speed(inertialFrameMeasurements(:,4), inertialFrameMeasurements(:,8), 0);
   speed_y = speed(inertialFrameMeasurements(:,5), inertialFrameMeasurements(:,8), 0);
   speed_z = speed(inertialFrameMeasurements(:,6), inertialFrameMeasurements(:,8), 0);
   pos_x = distance(speed_x, inertialFrameMeasurements(:,8), 0);
   pos_y = distance(speed_y, inertialFrameMeasurements(:,8), 0);
  pos_z = distance(speed_z, inertialFrameMeasurements(:,8), 0);


calculations = [ speed_x speed_y speed_z pos_x pos_y pos_z fm_sort2(:, 7) ];

csvwrite(positionFile, [fm_sort2(:, 1:6) speed_x speed_y speed_z pos_x pos_y pos_z fm_sort2(:, 7:8)]);
