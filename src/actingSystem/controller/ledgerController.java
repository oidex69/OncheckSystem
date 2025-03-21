package actingSystem.controller;

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

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ledgerController implements Initializable {

    @FXML
    Label titleLabel;

    String title;

    private int rowCounterCr = 0,rowCounterDr = 0, dataIndex;
    private double totalAmtCr = 0,totalAmtDr = 0;
    
    @FXML
    Label totalAmountCr,totalAmountDr;


    @FXML 
    GridPane drGridPane,crGridPane;

    @FXML
    DatePicker showDate,showDateEnd;

    @FXML
    Button goBackButton;

    @FXML 
    ImageView backButton;


    private databaseAction action = new databaseAction();
    private LinkedNode node = new LinkedNode();



    private LocalDate getDate() {
        return showDate.getValue();
    }

    private LocalDate getEndDate() {
        return showDateEnd.getValue();
    }
    
    public void goMain(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/LedgerMenu.fxml","LedgerMenu",getDate(),false,false);
        } catch (Exception e) {

        }
    }

    private void setDate(LocalDate date){
        showDate.setValue(date);
        showDateEnd.setValue(date);
    }

    public void setTitle(String title,LocalDate date){
        this.title = title;
        titleLabel.setText(title);
        setDate(date);
        chooseAccount();
    }

    private void initial(){
        drGridPane.getChildren().clear();  // Clear previous data entries
        crGridPane.getChildren().clear();  // Clear previous data entries
        rowCounterCr = 0;                  // Reset rowCounter to start fresh
        rowCounterDr = 0;                  // Reset rowCounter to start fresh
        totalAmtDr = 0;                    // Reset total amount
        totalAmtCr = 0;                    // Reset total amount
        
        dataIndex = action.countWithDate(getDate(), getEndDate());
        node = action.read(dataIndex, getDate(), getEndDate());
    }

    private void addBalance(){
        if(totalAmtCr>totalAmtDr){
            double total;
            total = totalAmtCr-totalAmtDr;

            Label balancLabel = new Label();
            balancLabel.setMaxWidth(Double.MAX_VALUE);
            balancLabel.setText("Balance c/d");

            Label balanceAmount = new Label();
            balanceAmount.setMaxWidth(Double.MAX_VALUE);
            balanceAmount.setText(String.valueOf(total));

            GridPane.setMargin(balancLabel, new Insets(10, 10, 10, 10));
            GridPane.setMargin(balanceAmount, new Insets(10, 10, 10, 10));

            drGridPane.add(balancLabel,0,rowCounterDr);
            drGridPane.add(balanceAmount,2,rowCounterDr);

            rowCounterDr++;

        }else if(totalAmtCr<totalAmtDr){
            double total;
            total = totalAmtDr-totalAmtCr;

            Label balancLabel = new Label();
            balancLabel.setStyle("-fx-font-size: 16px;");
            balancLabel.setMaxWidth(Double.MAX_VALUE);
            balancLabel.setText("Balance b/d");

            Label balanceAmount = new Label();
            balanceAmount.setStyle("-fx-font-size: 16px;");
            balanceAmount.setMaxWidth(Double.MAX_VALUE);
            balanceAmount.setText(String.valueOf(total));

            GridPane.setMargin(balancLabel, new Insets(10, 10, 10, 10));
            GridPane.setMargin(balanceAmount, new Insets(10, 10, 10, 10));

            crGridPane.add(balancLabel,0,rowCounterCr);
            crGridPane.add(balanceAmount,2,rowCounterCr);

            rowCounterCr++;
        }
    }

    private void showData(String titleCompare){
        initial();
        for(int i =0;i<dataIndex;i++){
            if(node.debit[i].equals(titleCompare)){
                Label credit = new Label();
                credit.setStyle("-fx-font-size: 16px;");
                credit.setMaxWidth(Double.MAX_VALUE);

                if(node.credit[i].equals("Creditor")){
                    credit.setText("To, "+node.descp[i]);
                }else{
                    credit.setText("To, "+node.credit[i]);
                }

                Label amount = new Label();
                amount.setStyle("-fx-font-size:16;");
                amount.setMaxWidth(Double.MAX_VALUE);
                amount.setText(String.valueOf(node.amount[i]));

                totalAmtDr += node.amount[i];

                GridPane.setMargin(credit, new Insets(10, 10, 10, 10));
                GridPane.setMargin(amount, new Insets(10, 10, 10, 10));

                drGridPane.add(credit, 0, rowCounterDr);
                drGridPane.add(amount, 2, rowCounterDr);

                rowCounterDr++;
            }
            if(node.credit[i].equals(titleCompare)){
                Label debit = new Label();
                debit.setStyle("-fx-font-size: 16px;");
                debit.setMaxWidth(Double.MAX_VALUE);

                if(node.debit[i].equals("Debtors")){
                    debit.setText("By, "+node.descp[i]);
                }else{
                    debit.setText("By, "+node.debit[i]);
                }

                Label amount = new Label();
                amount.setStyle("-fx-font-size:16;");
                amount.setMaxWidth(Double.MAX_VALUE);
                amount.setText(String.valueOf(node.amount[i]));

                totalAmtCr += node.amount[i];

                GridPane.setMargin(debit, new Insets(10, 10, 10, 10));
                GridPane.setMargin(amount, new Insets(10, 10, 10, 10));

                crGridPane.add(debit, 0, rowCounterCr);
                crGridPane.add(amount, 2, rowCounterCr);

                rowCounterCr++;
            }
        }

            addBalance();

            if(totalAmtCr>totalAmtDr){
                totalAmtDr = totalAmtDr+(totalAmtCr-totalAmtDr);
            }else{
                totalAmtCr = totalAmtCr+(totalAmtDr-totalAmtCr);
            }

            totalAmountCr.setText(String.valueOf(totalAmtCr));
            totalAmountDr.setText(String.valueOf(totalAmtDr));
    }

    private void chooseAccount(){
        if (titleLabel.getText().equals("Capital Account")) {
            showData("Capital");

        }else if(titleLabel.getText().equals("Purchase Account")){
            showData("Purchase");

        }else if(titleLabel.getText().equals("Cash Account")){
            showData("Cash");

        }else if(titleLabel.getText().equals("Bank Account")){
            showData("Bank");

        }else if(titleLabel.getText().equals("Sale's Account")){
            showData("Sales");

        }else if(titleLabel.getText().equals("Purchase Return Account")){
            showData("Purchase Return");

        }else if(titleLabel.getText().equals("Sale's Return Account")){
            showData("Sales Return");

        }else if(titleLabel.getText().equals("Debtor's Account")){
            showData("Debtors");

        }else if(titleLabel.getText().equals("Creditor's Account")){
            showData("Creditor");

        }else if(titleLabel.getText().equals("Wage Account")){
            showData("Wages");

        }else if(titleLabel.getText().equals("Rent Account")){
            showData("Rent");

        }else if(titleLabel.getText().equals("Salary Account")){
            showData("Salary");

        }

    }

    @SuppressWarnings("unused")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        node.effects(goBackButton, backButton);
        showDate.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null && !newValue.equals(oldValue)) {
            chooseAccount();
        }
        });
        showDateEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                chooseAccount();
            }
        });
    }
}
