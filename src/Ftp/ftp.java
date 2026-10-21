package Ftp;

public class ftp {
    public String User;
    public Double weigth;
    public ftp(String user, Double weight){
        // Constructor for the Ftp.ftp c½lass
        User = user;
        weigth = weight;
    }
    public void setWeight(double weight){
        // Set the weight of the user
        weigth = weight;
    }
    public double getWeight(){
        // Get the weight of the user
        return weigth;
    }
    public void setUser(String user){
        // Set the user name
        User = user;
    }
    public String getUser(){
        // Get the user name
        return User;
    }

    public double ftpCalc(double x){
        // Take the FTP of the number x
        if(x> 650||x < 0){
            // Check if the power is actually possible might need to change as training and nutrition becomes better
            System.out.println("The specified power is not realistic:");
            return 0.00;
        }else {
            // The normal thinking is to take a 20 min test and multiply the amount with 0.95 to get the Functional Threshold Power
            double ftp = x * 0.95;
            // prints the FTP out
            printFtp(ftp);
            // Also gets the zones out
            printFTPZone(ftp);
            return ftp;
        }
    }

    public void printFtp(double x){
        // Print the FTP of the number x;
        IO.println(String.format("The FTP of for " + User + " is " + x, x, User));
        // When choosen without weight then it will be null and thus needs this.
        if(weigth== null){
        }
        // Checks if the weight is larger than zero.
        else if(weigth > 0) {
            IO.println(String.format("The FTP to Weigth ratio is " + x / weigth, x / weigth));
        }
    }

    public void printFTPZone(double x){
        // Print the FTP zones of the number x

        double ftp = x;
        double z7 = ftp*7.00;
        if (z7>2000){
            z7 = 2000;
        }

        IO.println(String.format("Zone 1(Rest): %.2f - %.2f", ftp * 0.00, ftp * 0.75));
        IO.println(String.format("Zone 2(Endurance): %.2f - %.2f", ftp * 0.76, ftp * 0.90));
        IO.println(String.format("Zone 3(Tempo): %.2f - %.2f", ftp * 0.91, ftp * 1.05));
        IO.println(String.format("Zone 4(Threshold): %.2f - %.2f", ftp * 1.06, ftp * 1.20));
        IO.println(String.format("Zone 5(VO2max): %.2f - %.2f", ftp * 1.21, ftp * 1.50));
        IO.println(String.format("Zone 6(Anaerobic): %.2f - %.2f", ftp * 1.51, ftp * 2.00));
        IO.println(String.format("Zone 7(Sprint): %.2f - %.2f", ftp * 2.01, z7));
    }
}
