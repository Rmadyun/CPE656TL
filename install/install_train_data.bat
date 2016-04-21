"C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" -uroot -proot < "Initialize Database Script"
java -jar C:\TrainTrax\CPE656TL-master\install\TrainNavigationDatabase.MeasurementImport.jar -p ".\MainTrackMeasurements.csv" -s ".\TrackSwitchMeasurements.csv"
echo "Database Initialized"
pause