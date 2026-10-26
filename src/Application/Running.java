package Application;

import Ftp.ftp;
import Fit.FitActivity;
import Fit.FitParser;

import java.io.IOException;
import java.nio.file.Path;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.util.Scanner;

public class Running {
        public void run() {
            Scanner scanner = new Scanner(System.in);
            boolean active = true;
            boolean first = true;
            while(active) {
                printOptions(first);
                if(first) {
                    first = false;
                }
                active = goInto(scanner, active);
            }
        }
        public void printOptions(boolean first){
            if(first == true) {
                System.out.println("Welcome to the power calculater. ");
                System.out.println("Please submit all numbers as doubles (XXX.XX)");
                System.out.println("Let's start by choosing what part you want.");
            }
            System.out.println("Type in the number for the functionality you want:");
            System.out.println("1 : FTP zone calculation w weight");
            System.out.println("2 : FTP zone calculation w/o weight");
            System.out.println("3 : Parse a .fit activity file");

            System.out.println("0 : Exit program");
        }
        public boolean goInto(Scanner scanner, boolean active){
            int firstInput = scanner.nextInt();
            if(firstInput==1) {
                System.out.println("Let's start with the athlete name");
                String name = scanner.next();
                System.out.println("Please specify the weight of the athlete in question. ");
                double weight = scanner.nextDouble();
                System.out.println("The power from the 20 min test.");
                double power = scanner.nextDouble();
                ftp myFtp = new ftp(name, weight);
                myFtp.ftpCalc(power);
            }
            else if(firstInput==2){
                System.out.println("Let's start with the athlete name");
                String name = scanner.next();
                System.out.println("The power from the 20 min test.");
                double power = scanner.nextDouble();
                ftp myFtp = new ftp(name, null);
                myFtp.ftpCalc(power);
            }
            else if(firstInput==3){
                Path file = chooseFitFile();
                if (file == null) {
                    System.out.println("No FIT file selected.");
                    return active;
                }

                try {
                    FitActivity activity = new FitParser().parse(file);
                    System.out.println("Parsed " + activity.records().size() + " records.");
                    System.out.println("Start: " + activity.startTime());
                    System.out.println("End: " + activity.endTime());
                    System.out.println("Duration: " + formatDuration(activity.duration()));
                    printAverage("Average power", activity.averagePower(), " W");
                    printAverage("Average speed", activity.averageSpeed(), " m/s");
                    printAverage("Average cadence", activity.averageCadence(), " rpm");
                    printAverage("Average heart rate", activity.averageHeartRate(), " bpm");
                    if (activity.deviceName() != null) {
                        System.out.println("Device: " + activity.deviceName());
                    }
                } catch (IOException | RuntimeException exception) {
                    System.out.println("Could not parse FIT file: " + exception.getMessage());
                    if (exception.getCause() != null) {
                        System.out.println("Decoder detail: " + exception.getCause());
                    }
                }
            }
            else if(firstInput==0){
                System.out.println("See you later. ");
                active = false;
            }
            else{
                System.out.println("This is not applicable at the moment please try one of the other options. ");

            }
            return active;
        }

        private void printAverage(String label, Double value, String unit) {
            if (value == null) {
                System.out.println(label + ": unavailable");
            } else {
                System.out.printf("%s: %.2f%s%n", label, value, unit);
            }
        }

        private String formatDuration(java.time.Duration duration) {
            long seconds = duration.getSeconds();
            return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        }

        private Path chooseFitFile() {
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println("Cannot open the FIT file chooser because Java is running in headless mode.");
                return null;
            }

            FileDialog fileDialog = new FileDialog(
                    (Frame) null, "Select a FIT activity file", FileDialog.LOAD);
            fileDialog.setFilenameFilter((directory, name) ->
                    name.toLowerCase(java.util.Locale.ROOT).endsWith(".fit"));
            fileDialog.setAlwaysOnTop(true);
            fileDialog.setVisible(true);

            if (fileDialog.getFile() == null) {
                return null;
            }

            return Path.of(fileDialog.getDirectory(), fileDialog.getFile());
        }
}
