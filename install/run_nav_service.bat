set "PR3_PORT=COM4"
set "MDU_PORT=COM5"
set "SERVICE_PORT=8183"
java "-Djava.library.path=C:\Program Files (x86)\JMRI\lib\x64" -Dfile.encoding=Cp1252  -jar "C:\TrainTrax\CPE656TL-master\install\TrainNavigationService.RestService.jar" --service-port=%SERVICE_PORT% --pr3-port=%PR3_PORT% --mdu-port=%MDU_PORT%
pause
