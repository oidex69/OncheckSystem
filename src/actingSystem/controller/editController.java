package actingSystem.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import actingSystem.App;
import actingSystem.LinkedNode;
import actingSystem.database.databaseAction;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.value.ChangeListener;
// import javafx.application.Platform;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
import javafx.util.Duration;
import javafx.scene.control.ScrollPane;

public class editController implements Initializable {
    // // private String credit,debit,despc;
    // private double amount;

    LinkedNode link = new LinkedNode(); 
    databaseAction datbaseSave = new databaseAction();
        
    private int rowCounter = 0;
    // private ChangeListener<String> amountListener;
                
    @FXML
    private ImageView backButton, saveButton, addButton;
     
    @FXML
    Button goBackButton,clickSaveButton,delButton;

    @FXML
    ComboBox<String> combox1, combox2,chooseAccount;

    @FXML
    private TextField lastAmountField=null;
            
    @FXML
    GridPane journalGridPane;
                
    @FXML
    DatePicker showDate,showDateEnd;

    @FXML
    Label accountType,account,accountTypeLabel;
    
    @FXML
    AnchorPane journalEntryAnchorPane;

    @FXML
    ScrollPane journalSrc;

    @FXML
    TextField delNum;

    // Flag to see if the amount is filled or not
    boolean amountFlag = false,saveSuccessFlag=false;
    boolean addTransaction = false;
    boolean chooseAccountFlag= false;

                    
    String[] accounts = {"Cash", "Bank", "Capital", "Sales", "Creditor", "Purchase Return","Debtors"};
    String[] credit = {"Sales","Purchase Return","Debtors"};
    String[] debit = {"Sales Return", "Creditor" , "Wages", "Rent","Salary"};

    private int dataIndex;

    public void showDate() {
        showDate.setValue(link.getDate());
        showDateEnd.setValue(link.getDate());
    }

    // Method to navigate back to the main view
    public void goMain(ActionEvent event) {
            try {
                if(LinkedNode.getSaveFlag()==true){
                    datbaseSave.udateDatabase(rowCounter, link);
                    System.out.println("database update successfull");
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

    private void read(){
        initial();
        dataIndex = datbaseSave.countWithDate(showDate.getValue(), showDateEnd.getValue());
        System.out.println("dataIndex="+dataIndex);
        link = datbaseSave.readForEdit(dataIndex, showDate.getValue(),showDateEnd.getValue());
        for(int i=0;i<dataIndex;i++){
            addTransaction();
        }
        journalGridPane.requestLayout();
        journalSrc.requestLayout();
    }

    private void initial(){
        LinkedNode.setSave(false);
        amountFlag = false;
        clickSaveButton.setDisable(true);
        rowCounter=0;
        // gridPane.getChildren().clear(); 
        journalGridPane.getChildren().clear();
        journalGridPane.getRowConstraints().clear(); 
        
    }

    @SuppressWarnings("unused")
    public void addTransaction(){
        System.out.println("rowCounter = "+ rowCounter);
        ComboBox<String> debitBox = new ComboBox<>();
        // debitBox.setDisable(true);
        debitBox.setMaxWidth(Double.MAX_VALUE);
        debitBox.setPrefWidth(250);
        
        ComboBox<String> creditBox = new ComboBox<>();
        creditBox.setMaxWidth(Double.MAX_VALUE);
        creditBox.setPrefHeight(250);
        creditBox.getItems().addAll(accounts);
        

        VBox particularBox = new VBox();
        particularBox.setSpacing(5);
        particularBox.setPadding(new Insets(0, 5, 5, 5));

        HBox debitRow = new HBox(2,new Label("By, "),debitBox);
        HBox creditRow= new HBox(2,new Label("To, "),creditBox);

        particularBox.getChildren().addAll(debitRow,creditRow);

        TextField descp = new TextField();
        descp.setEditable(false);
        

        TextField debitamount = new TextField();
        // debitamount.setEditable(false);
        

        VBox debitamountBox = new VBox();
        debitamountBox.setSpacing(5);
        debitamountBox.setPadding(new Insets(5, 5, 5, 5));
        debitamountBox.getChildren().add(debitamount); // Add amount to debit box
            
        TextField creditamount = new TextField();
        // creditamount.setEditable(false);
        creditamount.setText(String.valueOf(link.getAmount(rowCounter)));
    
        VBox creditamountBox = new VBox();
        creditamountBox.setSpacing(5);
        creditamountBox.setPadding(new Insets(5, 5, 5, 5));
        TextField decoy = new TextField();
        decoy.setVisible(false);
        decoy.setDisable(true);
       

        addEventDynamic(creditamount,debitamount);

        ChangeListener<String> creditListner = (observable, oldValue, credit) ->{
            String currentDebitValue = debitBox.getValue();
            debitBox.getItems().clear();
            System.out.println(currentDebitValue);
    
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
            else if ("Cash".equals(credit)) {
                debitBox.getItems().addAll("Bank", "Purchase", "Sales Return","Creditor","Wages","Rent","Salary");
        
            } else if ("Bank".equals(credit)) {
                debitBox.getItems().addAll("Cash", "Purchase", "Sales Return","Creditor","Wages","Rent","Salary");
        
            } else if ("Debtors".equals(credit)) {
                debitBox.getItems().addAll("Cash", "Bank","Sales Return");
                        
            } else {
                debitBox.getItems().clear();  // Clear items if no match
            }
    
            if(debitBox.getItems().contains(currentDebitValue)){
                System.out.println("it did contained once");
                debitBox.setValue(currentDebitValue);
            }else {
                debitBox.setValue(null);
            }
    
            if (!credit.trim().isEmpty()) {
                clickSaveButton.setDisable(false);  // Enable save button if credit text is changed
                addTransaction=true;
            }
        };

        ChangeListener<String> debitListner = (observable1, oldValue1, debit) -> {
            clickSaveButton.setDisable(false);
            if (creditBox.getValue() != null && !creditBox.getValue().trim().isEmpty()) {
                amountFlag = true;  // Set the flag to true if an account type is selected
            } else {
                amountFlag = false;  // Reset flag if no account type is selected
            }
    
            // Enable the amount field if accountType is selected
            if (debitBox.getValue() != null) {
                debitamount.setEditable(true);
            }
    
            // Enable descp for specific account types
            if ("Debtors".equals(debit) || "Creditor".equals(creditBox.getValue())|| ("Creditor".equals(creditBox.getValue()) && "Purchase".equals(debit)) || "Capital".equals(creditBox.getValue())) {
                descp.setEditable(true);  // Enable descp for specific conditions
                descp.setPromptText("Enter a Name");
            } else {
                descp.setEditable(false);  // Lock descp otherwise
                descp.setPromptText("");
            }
        };

        creditBox.valueProperty().addListener(creditListner);
        debitBox.valueProperty().addListener(debitListner);

        descp.textProperty().addListener((observable2, oldValue2, description)->{
            if (!description.trim().isEmpty()) {
                clickSaveButton.setDisable(false);  // Enable save button if descp text is changed
                addTransaction=true;
            }
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


        creditBox.setValue(link.getCredit(rowCounter));
        debitBox.setValue(link.getDebit(rowCounter));
        descp.setText(link.getdesc(rowCounter));
        debitamount.setText(String.valueOf(link.getAmount(rowCounter)));
        creditamountBox.getChildren().addAll(decoy,creditamount); 
        
        rowCounter++; 
    }
    
    public void deleteRecord(ActionEvent event){
        int deleteNumber = Integer.parseInt(delNum.getText());
        if(deleteNumber>rowCounter){
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // Change to INFORMATION or WARNING as needed
            alert.setTitle("No Record Found");
            alert.setHeaderText("Are you sure the record exists?");
            alert.setContentText("The record is either corrupted or has been deleted and cannot be found. Please try again.");
            
            // Remove the Cancel button
            alert.getButtonTypes().setAll(ButtonType.OK); // Only keep the OK button
        
            // Show the alert and wait for the user's response
            Optional<ButtonType> result = alert.showAndWait();
        
            // Check user's response
            if (result.isPresent() && result.get() == ButtonType.OK) {
                delNum.clear();
                event.consume(); // Consume the event if OK is clicked
            }
            
        }else{
            datbaseSave.deleteRecord(link.getDataCollectionCounter(deleteNumber-1));
            read();
        }
    }

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
            if(!debit.getText().isEmpty() && credit.getText().equals(debit)){
                clickSaveButton.setDisable(false);
            }
        });
   
        credit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clickSaveButton.setDisable(false);  // Enable save button if amount is filled
                addTransaction=true;
            } else {
                clickSaveButton.setDisable(true);  // Disable save button if amount is empty
            }
            if(!credit.getText().isEmpty() && credit.getText().equals(debit)){
                clickSaveButton.setDisable(false);
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

        clickSaveButton.setOnAction(event->{
            if (credit.getText().trim().isEmpty()) {
                emptyAmountBoxAlert(event);
            } else {
                saveFile(); 
            }
        });

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
    
    void effects(){
        link.effects(goBackButton, backButton);
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

    //Save file action
    public void saveFile() {
        double amountCheck=0;
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
                                        link.addDebit(row, value);
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
        datbaseSave.printAll(dataIndex, link);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        showDate();
        effects();
        read();
        saveButton.setOnMouseClicked(event->{
            saveFile();
        });  // Set save button action
        showDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                read();
            }
        });
        showDateEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                read();
            }
        });

        // delButton.setOnMouseClicked(e->deleteRecord());
        delButton.setOnAction(this::deleteRecord);
        
        delNum.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("[0-9]")) {
                event.consume();
            }
            if (delNum.getText().isEmpty() && character.equals("0")) {
                event.consume();
            }
        });
    }   

}
