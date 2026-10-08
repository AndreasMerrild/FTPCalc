public class ftp {
    public String User;
    private double weigth;
    ftp(String user, double weight){
        // Constructor for the ftp c½lass
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

        double ftp = x * 0.95;
        printFtp(ftp);
        printFTPZone(ftp);
        return ftp;
    }

    private void printFtp(double x){
        // Print the FTP of the number x;

        IO.println(String.format("The FTP of for " + User + " is " + x, x, User));
        if(weigth > 0) {
            IO.println(String.format("The FTP to Weigth ratio is " + x / weigth, x / weigth));
        }
    }

    public void printFTPZone(double x){
        // Print the FTP zones of the number x

        double ftp = x;


        IO.println(String.format("Zone 1(Rest): %.2f - %.2f", ftp * 0.00, ftp * 0.75));
        IO.println(String.format("Zone 2(Endurance): %.2f - %.2f", ftp * 0.76, ftp * 0.90));
        IO.println(String.format("Zone 3(Tempo): %.2f - %.2f", ftp * 0.91, ftp * 1.05));
        IO.println(String.format("Zone 4(Threshold): %.2f - %.2f", ftp * 1.06, ftp * 1.20));
        IO.println(String.format("Zone 5(VO2max): %.2f - %.2f", ftp * 1.21, ftp * 1.50));
        IO.println(String.format("Zone 6(Anaerobic): %.2f - %.2f", ftp * 1.51, ftp * 2.00));
        IO.println(String.format("Zone 7(Sprint): %.2f - %.2f", ftp * 2.01, ftp * 7.00));
    }
}
