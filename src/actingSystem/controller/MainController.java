package actingSystem.controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


import actingSystem.App;
import actingSystem.LinkedNode;

public class MainController implements Initializable {

    @FXML
    Label showDate,userName;

    @FXML
    Button transaction,returnTransaction,goToDisplay; 

    @FXML
    GridPane userGridPane, accountantGridPane;

    @FXML
    VBox transactionView,displayView,displayVBox;
    

    public void showDate(){
        // userName.setText(LinkedNode.getUserName());
        LinkedNode link = new LinkedNode();
        showDate.setText(link.getDateString());
        checkUser();
    }

    public void goToPayment(){
        try {
            App.changeScene("/actingSystem/fxml/payment.fxml","Payment",null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToRecepit(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/payment.fxml","Receipt",null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToIncomeStatment(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/IncomeExpenses.fxml",null,null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToJournalEntry(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/payment.fxml","Journal Entry",null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToJournal(ActionEvent event){
         try {
            App.changeScene("/actingSystem/fxml/journal.fxml",null,null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToLedgerMenu(){
        try {
            App.changeScene("/actingSystem/fxml/LedgerMenu.fxml",null,null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToTrial(){
        try {
            App.changeScene("/actingSystem/fxml/TrialBalance.fxml",null,null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToPl(){
        try {
            App.changeScene("/actingSystem/fxml/PlAccount.fxml",null,null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToTrading(){
        try {
            App.changeScene("/actingSystem/fxml/TradingAccount.fxml",null,null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToBalanceSheet(){
        try {
            App.changeScene("/actingSystem/fxml/BalanceSheet.fxml",null,null,false,false);
        } catch (Exception e) {

        }
    }

    public void goToEdit(){
        try {
            App.changeScene("/actingSystem/fxml/Edit.fxml","Edit",null,false,false);
        } catch (Exception e) {

        }
    }

    private void checkUser(){
        // if(LinkedNode.getUserLoginStat()){
        //     userGridPane.setVisible(true);
        //     accountantGridPane.setVisible(false);
        // }
        // if(LinkedNode.getAcctLoginStat()){
        //     // userGridPane.setVisible(false);
        //     accountantGridPane.setVisible(true);
        // }
    }

    
    @SuppressWarnings("unused")
    public void switchView(ActionEvent event){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.2));
        fadeOut.setFromValue(1.0); 
        fadeOut.setToValue(0.0);  

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2));
        fadeIn.setFromValue(0.0);  
        fadeIn.setToValue(1.0);   

       
        if (transactionView.isVisible()) {
            
            fadeOut.setNode(transactionView);
            fadeOut.setOnFinished(e -> {
                transactionView.setVisible(false); 
                displayView.setVisible(true);  
                fadeIn.setNode(displayView); 
                fadeIn.play();  
            });
            fadeOut.play();  
        } else {
            fadeOut.setNode(displayView);
            fadeOut.setOnFinished(e -> {
                displayView.setVisible(false);
                transactionView.setVisible(true); 
                fadeIn.setNode(transactionView); 
                fadeIn.play();  
            });
            fadeOut.play();  
        }
    }

    private void loadView(){
        if(LinkedNode.getAcctLoginStat()){
            chooseFirstAppearence(true,false);
        }else if(LinkedNode.getUserLoginStat()){
            chooseFirstAppearence(false, true);
        }
    }

    public void chooseFirstAppearence(boolean transaction,boolean report){
        System.out.println( transaction +""+report);
        if(transaction){
            // System.out.println("accountant loaded here");
            transactionView.setVisible(true); 
            displayView.setVisible(false);
        }else if(report){
            // System.out.println("user loaded here");
            if(LinkedNode.getUserLoginStat()){
                transactionView.setVisible(false); 
                displayView.setVisible(true);
                displayVBox.getChildren().remove(returnTransaction);
            }else{
                transactionView.setVisible(false); 
                displayView.setVisible(true);
            }
        }else{
            // System.out.println("loaded here");
            transactionView.setVisible(true); 
            displayView.setVisible(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactionView.setVisible(true);
        displayView.setVisible(false);
        loadView();
        showDate();
        userName.setText(LinkedNode.getUserName());
        returnTransaction.setOnAction(this::switchView);
        goToDisplay.setOnAction(this::switchView);
    }

}
