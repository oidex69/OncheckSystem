package actingSystem.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import actingSystem.App;
import actingSystem.LinkedNode;
import actingSystem.database.databaseAction;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class transactionController implements Initializable {
    // // private String credit,debit,despc;
    // private double amount;

    LinkedNode link = new LinkedNode(); 
    databaseAction datbaseSave = new databaseAction();
        
    private int rowCounter = 0;
    // private ChangeListener<String> amountListener;
                
    @FXML
    private ImageView backButton, saveButton, addButton;
     
    @FXML
    Button addTransactionButton,goBackButton,clickSaveButton;

    @FXML
    ComboBox<String> combox1, combox2,chooseAccount;

    @FXML
    private TextField lastAmountField=null;
            
    @FXML
    GridPane gridPane,journalGridPane;
                
    @FXML
    Label showDate;

    @FXML
    Label accountType,account,accountTypeLabel;
    
    @FXML
    AnchorPane paymentAnchorPane,journalEntryAnchorPane;

    // Flag to see if the amount is filled or not
    boolean amountFlag = false,saveSuccessFlag=false;
    boolean addTransaction = false;
    boolean chooseAccountFlag= false;

                    
    String[] accounts = {"Cash", "Bank", "Capital", "Sales", "Creditor", "Purchase Return","Debtors"};
    String[] credit = {"Sales","Purchase Return","Debtors"};
    String[] debit = {"Sales Return", "Creditor" , "Wages", "Rent","Salary"};

    public void showDate() {
        showDate.setText(link.getDateString());
        
    }
    
    public void chooseAccountType(){
        if(accountType.getText().equals("Payment")){
            paymentAnchorPane.setVisible(true);
            journalEntryAnchorPane.setVisible(false);
            chooseAccount.getItems().addAll("Cash","Bank");
            addPaymentTransaction();
        }else if(accountType.getText().equals("Journal Entry")){
            journalEntryAnchorPane.setVisible(true);
            paymentAnchorPane.setVisible(false);
            addJournalEntry();
        }else if(accountType.getText().equals("Receipt")){
            paymentAnchorPane.setVisible(true);
            journalEntryAnchorPane.setVisible(false);
            chooseAccount.getItems().addAll("Cash","Bank");
            addRecepit();
        }
    }

    private void addJournalEntry(){
        // journalEntry.setVisible(true);
        account.setVisible(false);
        chooseAccount.setVisible(false);
        accountType.setLayoutY(12);
        accountTypeLabel.setLayoutY(12);
        addTransaction();
    }

    // Method to navigate back to the main view
    public void goMain(ActionEvent event) {
            try {
                if(LinkedNode.getSaveFlag()==true){
                    datbaseSave.save(rowCounter, link);
                    App.changeScene("/actingSystem/fxml/Main3.0.fxml","MainMenu",null,true,false);
                }else{
                    // Show a confirmation dialog if the saveFlag is false
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Save Not Confirmed");
                    alert.setHeaderText("Are you sure you want to exit?");
                    alert.setContentText("Do you want to exit before saving.");
                        
                    // Show the alert and wait for the user's response
                    Optional<ButtonType> result = alert.showAndWait();
                        
                    // Check user's response
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // If user clicked "OK", perform the save
                        App.changeScene("/actingSystem/fxml/Main3.0.fxml","MainMenu",null,true,false);
                    } else {
                        // If user clicked "Cancel", do nothing (or you can log/cancel the action)
                        event.consume();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
                    
    public void handleExitWithSaveConfirmation(javafx.event.Event event) {
        if(LinkedNode.getSaveFlag()==true){
                datbaseSave.save(rowCounter, link);
                Platform.exit();
        }else{
            // Check if the user wants to save changes before exiting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes!");
            alert.setContentText("Do you want to exit before saving your changes?");
    
            // Show the confirmation dialog
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Platform.exit();  // Exit the application
                } else {
                    event.consume();  // Save the data
                }
            });
        }
    }

    private void initial(){
        LinkedNode.setSave(false);
        addTransactionButton.setDisable(true);
        amountFlag = false;
        clickSaveButton.setDisable(true);
    }

    @SuppressWarnings("unused")
    public void addTransaction() {
        initial();

        ComboBox<String> debitBox = new ComboBox<>();
        debitBox.setDisable(true);
        debitBox.setMaxWidth(Double.MAX_VALUE);
        debitBox.setPrefWidth(250);

        ComboBox<String> creditBox = new ComboBox<>();

        creditBox.setMaxWidth(Double.MAX_VALUE);
        creditBox.setPrefWidth(250);
        creditBox.getItems().addAll(accounts);

        VBox particularBox = new VBox();
        particularBox.setSpacing(5);
        // particularBox.setPadding(new Insets(0, 5, 5, 5)); 
        
        HBox debitRow = new HBox(2,new Label("By, "),debitBox);
        HBox creditRow= new HBox(2,new Label("To, "),creditBox);
        
        particularBox.getChildren().addAll(debitRow,creditRow);

        TextField descp = new TextField();
        descp.setEditable(false);

        TextField debitamount = new TextField();
        debitamount.setEditable(false);

        VBox debitamountBox = new VBox();
        debitamountBox.setSpacing(5);
        debitamountBox.setPadding(new Insets(5, 5, 5, 5));
        debitamountBox.getChildren().add(debitamount); // Add amount to debit box
        
        TextField creditamount = new TextField();
        creditamount.setEditable(false);

        VBox creditamountBox = new VBox();
        creditamountBox.setSpacing(5);
        creditamountBox.setPadding(new Insets(5, 5, 5, 5));
        TextField decoy = new TextField();
        decoy.setVisible(false);
        decoy.setDisable(true);
        creditamountBox.getChildren().addAll(decoy,creditamount); 
        
        addEventDynamic(creditamount,debitamount);
        
        creditBox.valueProperty().addListener((observable, oldValue, credit) -> {
            
            debitBox.setDisable(false);
            debitBox.getItems().clear();  // Clear previous accountType items

            descp.setEditable(false);  // Reset descp
            descp.clear();  // Clear descp

            debitamount.setEditable(false);  // Reset amount
            debitamount.clear();
            creditamount.setEditable(false);  // Reset amount
            creditamount.clear();

            // Set the accountType options based on particulaBox selection
            if ("Capital".equals(credit)) {
                debitBox.getItems().addAll("Cash", "Bank");

            } else if ("Sales".equals(credit)) {
                debitBox.getItems().addAll("Cash", "Bank", "Debtors");

            } else if ("Creditor".equals(credit)) {
                debitBox.getItems().addAll("Purchase","Wages","Rent","Salary");
                
            } else if ("Purchase Return".equals(credit)) {
                debitBox.getItems().addAll("Cash", "Bank","Creditor");

            }
            //  else if("Sales Return".equals(credit)){
            //     debitBox.getItems().addAll("Cash", "Bank","Debtors");

            // }
            else if ("Cash".equals(credit)) {
                debitBox.getItems().addAll("Bank", "Purchase", "Sales Return","Creditor","Wages","Rent","Salary");

            } else if ("Bank".equals(credit)) {
                debitBox.getItems().addAll("Cash", "Purchase", "Sales Return","Creditor","Wages","Rent","Salary");

            } else if ("Debtors".equals(credit)) {
                debitBox.getItems().addAll("Cash", "Bank","Sales Return");
                
            } else {
                debitBox.getItems().clear();  // Clear items if no match
            }

            // Listener for accountType changes
            debitBox.valueProperty().addListener((observable1, oldValue1, debit) -> {
                if (credit != null && !credit.trim().isEmpty()) {
                    amountFlag = true;  // Set the flag to true if an account type is selected
                } else {
                    amountFlag = false;  // Reset flag if no account type is selected
                }

                // Enable the amount field if accountType is selected
                if (debitBox.getValue() != null) {
                    debitamount.setEditable(true);
                }

                // Enable descp for specific account types
                if ("Debtors".equals(debit) || "Creditor".equals(credit)|| ("Creditor".equals(credit) && "Purchase".equals(debit)) || "Capital".equals(credit) || ("Debtors".equals(credit) && "Purchase".equals(debit))) {
                    descp.setEditable(true);  // Enable descp for specific conditions
                    descp.setPromptText("Enter a Name");
                } else {
                    descp.setEditable(false);  // Lock descp otherwise
                    descp.setPromptText("");
                }
            });
        });

        descp.addEventFilter(KeyEvent.KEY_TYPED, event->{
            String character = event.getCharacter();
            if(!character.matches("[a-zA-Z ]")){
                event.consume();
            }
        });

        RowConstraints newRowConstraints = new RowConstraints();
        newRowConstraints.setMinHeight(65);
        newRowConstraints.setPrefHeight(65);
        newRowConstraints.setVgrow(Priority.NEVER);

        journalGridPane.add(particularBox, 0, rowCounter);
        journalGridPane.add(descp, 1, rowCounter);
        journalGridPane.add(debitamountBox, 2, rowCounter);
        journalGridPane.add(creditamountBox, 3, rowCounter);
        journalGridPane.getRowConstraints().add(newRowConstraints);

        Region separatorRegion = new Region();
            separatorRegion.setMinHeight(1);   
            separatorRegion.setPrefHeight(1);  
            separatorRegion.setMaxHeight(1);   
            separatorRegion.setStyle("-fx-background-color: #b2c8b7;");  
            GridPane.setColumnSpan(separatorRegion, journalGridPane.getColumnCount());  

            journalGridPane.add(separatorRegion, 0, rowCounter );  
            GridPane.setValignment(separatorRegion, VPos.BOTTOM); 

        rowCounter++;

    }
    
    @SuppressWarnings("unused")
    private void addEventDynamic(TextField amount){
        amount.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("\\d") && !character.equals(".")) {
                event.consume();  // Consume event if it's not a digit or a dot
            }
            // Only allow one dot
            if (character.equals(".") && amount.getText().contains(".")) {
                event.consume();  // Consume event if dot already exists
            }
            

        });

        amount.addEventFilter(KeyEvent.KEY_TYPED, event->{
            String character = event.getCharacter();
            if(!character.matches("[0-9.]")){
                event.consume();
            }
            if (character.equals(".") && amount.getText().contains(".")) {
                event.consume();  // Consume event if dot already exists
            }
        });

        
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clickSaveButton.setDisable(false);  // Enable save button if amount is filled
                addTransactionButton.setDisable(false);  // Enable add transaction button
                addTransaction=true;
            } else {
                clickSaveButton.setDisable(true);  // Disable save button if amount is empty
                addTransactionButton.setDisable(true);  // Disable add transaction button
                addTransaction=false;
            }
        });

        // Handling "Enter" key separately in a KEY_PRESSED event
        amount.setOnKeyPressed(event -> {
            if (amountFlag && event.getCode().toString().equals("ENTER") && addTransaction==true) {
                if(amount.getText().trim().isEmpty() || amount.isDisable()){
                    emptyAmountBoxAlert(event);
                }else{
                    addTransactionButton.fire();
                }
            }
        });

        addTransactionButton.setOnAction(event -> {
            // Check if the 'amount' TextField is empty
            if (amount.getText().trim().isEmpty()) {

                emptyAmountBoxAlert(event);  

            } else {

                if(accountType.getText().equals("Payment")){
                    addPaymentTransaction();  
                    
                }else if(accountType.getText().equals("Receipt")){
                    addRecepit();  
                    
                }else if(accountType.getText().equals("Journal Entry")){
                    addJournalEntry();  
                }
            }
        });

        clickSaveButton.setOnAction(event->{
            if (amount.getText().trim().isEmpty()) {
                emptyAmountBoxAlert(event);
            } else {
                savePayment(); 
            }
        });

    }

    @SuppressWarnings("unused")
    private void addEventDynamic(TextField credit,TextField debit){

        credit.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("\\d") && !character.equals(".")) {
                event.consume();  // Consume event if it's not a digit or a dot
            }
            // Only allow one dot
            if (character.equals(".") && credit.getText().contains(".")) {
                event.consume();  // Consume event if dot already exists
            }
        });

        debit.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("\\d") && !character.equals(".")) {
                event.consume();  // Consume event if it's not a digit or a dot
            }
            // Only allow one dot
            if (character.equals(".") && debit.getText().contains(".")) {
                event.consume();  // Consume event if dot already exists
            }
        });
   
        credit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clickSaveButton.setDisable(false);  // Enable save button if amount is filled
                addTransactionButton.setDisable(false);  // Enable add transaction button
                addTransaction=true;
            } else {
                clickSaveButton.setDisable(true);  // Disable save button if amount is empty
                addTransactionButton.setDisable(true);  // Disable add transaction button
                addTransaction=false;
            }
        });

        // Handling "Enter" key separately in a KEY_PRESSED event
        credit.setOnKeyPressed(event -> {
            if (amountFlag && event.getCode().toString().equals("ENTER") && addTransaction==true) {
                if(credit.getText().trim().isEmpty() || credit.isDisable()){
                    emptyAmountBoxAlert(event);
                }else{
                    addTransactionButton.fire();
                }
            }
        });

        debit.setOnKeyReleased(event -> {
            if (debit.getText().isEmpty()) {
                credit.setEditable(false);
            }
            if(!debit.getText().isEmpty()){
                credit.setEditable(true);
            }
        });
        
        addTransactionButton.setOnAction(event -> {
            // Check if the 'amount' TextField is empty
            if (credit.getText().trim().isEmpty()) {
                emptyAmountBoxAlert(event);  

            } else {

                if(accountType.getText().equals("Payment")){
                    addPaymentTransaction();  
                    
                }else if(accountType.getText().equals("Receipt")){
                    addRecepit();  
                    
                }else if(accountType.getText().equals("Journal Entry")){
                    addJournalEntry();  
                }
            }
        });

        clickSaveButton.setOnAction(event->{
            if (credit.getText().trim().isEmpty()) {
                emptyAmountBoxAlert(event);
            } else {
                saveFile(); 
            }
        });

    }

    @SuppressWarnings("unused")
    private void  addPaymentTransaction() {
        initial();
        ComboBox<String> debitBox = new ComboBox<>();
        debitBox.setDisable(true);
        // debitBox.setMaxWidth(Double.MAX_VALUE);
        debitBox.setPrefWidth(250);
        debitBox.getItems().addAll(debit);

        TextField descp = new TextField();
        descp.setEditable(false);

        TextField amount = new TextField();
        amount.setEditable(false);

        // Set margins
        GridPane.setMargin(debitBox, new Insets(10, 10, 10, 10));
        GridPane.setMargin(descp, new Insets(10, 10, 10, 10));
        GridPane.setMargin(amount, new Insets(10, 10, 10, 10));

        addEventDynamic(amount);

        gridPane.add(debitBox, 0, rowCounter);
        gridPane.add(amount, 1, rowCounter);

        chooseAccount.valueProperty().addListener((observable,oldValue,value)->{
            LinkedNode.setSave(false);
        });

        if(chooseAccountFlag==false){
            chooseAccount.setOnAction(event->{
                if(chooseAccount.getSelectionModel().getSelectedItem() != null){
                    debitBox.setDisable(false);
                    chooseAccountFlag=true;
                }else{
                    debitBox.setDisable(true);
                    chooseAccountFlag=true;
                }
            });
        }else{
            debitBox.setDisable(false);
        }

        debitBox.valueProperty().addListener((observable, oldValue, credit) -> {
            amount.setEditable(true);
            amountFlag = true;
        });
        
        rowCounter++;

    }

    @SuppressWarnings("unused")
    private void addRecepit(){
        initial();
        //This is debit which means that we will receive it.
        ComboBox<String> creditBox = new ComboBox<>();
        creditBox.setDisable(true);
        // creditBox.setMaxWidth(Double.MAX_VALUE);
        creditBox.setPrefWidth(250);
        creditBox.getItems().addAll(credit);

        TextField descp = new TextField();
        descp.setEditable(false);

        TextField amount = new TextField();
        amount.setEditable(false);

        // Set margins
        GridPane.setMargin(creditBox, new Insets(10, 10, 10, 10));
        GridPane.setMargin(descp, new Insets(10, 10, 10, 10));
        GridPane.setMargin(amount, new Insets(10, 10, 10, 10));

        addEventDynamic(amount);
        gridPane.add(creditBox, 0, rowCounter);
        gridPane.add(amount, 1, rowCounter);

        chooseAccount.valueProperty().addListener((observable,oldValue,value)->{
            LinkedNode.setSave(false);
        });

        if(chooseAccountFlag==false){
            chooseAccount.setOnAction(event->{
                if(chooseAccount.getSelectionModel().getSelectedItem() != null){
                    creditBox.setDisable(false);
                    chooseAccountFlag=true;
                }else{
                    creditBox.setDisable(true);
                    chooseAccountFlag=true;
                }
            });
        }else{
            creditBox.setDisable(false);
        }

        creditBox.valueProperty().addListener((observable, oldValue, credit) -> {
            amount.setEditable(true);
            amountFlag = true;
        });
        
        rowCounter++;

    }

    private void emptyAmountBoxAlert(ActionEvent event) {
                // Create an information alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Amount Box Empty");
                alert.setHeaderText("Please fill all the amount box before continuing.");
                
                // Show the confirmation dialog
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        event.consume();;  // Exit the application
                    } else {
                        event.consume();  // Consume the event to prevent the default exit behavior
            }
        });
    }

    private void emptyAmountBoxAlert(KeyEvent event) {
        // Create an information alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Amount Box Empty");
        alert.setHeaderText("Please fill all the amount box before continuing.");
        
        // Show the confirmation dialog
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                event.consume();;  // Exit the application
            } else {
                event.consume();  // Consume the event to prevent the default exit behavior
            }
        });
    }   

    public void savePayment(){
        link.initialize(rowCounter);
        LinkedNode.setSave(true);
        if (!amountFlag) {
            clickSaveButton.setDisable(true);
        } else {
            clickSaveButton.setDisable(false);
        }

        for(Node node:gridPane.getChildren()){
            Integer row = GridPane.getRowIndex(node);
            Integer column = GridPane.getColumnIndex(node);

            if(node instanceof ComboBox<?>) {
                ComboBox<?> comboBox = (ComboBox<?>) node;
                if(column == 0){
                    String date = showDate.getText();
                    link.addDate(row,date);

                    String value =  String.valueOf(comboBox.getValue());

                    if(accountType.getText().equals("Payment")){
                        link.addDebit(row, value);
            
                        link.addCredit(row, String.valueOf(chooseAccount.getValue()));
                        System.out.println(String.valueOf(chooseAccount.getValue()));
                    }

                    if(accountType.getText().equals("Receipt")){
                        link.addCredit(row, value);
            
                        link.addDebit(row, chooseAccount.getValue());
                    }
                }
                
            }
            if(node instanceof TextField) {
                TextField textField = (TextField) node;
                String value = textField.getText();
                if(column==1){
                    double amount = Double.parseDouble(value);
                    link.addamount(row, amount);
                    link.adddesc(row, "");
                }
            }
        }
        clickSaveButton.setDisable(true);    
    }
    
    // Save file action
    public void saveFile() {
        double amountCheck=0;
        link.initialize(rowCounter);
        LinkedNode.setSave(true);
        
        if (!amountFlag) {
            clickSaveButton.setDisable(true);
        } else {
            clickSaveButton.setDisable(false);
        }
        //Creates a instance of the LinkeNode class
        // Iterate over gridPane children and print values
        for (Node node : journalGridPane.getChildren()) {
            
            Integer row = GridPane.getRowIndex(node);
            Integer column = GridPane.getColumnIndex(node);

            int vboxRowCounter = 0;
            if(node instanceof VBox) {
                VBox vbox = (VBox) node;
                if(column == 0) {       
                    for(Node child :vbox.getChildren()){
                        vboxRowCounter++;
                        if (vboxRowCounter == 1) {
                            // This is Row 1 of the VBox
                            if (child instanceof HBox) {
                                HBox hbox = (HBox) child;
                                for (Node hboxChild : hbox.getChildren()) {
                                    // Process components inside Row 1
                                    if (hboxChild instanceof ComboBox<?>) {
                                        ComboBox<?> comboBox = (ComboBox<?>) hboxChild;
                                        String value = (String) comboBox.getValue();
                                        String date = showDate.getText();
                                        link.addDebit(row, value);
                                        link.addDate(row, date);
                                    }
                                }
                            }
                        } else if (vboxRowCounter == 2) {
                            // This is Row 2 of the VBox
                            if (child instanceof HBox) {
                                HBox hbox = (HBox) child;
                                for (Node hboxChild : hbox.getChildren()) {
                                    // Process components inside Row 2
                                    if (hboxChild instanceof ComboBox<?>) {
                                        ComboBox<?> comboBox = (ComboBox<?>) hboxChild;
                                        String value = (String) comboBox.getValue();
                                        link.addCredit(row, value);
                                    }
                                }
                            }
                        }
                    }
                }else if(column == 2) {
                    for(Node child: vbox.getChildren()){
                        if (child instanceof TextField) {
                            TextField textField = (TextField) child;
                            amountCheck = Double.parseDouble(textField.getText());
                        }
                        
                    }
                }else if(column == 3) {
                    for (Node child : vbox.getChildren()) {
                        vboxRowCounter++;
                        if(vboxRowCounter==2){
                            if (child instanceof TextField) {
                                TextField textField = (TextField) child;
                                if(amountCheck == Double.parseDouble(textField.getText())) {
                                    link.addamount(row, amountCheck);
                                    saveSuccessFlag=true;
                                }else{
                                    // Create an information alert
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Saved Failed!!");
                                    alert.setHeaderText("Amount Box DoesNot Match Please Fix it.");
                                    alert.setContentText("Amount box of row: "+ (row+1) + " doesnot match.");
                                    
                                    // Show the confirmation dialog
                                    alert.showAndWait().ifPresent(response -> {
                                        if (response == ButtonType.OK) {
                                            return;
                                        } else {
                                            return;
                                        }
                                    });
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                String value = textField.getText();
                if(value == null){
                    value = "";
                    link.adddesc(row, value);
                }else {
                    link.adddesc(row, value);
                    
                }
                
            }

        }
        if(!saveSuccessFlag){
            clickSaveButton.setDisable(false);
        }else if(saveSuccessFlag){
            clickSaveButton.setDisable(true);
        }
    }

    @SuppressWarnings("unused")
    void effects(){
        link.effects(goBackButton, backButton);

        addTransactionButton.setOnMouseEntered(event -> {
            // Fade out the current image
            FadeTransition fadeOut = new FadeTransition(Duration.millis(50), addButton);
            fadeOut.setFromValue(1.0); // Fully visible
            fadeOut.setToValue(0.0);   // Fully invisible
            fadeOut.setInterpolator(Interpolator.EASE_BOTH);

            // After fade-out, change the image and fade it in
            fadeOut.setOnFinished(e -> {
                addButton.setImage(new Image("/actingSystem/css/addHover.png")); // Change to hover image

                FadeTransition fadeIn = new FadeTransition(Duration.millis(50), addButton);
                fadeIn.setFromValue(0.0); // Start invisible
                fadeIn.setToValue(1.0);   // Fully visible
                fadeIn.setInterpolator(Interpolator.EASE_BOTH);
                fadeIn.play();
            });

            fadeOut.play(); // Start fade-out
        });

        addTransactionButton.setOnMouseExited(event -> {
            // Fade out the current image
            FadeTransition fadeOut = new FadeTransition(Duration.millis(50), addButton);
            fadeOut.setFromValue(1.0); // Fully visible
            fadeOut.setToValue(0.0);   // Fully invisible
            fadeOut.setInterpolator(Interpolator.EASE_BOTH);

            // After fade-out, revert to the default image and fade it in
            fadeOut.setOnFinished(e -> {
                addButton.setImage(new Image("/actingSystem/css/add.png")); // Revert to default image

                FadeTransition fadeIn = new FadeTransition(Duration.millis(50), addButton);
                fadeIn.setFromValue(0.0); // Start invisible
                fadeIn.setToValue(1.0);   // Fully visible
                fadeIn.setInterpolator(Interpolator.EASE_BOTH);
                fadeIn.play();
            });

            fadeOut.play(); // Start fade-out
        });

        // Add effects for clickSaveButton
        clickSaveButton.setOnMouseEntered(event -> {
            // Fade out the current image
            FadeTransition fadeOut = new FadeTransition(Duration.millis(50), saveButton);
            fadeOut.setFromValue(1.0); // Fully visible
            fadeOut.setToValue(0.0);   // Fully invisible
            fadeOut.setInterpolator(Interpolator.EASE_BOTH);

            // After fade-out, change the image and fade it in
            fadeOut.setOnFinished(e -> {
                saveButton.setImage(new Image("/actingSystem/css/saveHover.png")); // Change to hover image

                FadeTransition fadeIn = new FadeTransition(Duration.millis(50), saveButton);
                fadeIn.setFromValue(0.0); // Start invisible
                fadeIn.setToValue(1.0);   // Fully visible
                fadeIn.setInterpolator(Interpolator.EASE_BOTH);
                fadeIn.play();
            });

            fadeOut.play(); // Start fade-out
        });

        clickSaveButton.setOnMouseExited(event -> {
            // Fade out the current image
            FadeTransition fadeOut = new FadeTransition(Duration.millis(50), saveButton);
            fadeOut.setFromValue(1.0); // Fully visible
            fadeOut.setToValue(0.0);   // Fully invisible
            fadeOut.setInterpolator(Interpolator.EASE_BOTH);

            // After fade-out, revert to the default image and fade it in
            fadeOut.setOnFinished(e -> {
                saveButton.setImage(new Image("/actingSystem/css/save.png")); // Revert to default image

                FadeTransition fadeIn = new FadeTransition(Duration.millis(50), saveButton);
                fadeIn.setFromValue(0.0); // Start invisible
                fadeIn.setToValue(1.0);   // Fully visible
                fadeIn.setInterpolator(Interpolator.EASE_BOTH);
                fadeIn.play();
            });

            fadeOut.play(); // Start fade-out
        });

    }
    
//     @SuppressWarnings("unused")
//     @Override
    public void setTitle(String accountTypeString){
        accountType.setText(accountTypeString);
        chooseAccountType();
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        showDate();
        effects();
        // saveButton.setOnMouseClicked(event->{
        //     saveFile();
        // });  // Set save button action
        
    }   
}
