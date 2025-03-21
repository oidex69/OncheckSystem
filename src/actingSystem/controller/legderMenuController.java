package actingSystem.controller;

import actingSystem.App;
import actingSystem.LinkedNode;
import actingSystem.database.databaseAction;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class legderMenuController implements Initializable {

    @FXML
    GridPane accountContainer;

    @FXML
    DatePicker showDate,showDateEnd;

    @FXML 
    Button goBackButton;

    @FXML
    ImageView backButton;

    private int rowCounter,dataIndex;

    private LinkedNode node;
    private databaseAction action;

     private LocalDate getDate() {
        return showDate.getValue();
    }
    
    private void setDate(){
        LinkedNode node = new LinkedNode();
        showDate.setValue(node.getDate());
        showDateEnd.setValue(node.getDate());
    }

    private LocalDate getEndDate() {
        return showDateEnd.getValue();
    }

    public void setDate(LocalDate date){
        showDate.setValue(date);
        showDateEnd.setValue(date);
    }

    public void goMain(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/Main3.0.fxml","MainMenu",null,false,true);
        } catch (Exception e) {

        }
    }

    public void goToBankAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Bank Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToPurchaseAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Purchase Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToSalesAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Sale's Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToPurchaseReturnAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Purchase Return Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToSalesReturnAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Sale's Return Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToDebitorsAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Debtor's Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToCreditorsAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Creditor's Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToCapitalAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Capital Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToCashAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Cash Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToWageAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Wage Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToRentAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Rent Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    public void goToSalaryAcc(){
        try {
            App.changeScene("/actingSystem/fxml/Ledger2.0.fxml","Salary Account",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    private void initial(){
        accountContainer.getChildren().clear();  // Clear previous data entries
        rowCounter = 0;                  // Reset rowCounter to start fresh
        
        action = new databaseAction();
        dataIndex = action.countWithDate(getDate(),getEndDate());
        node = action.read(dataIndex, getDate(), getEndDate());
    }

    @SuppressWarnings("unused")
    private void setAction(String title,Button button){
        if(title.equals("Bank")){
            button.setOnAction(event->goToBankAcc());

        }else if(title.equals("Cash")){
            button.setOnAction(event->goToCashAcc());

        }else if(title.equals("Capital")){
            button.setOnAction(event->goToCapitalAcc());

        }else if(title.equals("Purchase")){
            button.setOnAction(event->goToPurchaseAcc());

        }else if(title.equals("Sales")){
            button.setOnAction(event->goToSalesAcc());

        }else if(title.equals("Purchase Return")){
            button.setOnAction(event->goToPurchaseReturnAcc());

        }else if(title.equals("Sales Return")){
            button.setOnAction(event->goToSalesReturnAcc());

        }else if(title.equals("Debtors")){
            button.setOnAction(event->goToDebitorsAcc());

        }else if(title.equals("Creditor")){
            button.setOnAction(event->goToCreditorsAcc());

        }else if(title.equals("Wages")){
            button.setOnAction(event->goToWageAcc());

        }else if(title.equals("Rent")){
            button.setOnAction(event->goToRentAcc());

        }else if(title.equals("Salary")){
            button.setOnAction(event->goToSalaryAcc());

        }
    }

    private void createButton(String title){
        Button button = new Button();
        button.setText(title);
        button.setPrefWidth(186); 
        // button.setPrefHeight(40); 
        button.setMinHeight(40);
        
        setAction(title,button);

        GridPane.setMargin(button, new Insets(10, 10, 10, 10));

        accountContainer.add(button, 0, rowCounter);


        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(button, VPos.CENTER);
        
        rowCounter++;
    }

    public void showList(){
        Boolean cash=false,bank=false,prucahse=false,capital=false,sales=false,pruchaseReturn=false,salesReturn=false,debtors=false,creditors=false,wages=false,rent=false,salary=false;

        initial();

        for(int i = 0;i<dataIndex; i++){
            if((node.credit[i].equals("Bank")||node.debit[i].equals("Bank"))  && bank == false){
                bank=true;
                createButton("Bank");
                
            }
            if(node.credit[i].equals("Capital") && capital == false){
                capital=true;
                createButton("Capital");
                
                
            }
            if((node.credit[i].equals("Cash")||node.debit[i].equals("Cash"))  && cash == false){
                cash=true;
                createButton("Cash");
                
                
            }
            if(node.debit[i].equals("Purchase")&& prucahse == false){
                prucahse=true;
                createButton("Purchase");
                
                
            }
            if(node.credit[i].equals("Sales")  && sales == false){
                sales=true;
                createButton("Sales");
                
                
            }
            if(node.credit[i].equals("Purchase Return")  && pruchaseReturn == false){
                pruchaseReturn=true;
                createButton("Purchase Return");
                
                
            }
            if(node.debit[i].equals("Sales Return")  && salesReturn == false){
                salesReturn=true;
                createButton("Sales Return");
                
                
            }
            if((node.credit[i].equals("Debtors")||node.debit[i].equals("Debtors"))  && debtors == false) {
                debtors=true;
                createButton("Debtors");
            }
            if((node.credit[i].equals("Creditor")||node.debit[i].equals("Creditor"))  && creditors == false) {
                creditors = true;
                createButton("Creditor");
            }
            if(node.debit[i].equals("Wages") && wages == false) {
                wages=true;
                createButton("Wages");
            }
            if(node.debit[i].equals("Rent") && rent == false) {
                rent=true;
                createButton("Rent");
            }
            if(node.debit[i].equals("Salary") && salary == false) {
                salary=true;
                createButton("Salary");
            }

        }
        
    }

    @SuppressWarnings("unused")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDate();
        showList();
        node.effects(goBackButton, backButton);
        showDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                showList();
            }
            });
    }


    
}
