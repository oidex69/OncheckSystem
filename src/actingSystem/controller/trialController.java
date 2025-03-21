package actingSystem.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import actingSystem.App;
import actingSystem.LinkedNode;
import actingSystem.database.databaseAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class trialController implements Initializable {
    @FXML
    DatePicker showDate,showDateEnd;

    @FXML
    GridPane gridPane;

    @FXML 
    Label totalAmountCr,totalAmountDr;

    @FXML 
    Button goBackButton;

    @FXML
    ImageView backButton;

    private int rowCounter,dataIndex;
    private double amtCr,amtDr,totalDr,totalCr,totalAmntDr,totalAmntCr;
    private databaseAction action;
    private LinkedNode node;

    

    public void goMain(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/Main3.0.fxml","MainMenu",null,false,true);
        } catch (Exception e) {

        }
    }

    private LocalDate getDate() {
        return showDate.getValue();
    }

    private LocalDate getEndDate() {
        return showDateEnd.getValue();
    }

    private void setDate(){
        node = new LinkedNode();
        showDate.setValue(node.getDate());
        showDateEnd.setValue(node.getDate());
    }

    private void initial(){
        gridPane.getChildren().clear();  // Clear previous data entries
        rowCounter = 0;                  // Reset rowCounter to start fresh
        totalAmntDr=0;                 // Reset total amount
        totalAmntCr=0;                 // Reset total amount
        
        action = new databaseAction();
        dataIndex = action.countWithDate(getDate(),getEndDate());
        node = action.read(dataIndex, getDate(),getEndDate());
    }

    private void setBalance(){
        if(amtDr>amtCr){
            totalDr=0;
            totalDr = amtDr-amtCr;
            totalAmntDr+=totalDr;
        }else if(amtCr>amtDr){
            totalCr=0;
            totalCr = amtCr-amtDr;
            totalAmntCr+=totalCr;
        }
    }
    
    private void calculateData(String title){
        int i =0;
        amtCr=0;
        amtDr=0;
        //first ma debit side ko cash ko total amount;
        //credit side ko cash ko total amount;
        //add garni balance;
        while(i<dataIndex){
            if(node.debit[i].equals(title)){
                amtDr+=node.amount[i];
            }
            if(node.credit[i].equals(title)){
                amtCr+=node.amount[i];
            }
            i++;
        }
        setBalance();
    }

    private void createLabe(String lableTitle){
        Label particular = new Label();
        particular.setMaxWidth(Double.MAX_VALUE);
        particular.setText(lableTitle);

        Label amount = new Label();
        amount.setMaxWidth(Double.MAX_VALUE);

        if(amtDr>amtCr){
            amount.setText(String.valueOf(totalDr)); 
        }else if(amtCr>amtDr){
            amount.setText(String.valueOf(totalCr)); 
        }

        GridPane.setMargin(particular, new Insets(10, 10, 10, 10));
        GridPane.setMargin(amount, new Insets(10, 10, 10, 10));

        gridPane.add(particular,0,rowCounter);

        Region separatorRegion = new Region();
        separatorRegion.setMinHeight(1);   
        separatorRegion.setPrefHeight(1);  
        separatorRegion.setMaxHeight(1);   
        separatorRegion.setStyle("-fx-background-color: #b2c8b7;");  
        GridPane.setColumnSpan(separatorRegion, gridPane.getColumnCount());  

        gridPane.add(separatorRegion, 0, rowCounter );  
        GridPane.setValignment(separatorRegion, VPos.BOTTOM); 

        if(amtDr>amtCr){
            gridPane.add(amount,2,rowCounter);
        }else if(amtCr>amtDr){
            gridPane.add(amount,3,rowCounter); 
        }

        rowCounter++;

    }

    public void check(){
        Boolean cash=false,bank=false,prucahse=false,capital=false,sales=false,pruchaseReturn=false,salesReturn=false,debtors=false,creditors=false,wages=false,rent=false,salary=false;
        initial();
        for(int i = 0;i<dataIndex; i++){
            if((node.credit[i].equals("Bank")||node.debit[i].equals("Bank"))  && bank == false){
                bank=true;
                calculateData("Bank");
                createLabe("Bank");

            }
            if(node.credit[i].equals("Capital") && capital == false){
                capital=true;
                calculateData("Capital");
                createLabe("Capital");

            }
            if((node.credit[i].equals("Cash")||node.debit[i].equals("Cash"))  && cash == false){
                cash=true;
                calculateData("Cash");
                createLabe("Cash");

            }
            if(node.debit[i].equals("Purchase")&& prucahse == false){
                prucahse=true;
                calculateData("Purchase");
                createLabe("Purchase");

            }
            if(node.credit[i].equals("Sales")  && sales == false){
                sales=true;
                calculateData("Sales");
                createLabe("Sales");

            }
            if(node.credit[i].equals("Purchase Return")  && pruchaseReturn == false){
                pruchaseReturn=true;
                calculateData("Purchase Return");
                createLabe("Purchase Return");

            }
            if(node.debit[i].equals("Sales Return")  && salesReturn == false){
                salesReturn=true;
                calculateData("Sales Return");
                createLabe("Sales Return");

            }
            if((node.credit[i].equals("Debtors")||node.debit[i].equals("Debtors"))  && debtors == false) {
                debtors=true;
                calculateData("Debtors");
                createLabe("Debtors");

            }
            if((node.credit[i].equals("Creditor")||node.debit[i].equals("Creditor"))  && creditors == false) {
                creditors=true;
                calculateData("Creditor");
                createLabe("Creditor");

            }
            if(node.debit[i].equals("Wages") && wages == false) {
                wages=true;
                calculateData("Wages");
                createLabe("Wages");

            }
            if(node.debit[i].equals("Rent") && rent == false) {
                rent=true;
                calculateData("Rent");
                createLabe("Rent");
            }
            if(node.debit[i].equals("Salary") && salary == false) {
                salary=true;
                calculateData("Salary");
                createLabe("Salary");
            }

        }
        totalAmountDr.setText(String.valueOf(totalAmntDr));
        totalAmountCr.setText(String.valueOf(totalAmntCr));
    }

    @SuppressWarnings("unused")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDate();
        check();
        node.effects(goBackButton, backButton);
        showDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                check();
            }
        });
    }
}
