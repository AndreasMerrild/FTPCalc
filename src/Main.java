import java.util.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printOptions();
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
        else{
            System.out.println("This is not applicable at the moment please try one of the other options. ");
        }
    }
    public static void printOptions(){
        System.out.println("Welcome to the power calculater. ");
        System.out.println("Please submit all numbers as doubles (XXX.XX)");
        System.out.println("Let's start by choosing what part you want.");

        System.out.println("Type in the number for the functionality you want:");
        System.out.println("1 : FTP zone calculation w weight");
        System.out.println("2 : FTP zone calculation w/o weight");

    }
}
