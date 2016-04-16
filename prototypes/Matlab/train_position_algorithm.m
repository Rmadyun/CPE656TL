#March 5, 2016
#CPE 658 Train Trax Project
#Corey Sanders
#Script is responsible for estimating the position of an object based on collected IMU measurements.

#Converts a Rotation from one coordinate frame to another
#Assumes that 'bodyFrameEulerAngleVector' was measured over a small increment of time (i.e. high sample rate is being used)
#bodyFrameEulerAngleVector Rotation (Using Euler Angles) measured from the source coordinate frame.
#initialBodyOrientationOnInertialFrameEulerAngleVector Rotation (Using Euler Angles) necessary to align the source body frame with the destination body prior to the 'bodyFrameEulerAngleVector' rotation happening
#retval Rotation (Using Euler Angles) measured from the destination coordinate frame.
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

#Creates a rotation matrix from Euler Angles
#Assumes roll, pitch, and yaw are in radians
#roll Rotation in radians about a given coordinate frame's X-axis
#pitch Rotation in radians about a given coordinate frame's Y-axis
#yaw Rotation in radians about a given coordinate frame's Z-axis
#retval Directional Cosing Matrix (a.k.a Rotation Matrix) representation of the rotation expressed with Euler Angles
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

#High Pass Filter
#v Original vector to apply filter to.
#tolerance Threshold value to use to determine whether a value should be retained when filtering or replaced.
#retval Matrix where values above the tolerance level are kept and all other values are replaced with zeroes.
function retval = hp_filter (v,tolerance)
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

#Determines the speed of a given object
#acceleration Vector of acceleration measurements (in m/s^2)
#time Vector of time measurements (in seconds)
#initialSpeed Vector of the initial velocity of the target object
#retVal Vector of the final velocity of the target object.
function retval = speed (acceleration, time, initialSpeed)
  tempVar = ones(rows(acceleration), 1);

  currentSpeed = initialSpeed;
  for(i= 1:rows(acceleration))
    tempVar(i, 1) = acceleration(i)*time(i) + currentSpeed;
    currentSpeed = tempVar(i,1);
  endfor

  retval = tempVar;

endfunction

#Determines the position of a given object.
#speed Vector of velocity measurements (in m/s)
#time Vector of timestamps for each velocity measurement (in seconds).
#initialPosition Vector of the initial position of the target object.
#retval Vector of the final position of the target object.
function retval = distance (speed, time, initialPosition)
  tempVar = ones(rows(speed), 1);

  currentPosition = initialPosition;
  for(i= 1:rows(speed))
    tempVar(i, 1) = speed(i)*time(i) + currentPosition;
    currentPosition = tempVar(i,1);
  endfor

  retval = tempVar;

endfunction

#Determines the change in orientation of the object.
#angularVelocity Vector of angular velocity measurements (radians / second)
#time Vector of the timestamps for each angular velocity measurement.
#retVal Vector of the change in orientation of the object.
function retval = delta_orientation (angularVelocity, time)
  tempVar = ones(rows(angularVelocity), 1);

  for(i= 1:rows(angularVelocity))
    tempVar(i, 1) = angularVelocity(i)*time(i);
  endfor

  retval = tempVar;

endfunction

#Determines the current orientation of the object.
#angularVelocity Vector of angular velocity measurements (radians / second)
#time Vector of the timestamps for each angular velocity measurement.
#initialOrientation Vector of the initial orientation of the object
#retVal Vector of the final orientation of the object.
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
#1: Orientation Along Inertial X
#2: Orientation Along Inertial Y
#3: Orientation Along Inertial Z
#Return Value (retval)
#1: Final Orientation Along Inertial X Axis (radians)
#2: Final Orientation Along Inertial Y Axis (radians)
#3: Final Orientation Along Inertial Z Axis (radians)
#4: Final Acceleration Along Inertial X Axis (m/s^2)
#5: Final Acceleration Along Inertial Y Axis (m/s^2)
#6: Final Acceleration Along Inertial Z Axis (m/s^2)
#7  Absolute Time that Measurement was Made (seconds)
#8  Change Time Since Last Measurement (seconds)
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


function retval = calculateVector (bodyFrameMeasurementMatrix, inertialFrameOrientation)

   tempVar = ones(rows(bodyFrameMeasurementMatrix), columns(bodyFrameMeasurementMatrix));

  for(i= 1:rows(bodyFrameMeasurementMatrix))
    
    tempVar(i,1) = inertialFrameOrientation(i,1);
    tempVar(i,2) = inertialFrameOrientation(i,2);
    tempVar(i,3) = inertialFrameOrientation(i,3);
    
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


#Calculates measurements as observed from inertialframe
#Parameter 1 (bodyFrameMeasurementMatrix)
#Matrix with the following columns:
#1: Delta Orientation Along Body X
#2: Delta Orientation Along Body Y
#3: Delta Orientation Along Body Z
#4: Acceleration Along Body X
#5: Acceleration Along Body Y
#6: Acceleration Along Body Z
#7: Absolute Time
#8: Relative Time
#Parameter 2 (initialIntertialFrameOrientation)
#Column with the following rows:
#1: Orientation Along Inertial X
#2: Orientation Along Inertial Y
#3: Orientation Along Inertial Z
#Return Value (retval)
#1: Final Orientation Along Inertial X Axis (radians)
#2: Final Orientation Along Inertial Y Axis (radians)
#3: Final Orientation Along Inertial Z Axis (radians)
#4: Final Acceleration Along Inertial X Axis (m/s^2)
#5: Final Acceleration Along Inertial Y Axis (m/s^2)
#6: Final Acceleration Along Inertial Z Axis (m/s^2)
#7  Absolute Time that Measurement was Made (seconds)
#8  Change Time Since Last Measurement (seconds)
function retval = calculateInertialFrameMatrix2 (bodyFrameMeasurementMatrix, initialInertialFrameOrientation)

   tempVar = ones(rows(bodyFrameMeasurementMatrix), columns(bodyFrameMeasurementMatrix));
   
   currentInertialFrameOrientation = initialInertialFrameOrientation;

  for(i= 1:rows(bodyFrameMeasurementMatrix))
    
    deltaInertialFrameOrientation = convertToInertialFrameFromBodyFrame(transpose(bodyFrameMeasurementMatrix(i, 1:3)), currentInertialFrameOrientation);

    #Update the current inertial frame orientation
    currentInertialFrameOrientation = currentInertialFrameOrientation + deltaInertialFrameOrientation;
    
    tempVar(i, 1:3) = currentInertialFrameOrientation;

    current_roll = tempVar(i, 1);
    current_pitch = tempVar(i, 2);
    current_yaw = tempVar(i, 3);

    rotationMatrix = createRotationMatrix(current_roll, current_pitch, current_yaw);

    #Print the rotation matrix
    #rotationMatrix
    
    #Calculate acceleration vector relative to inertial frame
    rotated_a = transpose(rotationMatrix * transpose(bodyFrameMeasurementMatrix(i, 4:6)));
   
    tempVar(i, 4:6) = rotated_a;
    #Copy time measurements
    tempVar(i, 7:8) = bodyFrameMeasurementMatrix(i, 7:8);
  endfor

  retval = tempVar;

endfunction


#Function is responsible for reading file captures collected from the
#Motion Detection Unit hardware for analysis
#filename File and path of the MDU Capture file to read
#imu Matrix that contains all of the intepreted IMU data
#rfid Matrix of RFID tag detected events
function [imu,rfid] = readMduCaptureFile(filename)	
	entries = ones(0,8);
	
	fid = fopen(filename, 'r');
	while(!feof(fid))
		line = fgetl(fid);
		substrings = strsplit(line, ',');
		
		if(columns(substrings) >= 8)
			mtype = 'I';
		
			offset = 0;
			if(columns(substrings) ==  9) 
				mtype = substrings(1){:};
				offset = 1;
			endif
			if(mtype == 'I')
				t = str2double(substrings(offset + 1){:});
				accX = str2double(substrings(offset + 2){:});
				accY = str2double(substrings(offset + 3){:});
				accZ = str2double(substrings(offset + 4){:});
				temp = str2double(substrings(offset + 5){:});
				gyrX = str2double(substrings(offset + 6){:});
				gyrY = str2double(substrings(offset + 7){:});
				gyrZ = str2double(substrings(offset + 8){:});
			
				#Interpret values
				#Assuming that time is originally being measured in milliseconds
				t = t/1000.0;
		
				#Assuming that a MPU-6050 is being used and it is configured into 2g mode
				accX = (accX*(2/32768))*9.81;
				accY = (accY*(2/32768))*9.81;
				accZ = (accZ*(2/32768))*9.81;
		
				#Assuming that a MPU-6050 is being used and it is configured for 250 DPS mode
				gyrX = (gyrX*(250/32768))*(3.14159265359/180);
				gyrY = (gyrY*(250/32768))*(3.14159265359/180);
				gyrZ = (gyrZ*(250/32768))*(3.14159265359/180);
		
				sample = [t, accX, accY, accZ, temp, gyrX, gyrY, gyrZ];
				#printf("%s %2.2f %2.2f %2.2f %2.2f %2.2f %2.2f %2.2f %2.2f\n", mtype, t, accX, accY, accZ, temp, gyrX, gyrY, gyrZ);
				entries = [entries; sample];
			endif
		endif
	endwhile
	fclose(fid);

	imu = entries;
	rfid = zeros(0,0);
endfunction

#gyr 3D vector of gyroscope measurements (rad / s)
#acc 3D vector of accelerometer measurements (m/s^2)
function [f_rotm,f_omega,f_omega_vector] = updateRotationMatrix(gyr, dt, i_rotm, i_omega_p, i_omega_i, i_omega, i_omega_vector)

f_omega = gyr+i_omega_i;
f_omega_vector = i_omega + i_omega_p;

Update_Matrix = zeros(3,3);
Update_Matrix(1,2) = -dt*i_omega_vector(3);
Update_Matrix(1,3) = dt*i_omega_vector(2);
Update_Matrix(2,1) = dt*i_omega_vector(3);
Update_Matrix(2,3) = -dt*i_omega_vector(1);
Update_Matrix(3,1) = -dt*i_omega_vector(2);
Update_Matrix(3,2) = dt*i_omega_vector(1);

Temporary_Matrix = i_rotm*Update_Matrix;

f_rotm = i_rotm + Temporary_Matrix;

endfunction


function f_DCM_Matrix = normalizeRotationMatrix(DCM_Matrix)

f_DCM_Matrix = eye(3,3);
temporary = zeros(3,3);
err = -dot(DCM_Matrix(1,:), DCM_Matrix(2,:))*0.5;
temporary(1,:) = DCM_Matrix(2,:)*err;
temporary(2,:) = DCM_Matrix(1,:)*err;

temporary(1,:) = temporary(1,:) + DCM_Matrix(1,:);
temporary(2,:) = temporary(2,:) + DCM_Matrix(2,:);

temporary(3,:) = cross(temporary(1,:), temporary(2,:));

renorm = dot(temporary(1,:), temporary(1,:));

if(renorm < 1.5625 && renorm > 0.64)
renorm =  0.5*(3-renorm);
elseif(renorm < 100 && renorm > 0.01)
renorm = 1.0 / sqrt(renorm);
endif

f_DCM_Matrix(1,:) = temporary(1,:)*renorm;

renorm = dot(temporary(2,:), temporary(2,:));

if(renorm < 1.5625 && renorm > 0.64)
renorm =  0.5*(3-renorm);
elseif(renorm < 100 && renorm > 0.01)
renorm = 1.0 / sqrt(renorm);
endif

f_DCM_Matrix(2,:) = temporary(2,:)*renorm;

renorm = dot(temporary(3,:), temporary(3,:));

if(renorm < 1.5625 && renorm > 0.64)
renorm =  0.5*(3-renorm);
elseif(renorm < 100 && renorm > 0.01)
renorm = 1.0 / sqrt(renorm);
endif

f_DCM_Matrix(3,:) = temporary(3,:)*renorm;

endfunction

#TODO: Implement Drift Correction

#Gx,Gy,Gz,Ax,Ay,Az,time
#TODO: Receive initial orientation as input.
function inertialFrameMeasurements = DcmAlgorithm(bodyFrameMeasurements)

DCM_Matrix = eye(3,3);
omega_vector = zeros(1,3);
omega_p = zeros(1,3);
omega_i = zeros(1,3);
omega = zeros(1,3);

  n_orientation = zeros(rows(bodyFrameMeasurements), 3);
  n_acceleration = zeros(rows(bodyFrameMeasurements), 3);

  for(i= 1:rows(bodyFrameMeasurements))
      gyr = bodyFrameMeasurements(i, 1:3);
      dt = bodyFrameMeasurements(i, 8);
      #Matrix_update
      [DCM_Matrix, omega, omega_vector] = updateRotationMatrix(gyr, dt,       DCM_Matrix, omega_p, omega_i, omega, omega_vector);
      #Normalize
      DCM_Matrix = normalizeRotationMatrix(DCM_Matrix);

      #Calculate direction of body frame vector: RotationMatrix * AccVector
      b_acc_at_i = bodyFrameMeasurements(i, 4:6);

      n_acceleration(i,:) = DCM_Matrix * transpose(b_acc_at_i);

#Drift correction
#Euler angles
#This should be according to (yaw, pitch, roll ordered Euler angles.

      pitch = -asin(DCM_Matrix(3,1));
      roll = atan2(DCM_Matrix(3,2), DCM_Matrix(3,3));
      yaw = atan2(DCM_Matrix(2,1), DCM_Matrix(1,1));

      n_orientation(i,:) = [roll pitch yaw];

  endfor

  inertialFrameMeasurements = [ n_orientation n_acceleration bodyFrameMeasurements(:, 7:8) ];

endfunction


#Function estimates position from IMU measurements
function calculations = estimatePosition(fm_sort2, initialBodyYawOnInertialFrameInDegrees, numValuesForSmooth)
	#Calculate Orientation
	
	#Smoothing gyroscope data
	fm_sort2(:, 1:3) = [ transpose(smooth(fm_sort2(:, 1), numValuesForSmooth)) transpose(smooth(fm_sort2(:,2), numValuesForSmooth)) transpose(smooth(fm_sort2(:,3), numValuesForSmooth))];
	
	#Assuming that the body is initially at rest and gravity is the only thing that is acting upon it.
	#Determine approximately what the pitch and roll is on the object.
	#Roll, and Pitch like the rest of the calculations in the script are using the aerospace standard for rotation: 
	#Yaw, Pitch, Roll (i..e. Rxyz (the order of the subscripts is not a mistake))
	body_accx = mean(fm_sort2(1:20,4));
	body_accy = mean(fm_sort2(1:20,5));
	body_accz  = mean(fm_sort2(1:20,6));
	body_on_inertial_pitch = atan2(-1*body_accx, sqrt( body_accy*body_accy + body_accz*body_accz));
	body_on_inertial_roll = atan2(body_accy, body_accz);
	
	#Adjustment for specifically the initial orientation of the MDU
                     initialOrientationOnInertialFrame = [ body_on_inertial_roll; body_on_inertial_pitch ; initialBodyYawOnInertialFrameInDegrees*(pi/180)];
		
	orientation_x = delta_orientation(fm_sort2(:,1), fm_sort2(:,8));
	orientation_y = delta_orientation(fm_sort2(:,2), fm_sort2(:,8));
	orientation_z = delta_orientation(fm_sort2(:,3), fm_sort2(:,8));
	
	#Zero out the x and y since the unit should be stationary on those axes.
	orientation_x = zeros(rows(orientation_x), 1);
	orientation_y = zeros(rows(orientation_y), 1);
	
	bodyFrameMeasurements = [orientation_x orientation_y orientation_z fm_sort2(:, 4) fm_sort2(:, 5) fm_sort2(:, 6) fm_sort2(:, 7) fm_sort2(:, 8)];
	
	#3/5/16 NOTE: This should really be the InitialOrientation of the Object where it is expressed as the rotation
	#necessary to align the body axis coordinate frame with the inertial frame coordinate frame.
	inertialFrameMeasurements = calculateInertialFrameMatrix2(bodyFrameMeasurements, initialOrientationOnInertialFrame);
                     #inertialFrameMeasurements = DcmAlgorithm(bodyFrameMeasurements, initialOrientationOnInertialFrame);
	
	#Print ACC Output
	#inertialFrameMeasurements(1:100,4:6)
	
	#Post Coordinate Frame Transform Filter
	#inertialFrameMeasurements(:, 1:3) = hp_filter(inertialFrameMeasurements(:,1:3), 0.01);
	#inertialFrameMeasurements(:, 4:6) = hp_filter(inertialFrameMeasurements(:,4:6), 0.1);
	
	#Eliminate Gravity (Forces on the Z-Axis)
	#inertialFrameMeasurements(:, 6) = hp_filter(inertialFrameMeasurements(:,6), 20);
	
	#Smoothing attempt for processing data
	inertialFrameMeasurements(:, 4:6) = [ transpose(smooth(inertialFrameMeasurements(:, 4), numValuesForSmooth)) transpose(smooth(inertialFrameMeasurements(:,5), numValuesForSmooth)) transpose(smooth(inertialFrameMeasurements(:,6), numValuesForSmooth))];
	
	#Eliminate Gravity Effects
                     axOff = 0; #-0.2; #mean(inertialFrameMeasurements(1:10,4));
	ayOff = 0; #-0.1; #mean(inertialFrameMeasurements(1:10,5));
	azOff = mean(inertialFrameMeasurements(1:20,6));


	inertialFrameMeasurements=[inertialFrameMeasurements(:,1:3) (inertialFrameMeasurements(:,4)-axOff) (inertialFrameMeasurements(:,5)-ayOff) (inertialFrameMeasurements(:,6)-azOff) inertialFrameMeasurements(:,7:8)];
	
	#Shift Acceleration by 2.2 m/s^2 (Seems to be off by that much based on gravity reading about 7.58 m/s^2)
	#inertialFrameMeasurements(:, 4:6) += 1.1;
	
                     #disp(inertialFrameMeasurements(1:10,4:7));
		
 	#Estimate the velocity and position of the device
   	speed_x = speed(inertialFrameMeasurements(:,4), inertialFrameMeasurements(:,8), 0);
   	speed_y = speed(inertialFrameMeasurements(:,5), inertialFrameMeasurements(:,8), 0);
   	speed_z = speed(inertialFrameMeasurements(:,6), inertialFrameMeasurements(:,8), 0);
   	pos_x = distance(speed_x, inertialFrameMeasurements(:,8), 0);
   	pos_y = distance(speed_y, inertialFrameMeasurements(:,8), 0);
  	pos_z = distance(speed_z, inertialFrameMeasurements(:,8), 0);
		
	calculations = [ inertialFrameMeasurements(:,1:6) speed_x speed_y speed_z pos_x pos_y pos_z fm_sort2(:, 7) ];
endfunction

#Function interprets IMU data captured from a Motion Detection Unit (MDU) into position information that is saved
#in a separate CSV file.
#mduCaptureFile File and path of the MDU Capture file to read
#positionFile File path of the CSV file to write position estimates
function calculations = interpretPositionForMduCaptureFiles(mduCaptureFile, positionFile)
	
	[imu, rfid] = readMduCaptureFile(mduCaptureFile);
	
	printf("imu size: %d, %d\n", size(imu));
	
	#Calculate offset
	gxOff = mean(imu(1:10,6));
	gyOff = mean(imu(1:10,7));
	gzOff = mean(imu(1:10,8));
	axOff = mean(imu(1:10,2));
	ayOff = mean(imu(1:10,3));
	azOff = mean(imu(1:10,4));
	
	g=[(imu(:,6)-gxOff) (imu(:,7)-gyOff) (imu(:,8)-gzOff) imu(:,1)];
	a=[(imu(:,2)-axOff) (imu(:,3)-ayOff) (imu(:,4)-azOff) imu(:,1)];
	
	gt = (g(:,4));
	at = (a(:,4));
		
	a_time = a(:,4);
	g_time = g(:,4);
	
	#interpolate missing points if acc and gcc are not measured at the same time.
	#im1 = [interp1(g(:,4), g(:,1), a_time) interp1(g(:,4), g(:,2), a_time) interp1(g(:,4), g(:,3), a_time) a];	
	#im2 = [g(:, 1:3) interp1(a(:,4), a(:,1), g_time) interp1(a(:,4), a(:,2), g_time) interp1(a(:,4), a(:,3), g_time) g_time];	
	#fm = [im1;im2];
	
	#Assuming that gyroscope and accelerometer are measured at the same rate and at the same time.
	fm = [g(:, 1:3) a];

	#Rounding up	
	#fm_round = [(round(fm(:, 1:3) .* 100)./100) (round(fm(:, 4:6) .* 10)./10) fm(:, 7)];
	
	#Rounding down
	fm_round = [(floor(fm(:, 1:3) .* 1000)./1000) (floor(fm(:, 4:6) .* 1000)./1000) fm(:, 7)];
		
	#Sort 
	#Sorting by time
	fm_sort = sortrows(fm_round, 7);
	
	#Calculate the relative difference in time between samples
	fm_sort2 = [fm_sort [ 0 ; diff(fm_sort(:, 7))] ];
	
	#Assume zero for items that do not have a valid number
	fm_sort2(isnan(fm_sort2)) = 0;
	
	#Pre Coordinate Frame Transform Filter
	#fm_sort2(:, 1:3) = hp_filter(fm_sort2(:,1:3), 0.03);
	#fm_sort2(:, 4:6) = hp_filter(fm_sort2(:,4:6), 0.2);
                     #disp(fm_sort2(1:10,1:3));
                     
                     #Calculate position
	#calculations = estimatePosition(fm_sort2, -180, 11);
	#csvwrite(positionFile, calculations);
	
	if(positionFile == '')
	  	positionUpdates = zeros(0,4);
	else
	  positionUpdates=csvread(positionFile);
	endif
	
	initialOrientation = -pi; #-180 degrees
	[position, or, sp, scalarAcceleration] = resolvePosition(fm_sort2, positionUpdates, initialOrientation, 0.35);
	
	plot(scalarAcceleration(:,2), scalarAcceleration(:,1));
	
	calculations = [position or sp];
	
endfunction

#Function interprets IMU data captured from a Nexus 7 table into position information that is saved
#in a separate CSV file.
#gyroscopeFile File path of CSV with gyroscope measurements
#accelerometerFile File path of CSV with accelerometer measurements
#positionFile File path of the CSV file to write position estimates
function calculations = intepretPositionForDebugFiles(gyroscopeFile, accelerometerFile, positionFile)
	g=csvread(gyroscopeFile);
	a=csvread(accelerometerFile);
	
	gt = (g(:,4));
	at = (a(:,4));
	
	a_time = a(:,4);
	g_time = g(:,4);
	
	im1 = [interp1(g(:,4), g(:,1), a_time) interp1(g(:,4), g(:,2), a_time) interp1(g(:,4), g(:,3), a_time) a];
	
	im2 = [g(:, 1:3) interp1(a(:,4), a(:,1), g_time) interp1(a(:,4), a(:,2), g_time) interp1(a(:,4), a(:,3), g_time) g_time];
	
	fm = [im1;im2];
	
	#Convert from degrees to radians for processing.
	fm = [(pi/180)*fm(:,1:3) fm(:, 4:7)];
	
	#fm_round = [(round(fm(:, 1:3) .* 100)./100) (round(fm(:, 4:6) .* 10)./10) fm(:, 7)];
	#fm_round = [(floor(fm(:, 1:3) .* 100)./100) (floor(fm(:, 4:6) .* 10)./10) fm(:, 7)];
	fm_round = fm;
	
#	plot(fm(:,7), fm(:,4));
		
	#Sort 
	#t = fm_round(:, 7);
	#diff(t)
	
	fm_sort = sortrows(fm_round, 7);
	fm_sort2 = [fm_sort [ 0 ; diff(fm_sort(:, 7))] ];
	
	#Assume zero for items that do not have a valid number
	fm_sort2(isnan(fm_sort2)) = 0;
	
	#Filter
	#fm_sort2(:, 1:3) = hp_filter(fm_sort2(:,1:3), 0.002);
	#fm_sort2(:, 4:6) = hp_filter(fm_sort2(:,4:6), 0.2);
	
	#Calculate Orientation
	initialOrientationOnBodyFrame = [ 0; 0; 0];
	initialOrientationOnInertialFrame = [ 0; 0; 0];
	
	orientation_x = fm_sort2(:,1);
	orientation_y = fm_sort2(:,2);
	orientation_z = fm_sort2(:,3);
	
	bodyFrameMeasurements = [orientation_x orientation_y orientation_z fm_sort2(:, 4) fm_sort2(:, 5) fm_sort2(:, 6) fm_sort2(:, 7) fm_sort2(:, 8)];
	inertialFrameMeasurements = calculateVector(bodyFrameMeasurements, fm_sort2(:,1:3));
	
	#plot(inertialFrameMeasurements(:,7), inertialFrameMeasurements(:,4));
	
		#Smoothing attempt for processing data
	inertialFrameMeasurements(:, 4:6) = [ transpose(smooth(inertialFrameMeasurements(:, 4), 25)) transpose(smooth(inertialFrameMeasurements(:,5), 25)) transpose(smooth(inertialFrameMeasurements(:,6), 25))];
	
	#plot(inertialFrameMeasurements(:,7), inertialFrameMeasurements(:,4));
		
 	#Estimate the velocity and position of the device
   	speed_x = speed(inertialFrameMeasurements(:,4), inertialFrameMeasurements(:,8), 0);
   	speed_y = speed(inertialFrameMeasurements(:,5), inertialFrameMeasurements(:,8), 0);
   	speed_z = speed(inertialFrameMeasurements(:,6), inertialFrameMeasurements(:,8), 0);
   	pos_x = distance(speed_x, inertialFrameMeasurements(:,8), 0);
   	pos_y = distance(speed_y, inertialFrameMeasurements(:,8), 0);
  	pos_z = distance(speed_z, inertialFrameMeasurements(:,8), 0);
		
	calculations = [ inertialFrameMeasurements(:, 1:6) speed_x speed_y speed_z pos_x pos_y pos_z fm_sort2(:, 7) ];
	
	csvwrite(positionFile, [fm_sort2(:, 1:6) speed_x speed_y speed_z pos_x pos_y pos_z fm_sort2(:, 7:8)]);
endfunction


#Function interprets IMU data captured from a Nexus 7 table into position information that is saved
#in a separate CSV file.
#gyroscopeFile File path of CSV with gyroscope measurements
#accelerometerFile File path of CSV with accelerometer measurements
#positionFile File path of the CSV file to write position estimates
function intepretPositionForNexus7CaptureFiles(gyroscopeFile, accelerometerFile, positionFile)
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
	
	#Rounding down
	fm_round = [(floor(fm(:, 1:3) .* 1000)./1000) (floor(fm(:, 4:6) .* 1000)./1000) fm(:, 7)];
		
	#Sort 
	#t = fm_round(:, 7);
	#diff(t)
	
	fm_sort = sortrows(fm_round, 7);
	fm_sort2 = [fm_sort [ 0 ; diff(fm_sort(:, 7))] ];
	
	#Assume zero for items that do not have a valid number
	fm_sort2(isnan(fm_sort2)) = 0;
	
	#Filter
	fm_sort2(:, 1:3) = hp_filter(fm_sort2(:,1:3), 0.002);
	fm_sort2(:, 4:6) = hp_filter(fm_sort2(:,4:6), 0.2);
	
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
endfunction

#Function interprets IMU data captured from a Nexus 7 table into position information that is saved
#in a separate CSV file.
#gyroscopeFile File path of CSV with gyroscope measurements
#accelerometerFile File path of CSV with accelerometer measurements
#positionFile File path of the CSV file to write position estimates
function calculations = intepretPositionForNexus7CaptureFiles2(gyroscopeFile, accelerometerFile, positionFile)
	g=csvread(gyroscopeFile);
	a=csvread(accelerometerFile);
	
	
	#DEBUG: Swapping X& Y so that gimbal lock issue can be avoided.
	temp = g(:,1);
	g(:,1) = g(:,2);
	g(:,2) = temp;
	temp = a(:,1);
	a(:,1) = a(:,2);
	a(:,2) = temp;
	
	gt = (g(:,4)/1000000000);
	at = (a(:,4)/1000000000);
	
	
	#a_round = [(round(a(:, 1:3) .* 10)./10) at];
	#g_round = [(round(g(:, 1:3) .* 100)./100) gt];
	
	a_time = a(:,4);
	g_time = g(:,4);
	
	im1 = [interp1(g(:,4), g(:,1), a_time) interp1(g(:,4), g(:,2), a_time) interp1(g(:,4), g(:,3), a_time) a];
	
	im2 = [g(:, 1:3) interp1(a(:,4), a(:,1), g_time) interp1(a(:,4), a(:,2), g_time) interp1(a(:,4), a(:,3), g_time) g_time];
	
	#fm = [im1;im2];
	fm = im2;
	
	#Rounding down
	#fm_round = [(floor(fm(:, 1:3) .* 1000)./1000) (floor(fm(:, 4:6) .* 1000)./1000) fm(:, 7)];
	fm_round = fm;
		
	#Sort 
	#t = fm_round(:, 7);
	#diff(t)
	
	fm_sort = sortrows(fm_round, 7);
	fm_sort2 = [fm_sort [ 0 ; diff(fm_sort(:, 7))]/1000000000 ];
	fm_sort2(:,7) = fm_sort2(:,7)/1000000000 - fm_sort(1,7)/1000000000;
	
	#Assume zero for items that do not have a valid number
	fm_sort2(isnan(fm_sort2)) = 0;
	
	#Filter
	#fm_sort2(:, 1:3) = hp_filter(fm_sort2(:,1:3), 0.002);
	#fm_sort2(:, 4:6) = hp_filter(fm_sort2(:,4:6), 0.2);
	
	calculations = estimatePosition(fm_sort2, 0, 1);
	
	csvwrite(positionFile, calculations);
endfunction

