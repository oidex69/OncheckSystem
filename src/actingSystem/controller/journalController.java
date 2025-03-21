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
import javafx.scene.layout.VBox;

public class journalController implements Initializable {


    @FXML
    GridPane gridPane;

    @FXML
    Label totalAmount;

    @FXML 
    DatePicker showDate,showDateEnd;

    @FXML
    Button goBackButton;

    @FXML
    ImageView backButton;

    LinkedNode node = new LinkedNode();
    databaseAction action = new databaseAction();

    private int rowCounter = 0;
    private double totalamt;

    public void goMain(ActionEvent event) {
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

    public void setDate() {
        showDate.setValue(node.getDate());
        showDateEnd.setValue(node.getDate());
    }
    
    public void showData() {
        gridPane.getChildren().clear();  // Clear previous data entries
        rowCounter = 0;                  // Reset rowCounter to start fresh
        totalamt = 0;                    // Reset total amount
    
        
        int dataIndex = action.countWithDate(getDate(),getEndDate());
        node = action.read(dataIndex, getDate(),getEndDate());
    
        for (int i = 0; i < dataIndex; i++) {
            VBox particularBox = new VBox();
            particularBox.setSpacing(5);
    
            // Create labels for credit and debit
            Label credit = new Label();
            credit.setMaxWidth(Double.MAX_VALUE);
            if(node.credit[i].equals("Creditor")){
                credit.setText("\tTo," +node.descp[i]+"'s" + "\t A/c");
            }else{
                credit.setText("\tTo," + node.credit[i] + "\t A/c");
            }
    
            Label debit = new Label();
            debit.setMaxWidth(Double.MAX_VALUE);
            
            if(node.debit[i].equals("Debtors")){
                debit.setText(node.descp[i]+"'s"+ "\t A/c");
            }else{
                debit.setText(node.debit[i] + "\t A/c");
            }
    
            Label descp = new Label();
            descp.setMaxWidth(Double.MAX_VALUE);
            descp.setText(node.descp[i]);
    
            // Create an amount label
            Label amount = new Label();
            amount.setMaxWidth(Double.MAX_VALUE);
            amount.setText(String.valueOf(node.amount[i]));
            totalamt += node.amount[i];
    
            // Add debit and credit labels to the particular box
            particularBox.getChildren().add(debit);
            particularBox.getChildren().add(credit);
    
            // Create and set debit amount box
            VBox debitamountBox = new VBox();
            debitamountBox.setSpacing(5);
            Label debitAmountLabel = new Label(amount.getText());
            debitamountBox.getChildren().add(debitAmountLabel); // Add amount to debit box
    
            // Create and set credit amount box
            VBox creditamountBox = new VBox();
            creditamountBox.setSpacing(5);
            creditamountBox.getChildren().add(new Label(""));
            Label creditAmountLabel = new Label(amount.getText());
            debitamountBox.getChildren().add(creditAmountLabel);
            creditamountBox.getChildren().add(creditAmountLabel); // Add same amount to credit box
    
            // Set margins for the boxes
            GridPane.setMargin(particularBox, new Insets(10, 10, 20, 10));
            GridPane.setMargin(debitamountBox, new Insets(10, 10, 20, 10));
            GridPane.setMargin(creditamountBox, new Insets(10, 10, 20, 10));
    
            // Add boxes to the grid pane
            gridPane.add(particularBox, 0, rowCounter);
            gridPane.add(debitamountBox, 2, rowCounter);
            gridPane.add(creditamountBox, 3, rowCounter);

            Region separatorRegion = new Region();
            separatorRegion.setMinHeight(1);   
            separatorRegion.setPrefHeight(1);  
            separatorRegion.setMaxHeight(1);   
            separatorRegion.setStyle("-fx-background-color: #b2c8b7;");  
            GridPane.setColumnSpan(separatorRegion, gridPane.getColumnCount());  

            gridPane.add(separatorRegion, 0, rowCounter );  
            GridPane.setValignment(separatorRegion, VPos.BOTTOM); 
    
            rowCounter++;
        }
        totalAmount.setText(String.valueOf(totalamt));
    }
    
    @SuppressWarnings("unused")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDate();
        showData();
        node.effects(goBackButton, backButton);
        showDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                showData();
            }
        });
        showDateEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                showData();
            }
        });
    }
}
