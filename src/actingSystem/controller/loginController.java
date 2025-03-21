package actingSystem.controller;

import java.net.URL;
import java.util.ResourceBundle;

import actingSystem.App;
import actingSystem.LinkedNode;
import actingSystem.database.databaseAction;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class loginController implements Initializable {

    @FXML
    Label datePicker, errorMessage, errorMessage1, createAccountLabel, logIn;

    @FXML
    TextField createUsrname;


    @FXML
    VBox login, createAccount;

    @FXML
    ComboBox<String> accountType,loginaccountType;

    @FXML
    TextField userNameField,acctName;

    @FXML
    PasswordField passwordField,createPassword, checkPassword;  // Change to PasswordField for password input

    @FXML
    Button loginButton, createButton;

    LinkedNode link;
    databaseAction action;

    public void goMain(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/Main3.0.fxml","MainMenu",null,true,false);
        } catch (Exception e) {

        }
    }

    public void goMainUser(ActionEvent event){
        try {
            App.changeScene("/actingSystem/fxml/Main3.0.fxml","MainMenu",null,false,true);
        } catch (Exception e) {

        }
    }

    public void print() {
        LinkedNode.setUserName("admin");
        LinkedNode.setacctLoginStat(true);
        // LinkedNode.setuserLoginStat(true);
    }

    @SuppressWarnings("unused")
    private void checkAccount(ActionEvent event) {
        action = new databaseAction();
        String username = userNameField.getText().trim();
        String password = passwordField.getText().trim();
        String acct = loginaccountType.getValue();
        

        userNameField.setOnMouseClicked(event2 ->{
            if(errorMessage.isVisible()){
                errorMessage.setVisible(false);
            }
        });

        passwordField.setOnMouseClicked(event2 ->{
            if(errorMessage.isVisible()){
                errorMessage.setVisible(false);
            }
        });

        if (username.isEmpty()) {
            errorMessage.setVisible(true);
            errorMessage.setText("Username is empty.");
            return;
        }else if(password.isEmpty()){
            errorMessage.setVisible(true);
            errorMessage.setText("Password is empty.");
            return;
        }else if(acct==null){
            errorMessage.setVisible(true);
            errorMessage.setText("Please select account type");
            return;
        }
        
        if(acct.equals("Accountant")){
            System.out.println("Hello");
            LinkedNode.setacctLoginStat(action.readAccountant(username, password));
            System.out.println(LinkedNode.getAcctLoginStat());
            if(LinkedNode.getAcctLoginStat()){
                LinkedNode.setUserName(username);
                goMain(event);
            }else{
                errorMessage.setVisible(true);
                errorMessage.setText("The credentials are invalid!");
            }
        }else if(acct.equals("User")){
            System.out.println("user");
            LinkedNode.setuserLoginStat(action.readUser(username, password));
            if (LinkedNode.getUserLoginStat()) {
                LinkedNode.setUserName(username);
                goMainUser(event);
            } else {
                errorMessage.setVisible(true);
                errorMessage.setText("The credentials are invalid!");
            }
        }
    }

    private void createAccount(ActionEvent event) {

        action = new databaseAction();
        String username =createUsrname.getText().trim();
        String createPass = createPassword.getText().trim();
        String checkPass = checkPassword.getText().trim();
        String acct = accountType.getValue();
        String name = acctName.getText().trim();
        
        if(!createPass.isEmpty() && !checkPass.isEmpty() && acct!=null && !username.isEmpty()){
            if(createPass.equals(checkPass)){
                if(action.createUser(createUsrname.getText().trim(),createPass ,accountType.getValue())){
                    if(!acctName.isDisable()){
                        if(!name.isEmpty()){
                            String relation =action.createRelation(username,name);
                            if(relation.equals("true")){
                                switchToLogin();
                            }else{
                                System.out.println(relation);
                            }
                        }else{
                            errorMessage1.setVisible(true);
                            errorMessage1.setText("Please Choose an accountant.");
                        }
                    }else{
                        switchToLogin();
                    }
                }else{
                    errorMessage1.setVisible(true);
                    errorMessage1.setText("Password doesnot Match with Each other or \n the username already exists");
                }
            }
        }else{
            errorMessage1.setVisible(true);
            errorMessage1.setText("Please Fill all the form");
        }
    }

    private void clearErrorMessage() {
        if (errorMessage1.isVisible()) {
            errorMessage1.setVisible(false);
        }
        if(errorMessage.isVisible()){
            errorMessage1.setVisible(false);
        }
    }
    
    @SuppressWarnings("unused")
    private void transitionEffect(VBox fadeout,VBox fadin){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.2));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.setNode(fadeout);
        fadeOut.setOnFinished(e -> {
            fadeout.setVisible(false); 
            fadin.setVisible(true);  
            fadeIn.setNode(fadin); 
            fadeIn.play();  
        });
        fadeOut.play();
    }

    @SuppressWarnings("unused")
    private void switchToCreateAccount() {
        transitionEffect(login,createAccount);

        errorMessage1.setVisible(false);
        checkPassword.clear();
        accountType.getItems().clear();
        accountType.getItems().addAll("Accountant", "User");
        accountType.setPromptText("Choose Account Type");
        createPassword.clear();
        createUsrname.clear();
        
        checkPassword.setDisable(true);

        createPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty() || newValue.length() < 8) {
                checkPassword.setDisable(true);
            } else {
                checkPassword.clear();
                checkPassword.setDisable(false);
            }
        });

        // Validate username to allow only alphabetic characters and spaces
        createUsrname.addEventFilter(KeyEvent.KEY_TYPED, event1 -> {
            String character = event1.getCharacter();
            if (!character.matches("[a-zA-Z ]")) {
                event1.consume();  // Prevent the non-alphabetical character from being typed
            }

        });
        
        accountType.setOnMouseClicked(event2 -> clearErrorMessage());
        createUsrname.setOnMouseClicked(event2 -> clearErrorMessage());
        createPassword.setOnMouseClicked(event2 -> clearErrorMessage());
        checkPassword.setOnMouseClicked(event2 -> clearErrorMessage());
        
    }

    @SuppressWarnings("unused")
    private void switchToLogin() {
        errorMessage.setVisible(false);
        userNameField.clear();
        passwordField.clear();
        loginaccountType.getItems().clear();
        loginaccountType.getItems().addAll("Accountant", "User");
        loginaccountType.setPromptText("Choose Account Type");
        loginaccountType.getSelectionModel().clearSelection();
        transitionEffect(createAccount,login);
        
        passwordField.setOnMouseClicked(event2 -> clearErrorMessage());
        userNameField.setOnMouseClicked(event2 -> clearErrorMessage());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        link = new LinkedNode();
        datePicker.setText(link.getDateString());

        switchToLogin();
        
        createAccountLabel.setOnMouseClicked(event -> switchToCreateAccount());
        logIn.setOnMouseClicked(event -> switchToLogin());
        loginButton.setOnAction(this::checkAccount);
        createButton.setOnAction(this::createAccount);
        acctName.setDisable(true);

        accountType.setOnAction(event->{
            if(accountType.getValue().equals("User")){
                acctName.setDisable(false);
            }else{
                acctName.setDisable(true);
            }
        }); 

    }
    
}
