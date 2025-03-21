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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class incomeExpensesController implements Initializable {

    LinkedNode node = new LinkedNode(); 
    databaseAction action = new databaseAction();

    private int rowCounterInc,rowCounterExp,dataIndex;
    private double amtExpense,amtIncome,totalInc,totalExp,totalAmntInc,totalAmntExp;

    @FXML
    Label totalIncome,totalExpenses;

    @FXML
    GridPane incomeGridPane,expenseGridPane;

    @FXML
    DatePicker showDate,showDateEnd;

    @FXML
    Button goBackButton;

    @FXML
    ImageView backButton;

    private LocalDate getDate() {
        return showDate.getValue();
    }
    private LocalDate getEndDate() {
        return showDateEnd.getValue();
    }

    public void goMain(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/Main3.0.fxml","MainMenu",getDate(),false,true);
        } catch (Exception e) {

        }
    }

    private void setDate(){
        showDate.setValue(node.getDate());
        showDateEnd.setValue(node.getDate());
    }

    

    private void initial(){
        incomeGridPane.getChildren().clear();
        expenseGridPane.getChildren().clear();
        rowCounterInc = 0;                  // Reset rowCounter to start fresh
        rowCounterExp = 0;                  // Reset rowCounter to start fresh
        totalAmntInc = 0;                 // Reset total amount
        totalAmntExp = 0;                 // Reset total amount
        
        dataIndex = action.countWithDate(getDate(),getEndDate());
        node = action.read(dataIndex, getDate(),getEndDate());
    }

    private void setBalance(){
        totalInc=0;
        totalExp=0;
        if(amtIncome>amtExpense){
            
            totalInc = amtIncome-amtExpense;
            totalAmntInc+=totalInc;

        }else if(amtExpense>amtIncome){
            
            totalExp = amtExpense-amtIncome;
            totalAmntExp+=totalExp;
        }
    }
    
    private void calculateData(String title){
        int i =0;
        amtExpense =0;
        amtIncome =0;
        //first ma debit side ko cash ko total amount;
        //credit side ko cash ko total amount;
        //add garni balance;
        while(i<dataIndex){
            if(node.debit[i].equals(title)){
                amtExpense += node.amount[i];
            }
            if(node.credit[i].equals(title)){
                amtIncome += node.amount[i];
            }
            i++;
        }
        setBalance();
    }

    private void createLabelIncome(String lableTitle){
        Label particular = new Label();
        particular.setMaxWidth(Double.MAX_VALUE);
        particular.setText(lableTitle);

        Label amount = new Label();
        amount.setMaxWidth(Double.MAX_VALUE);
        amount.setText(String.valueOf(totalInc));

        GridPane.setMargin(particular, new Insets(10, 10, 10, 10));
        GridPane.setMargin(amount, new Insets(10, 10, 10, 10));

        incomeGridPane.add(particular,0,rowCounterInc);
        incomeGridPane.add(amount,1,rowCounterInc);

        rowCounterInc++;

    }

    private void createLabelExpenses(String lableTitle){
        Label particular = new Label();
        particular.setMaxWidth(Double.MAX_VALUE);
        particular.setText(lableTitle);

        Label amount = new Label();
        amount.setMaxWidth(Double.MAX_VALUE);
        amount.setText(String.valueOf(totalExp));

        GridPane.setMargin(particular, new Insets(10, 10, 10, 10));
        GridPane.setMargin(amount, new Insets(10, 10, 10, 10));

        expenseGridPane.add(particular,0,rowCounterExp);
        expenseGridPane.add(amount,1,rowCounterExp);

        rowCounterExp++;

    }
    
    public void check(){
        Boolean prucahse=false,sales=false,wages=false,rent=false,salary=false;
        initial();
        for(int i = 0;i<dataIndex; i++){
            if(node.debit[i].equals("Purchase")&& prucahse == false){ // Expense
                prucahse=true;
                calculateData("Purchase");
                createLabelExpenses("Purchase");

            }
            if(node.credit[i].equals("Sales")  && sales == false){ // Income
                sales=true;
                calculateData("Sales");
                createLabelIncome("Sales");

            }
            if(node.debit[i].equals("Wages") && wages == false) { // Expenses
                wages=true;
                calculateData("Wages");
                createLabelExpenses("Wages");

            }
            if(node.debit[i].equals("Rent") && rent == false) { // Expenses
                rent=true;
                calculateData("Rent");
                createLabelExpenses("Rent");
            }
            if(node.debit[i].equals("Salary") && salary == false) { // Expenses
                salary=true;
                calculateData("Salary");
                createLabelExpenses("Salary");
            }

        }

        totalIncome.setText(String.valueOf(totalAmntInc));
        totalExpenses.setText(String.valueOf(totalAmntExp));
    }

    @SuppressWarnings("unused")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDate();
        check();
        node.effects(goBackButton,backButton);

        showDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                check();
            }
        });
    }
}
