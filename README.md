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
also prints a summary containing duration and the average power, speed, heart
rate, and cadence. Speed is converted from the FIT file's m/s value and reported in km/h.
The application also opens a power-over-time line graph for records containing
both a timestamp and power value. The graph uses a 3-second moving average,
shows time in minutes, uses 100 W intervals on the power axis with headroom
above the maximum power, and draws only the smoothed line.