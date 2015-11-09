#November 3, 2015
#CPE 656 Train Trax Project
#Corey Sanders
#Script is responsible for estimating the position of an object based on collected IMU measurements.


#You can use the Quadratic formula to find the coefficients for factoring something of the form: ax^2 + bx + c = 0
#(x,y) = Position of Point Desired to Be Triangulated (Point T)
#(a1,b1) = Position of Point 1
#r1 = Distance of Point 1 from Point T
#(a2,b2) = Position of Point 2
#r2 = Distance of Point 2 from Point T
#(a3,b3) = Position of Point 3
#r3 = Distance of Point 3 from Point T
#A: x^2 - 2a1x - 2b1y + y^2 + a1^2 + b1^2 = r1^2
#B: x^2 - 2a2x - 2b2y + y^2 + a2^2 + b2^2 = r2^2
#C: x^2 - 2a3x - 2b3y + y^2 + a3^2 + b3^2 = r3^2

#(Pt1 - Pt2) = -2a1x + 2a2x - 2b1y + 2b2y + a1^2 - a2^2 + b1^2 - b2^2 = r1^2 - r2^2
#(Pt2 - Pt3) = -2a2x + 2a3x - 2b2y + 2b3y + a2^2 - a3^2 + b2^2 - b3^2 = r2^2 - r3^2


#Test:
# (x,y) = (1,1)
# (a1,b1) = (1,0)
# (a2,b2) = (0,1)
# (a3,b3)= (0,0)
# r1 = 1
# r2 = 1
# r3 = sqrt(2);
   function retval = triangulatePosition (pt1, pt2, pt3, distanceFromPt1, distanceFromPt2, distanceFromPt3)
   a1 = pt1(1, 1);
   b1 = pt1(1, 2);
   a2 = pt2(1, 1);
   b2 = pt2(1, 2);
   a3 = pt3(1,1);
   b3 = pt3(1,2);
   r1 = distanceFromPt1;
   r2 = distanceFromPt2;
   r3 = distanceFromPt3;

   equations = [ (-2*a1 + 2*a2) (-2*b1 + 2*b2) ; (-2*a2 + 2*a3) (-2*b2 + 2*b3) ];
   answers = [( (r1*r1 - r2*r2) - (a1*a1 - a2*a2 + b1*b1 - b2*b2)) ; ((r2*r2 - r3*r3) - (a2*a2 + a3*a3 + b2*b2 - b3*b3)) ];

   retval = inverse(equations)*answers;

   endfunction
   
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


#Y is the height of two right triangles that are part of a bigger triangle that includes the 2 known reference points
#Once you find Y, you have an angle (90 degrees), then a side, then another side. (SSA) https://www.mathsisfun.com/algebra/trig-solving-ssa-triangles.html
#Alternative
#d = distance between (a1,b1) and (a2,b2)
#e = r1
#f = r2
#-2y^2 + 2yd - d^2 + f^2 - e^2 = 0
#use the quadratic formula to solve for y
#Use law of cosines to solve for x.
#You can also use the pythogorean theorem
# #assuming that (a1,b1) is to the left of (a2,b2) and (a2,b2) = (0,0)
#then
# x^2 + y^2 = r2^2

#Test:
# (x,y) = (1,1)
# (a1,b1) = (1,0)
# (a2,b2)= (0,0)
# r1 = 1
# r2 = sqrt(2);

#Assumes one of the points is the origin.
   function retval = triangulatePositionUsingOrigin (pt1, distanceFromPt1, distanceFromOrigin)
     pt2  = [0 0];
     d = distancePoints(pt1, pt2);
     e = distanceFromPt1;
     f = distanceFromOrigin;

     #Calculating coefficients to find roots to solve for y
     a = -2;
     b = 2*d;
     c = f*f - d*d - e*e;

#     y_solutions = roots([ a b c]);
 #    x_solutions = ones(rows(y_solutions), 1);
     
    # for(i= 1:rows( y_solutions ))
     #         y = y_solutions(i, 1);
      #        x_solutions(i,1) = sqrt(e*e - (d-y)*(d-y));
       #endfor
       
       y = (1/(-2*d))*(-(d*d) + (e*e) - (f*f));
       x = sqrt((e*e) - ((d-y)*(d-y)));
     
     retval = [x y];
   endfunction



dataCollectionFile="/home/death/Documents/Track Geometry Data Collection.csv";

d=csvread(dataCollectionFile);
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
orientation_triangulation_vector1 = [ reference_pt1_position reference_pt2_position reference_pt3_position orientation_triangulation_pt1(:, 2:4) ];
orientation_triangulation_vector2 = [ reference_pt1_position reference_pt2_position reference_pt3_position orientation_triangulation_pt2(:, 2:4) ];

pos = triangulatePositionFromTable(position_triangulation_vector);
or1 = triangulatePositionFromTable(orientation_triangulation_vector1);
or2 = triangulatePositionFromTable(orientation_triangulation_vector2);
