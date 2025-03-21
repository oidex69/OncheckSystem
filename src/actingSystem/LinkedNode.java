package actingSystem;

import java.io.Serializable;
// import java.util.Arrays;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LinkedNode implements Serializable {
    private int[] dataCollectionCounter;
    public LocalDate[] date;
    private LocalDate[] editDate;
    public String[] credit;
    public String[] debit;
    public String[] descp; 
    public double[] amount;
    private static boolean saveFlag;
    private static String userName;

    private static boolean acctLoginStat;
    private  static boolean userLoginStat;

    private static int acctId;

    // private String[] accountType;

    public static void setAcctId(int id){
        acctId=id;
    }

    public static int getAcctId(){
        return acctId;
    }

    public static void setacctLoginStat(boolean stat){
        acctLoginStat = stat;
    }
    
    public static void setuserLoginStat(boolean stat){
        userLoginStat = stat;
    }

    public static boolean getAcctLoginStat(){
        return acctLoginStat;
    }

    public static boolean getUserLoginStat(){
        return userLoginStat;
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static String getUserName(){
        return userName;
    }
    
    public static void setSave(boolean flag){
        saveFlag = flag;
    }

    public static boolean getSaveFlag(){
        return saveFlag;
    }

    public LocalDate getDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.toLocalDate(); // Extracting LocalDate from LocalDateTime
    }

    public String getDateString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return getDate().format(formatter);
    }

    public void setEditDate(int index, LocalDate editDate){
        this.editDate[index] = editDate;
    }

    public LocalDate getEditDate(int index){
        return editDate[index];
    }

    public void setDataCollectionCounter(int index, int dataCollectionCounter){
        this.dataCollectionCounter[index] = dataCollectionCounter;
    }

    public int getDataCollectionCounter(int index){
        return dataCollectionCounter[index];
    }

    public void addDate(int index,String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate parseDate = LocalDate.parse(date,formatter);
        this.date[index] = parseDate;
    }

    public void initialize(int index){
        credit = new String[index];
        debit = new String[index];
        descp = new String[index];
        amount = new double[index];
        date = new LocalDate[index];
    }

    public void initializeEdit(int index){
        dataCollectionCounter = new int[index];
        editDate = new LocalDate[index];
        credit = new String[index];
        debit = new String[index];
        descp = new String[index];
        amount = new double[index];
        date = new LocalDate[index];
    }

    public void addCredit(int index, String credit) {
        this.credit[index] = credit;
    }

    public String getCredit(int index){
        return credit[index];
    }

    public void addDebit(int index, String debit) {
        this.debit[index] = debit;
    }

    public String getDebit(int index){
        return debit[index];
    }

    public void adddesc(int index, String desc) {
        this.descp[index] = desc;
    }

    public String getdesc(int index){
        return descp[index];
    }

    public void addamount(int index, double amount) {
        this.amount[index] = amount;
    }

    public double getAmount(int index){
        return amount[index];
    }

    public void printAll(int index) {
        double total=0;
        System.out.printf("%-7s %-10s %-15s %-15s %-15s %15s%n", "index","Date","Debit", "Credit", "Description", "Amount");
        for(int i=0;i < index ;i++){
           if(credit[i] != null){
                System.out.printf("%-7d %-10s %-15s %-15s %-15s %15.2f%n",i+1,date[i],debit[i], credit[i], descp[i], amount[i]);
                total+=amount[i];
           }else{
                break;
           }
        }
        System.out.println("Total amount = "+total);
    }

    @SuppressWarnings("unused")
   public void effects(Button goBackButton, ImageView backButton) {
    goBackButton.setOnMouseEntered(event -> {
        // Fade out the current image
        FadeTransition fadeOut = new FadeTransition(Duration.millis(50), backButton);
        fadeOut.setFromValue(1.0); // Fully visible
        fadeOut.setToValue(0.0);   // Fully invisible
        fadeOut.setInterpolator(Interpolator.EASE_BOTH);

        // After fade-out, change the image and fade it in
        fadeOut.setOnFinished(e -> {
            backButton.setImage(new Image("/actingSystem/css/backButtonHover.png")); // Change the image

            FadeTransition fadeIn = new FadeTransition(Duration.millis(50), backButton);
            fadeIn.setFromValue(0.0); // Start invisible
            fadeIn.setToValue(1.0);   // Fully visible
            fadeIn.setInterpolator(Interpolator.EASE_BOTH);
            fadeIn.play();
        });

        fadeOut.play(); // Start fade-out
    });

    goBackButton.setOnMouseExited(event -> {
        // Fade out the current image
        FadeTransition fadeOut = new FadeTransition(Duration.millis(50), backButton);
        fadeOut.setFromValue(1.0); // Fully visible
        fadeOut.setToValue(0.0);   // Fully invisible
        fadeOut.setInterpolator(Interpolator.EASE_BOTH);

        // After fade-out, change back to the default image and fade it in
        fadeOut.setOnFinished(e -> {
            backButton.setImage(new Image("/actingSystem/css/backButton.png")); // Revert to default image

            FadeTransition fadeIn = new FadeTransition(Duration.millis(50), backButton);
            fadeIn.setFromValue(0.0); // Start invisible
            fadeIn.setToValue(1.0);   // Fully visible
            fadeIn.setInterpolator(Interpolator.EASE_BOTH);
            fadeIn.play();
        });

        fadeOut.play(); // Start fade-out
    });
}

}


