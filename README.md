# FTPCalc

This is a project which will at some point make a power analytics software. 
Though as of yet, it only has a simple calculator for FTP, the Zones and Power to Weight ratio.

Run the Main application and input the metrics that are asked for (name, weight, Power)

## FIT files

The project uses Garmin's official FIT Java SDK through Maven. Import the project
from `pom.xml` in IntelliJ, then reload the Maven project so the dependency is
downloaded.

Choose `3 : Parse a .fit activity file` in the application and enter the path to
the FIT file. `FitParser` decodes the activity records and exposes timestamps,
power, heart rate, speed, and cadence through `FitActivity`. The application
also prints a summary containing duration and the average power, speed, and
cadence. Speed is reported in the units stored by the FIT file, normally m/s.