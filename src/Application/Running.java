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
                System.out.println("---------------------------------------------------");
                System.out.println("Welcome to the power calculater. ");
                System.out.println("Please submit all numbers as doubles (XXX.XX)");
                System.out.println("Let's start by choosing what part you want.");
            }
            System.out.println("---------------------------------------------------");
            System.out.println("Type in the number for the functionality you want:");
            System.out.println("1 : FTP zone calculation w weight");
            System.out.println("2 : FTP zone calculation w/o weight");
            System.out.println("3 : Parse a .fit activity file");
            System.out.println("4 : Parse multiple .fit activity files");

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
                System.out.println("Please select a FIT activity file to parse.");
                Path file = fitFileUsage.chooseFitFile();
                if (file == null) {
                    System.out.println("No FIT file selected.");
                    return active;
                }

                try {
                    FitActivity activity = new FitParser().parse(file);
                    System.out.println("Parsed " + activity.records().size() + " records.");
                    System.out.println("Start: " + activity.startTime());
                    System.out.println("End: " + activity.endTime());
                    System.out.println("Duration: " + fitFileUsage.formatDuration(activity.duration()));
                    fitFileUsage.printAverage("Average power", activity.averagePower(), " W");
                    fitFileUsage.printAverage("Average speed", activity.averageSpeed(), " km/h");
                    fitFileUsage.printAverage("Average cadence", activity.averageCadence(), " rpm");
                    fitFileUsage.printAverage("Average heart rate", activity.averageHeartRate(), " bpm");
                    if (activity.deviceName() != null) {
                        System.out.println("Device: " + activity.deviceName());
                    }
                    Graph.showPowerGraph(activity);
                    Graph.showBestPowerGraph(activity);
                } catch (IOException | RuntimeException exception) {
                    System.out.println("Could not parse FIT file: " + exception.getMessage());
                    if (exception.getCause() != null) {
                        System.out.println("Decoder detail: " + exception.getCause());
                    }
                }
            }
            else if(firstInput==4){
                System.out.println("Enter the directory containing the FIT files:");
                scanner.nextLine();
                String directoryText = scanner.nextLine().trim();
                if (directoryText.isEmpty()) {
                    System.out.println("No directory specified.");
                    return active;
                }

                try {
                    multiFileUsage usage = new multiFileUsage();
                    usage.loadFiles(Path.of(directoryText));
                    usage.printSummary();
                } catch (IOException | RuntimeException exception) {
                    System.out.println("Could not load FIT files: " + exception.getMessage());
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


}
