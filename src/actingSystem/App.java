package actingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import actingSystem.controller.MainController;
import actingSystem.controller.editController;
import actingSystem.controller.ledgerController;
import actingSystem.controller.legderMenuController;
import actingSystem.controller.transactionController;
import actingSystem.database.databaseConnection;
import actingSystem.controller.loginController;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    static private StackPane rootPane;
    private static final int CONNECTION_CHECK_INTERVAL = 5000; // Check every 5 seconds
    private volatile boolean running = true; // Flag to control the monitoring thread
    private Connection connection; // Hold the current database connection
    private static boolean  operation;
    
    @Override
    public void start(Stage stage) {
        try {
            // Load the main FXML and set it to the scene
            loginController obje = new loginController();
            obje.print();
            rootPane = new StackPane();
            Parent mainView = FXMLLoader.load(getClass().getResource("/actingSystem/fxml/login.fxml"));
            rootPane.getChildren().add(mainView);  // Set Main.fxml as the initial view
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.show();

            // Attempt initial connection and start monitoring in a background thread
            attemptDatabaseConnectionWithRetry(stage);
            startConnectionMonitor(stage);

            stage.setOnCloseRequest(event -> {
                if(operation){
                    transactionController controller = new transactionController();
                    controller.handleExitWithSaveConfirmation(event);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attemptDatabaseConnectionWithRetry(Stage stage) {
        while (true) {
            try {
                connection = databaseConnection.getConnectionWithoutDb();// Try connecting to the database
                break; // If connection succeeds, exit the loop
            } catch (SQLException e) {
                Optional<ButtonType> result = showRetryDialog("Database Connection Error",
                        "Unable to connect to the database. Please check your settings.",
                        e.getMessage());

                // If user cancels, close the application
                if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                    stage.close();
                    Platform.exit();
                    break;
                }
            }
        }
    }

    private void startConnectionMonitor(Stage stage) {
        // Start a background thread to monitor the connection status
        Thread monitorThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(CONNECTION_CHECK_INTERVAL);
                    if (!isConnectionValid()) {
                        // Connection lost
                        handleConnectionLoss(stage);
                    }
                } catch (InterruptedException ignored) {
                }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    private void handleConnectionLoss(Stage stage) {
        // Show an alert about the connection loss
        Platform.runLater(() -> {
            // Attempt to reconnect in the background
            attemptDatabaseConnectionWithRetry(stage);
        });
    }

    private boolean isConnectionValid() {
        try {
            return connection != null && connection.isValid(2); // Check if connection is valid
        } catch (SQLException e) {
            return false; // If an exception is thrown, the connection is invalid
        }
    }
    
    private Optional<ButtonType> showRetryDialog(String title, String header, String content) {
        // Define a custom ButtonType for "Retry"
        ButtonType retryButton = new ButtonType("Retry"); // Custom Retry button
        ButtonType cancelButton = ButtonType.CANCEL; // Use the built-in CANCEL ButtonType
    
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
    
        // Set the dialog's buttons to include Retry and Cancel
        alert.getButtonTypes().setAll(retryButton, cancelButton);
    
        // Show the dialog and wait for the user's response
        return alert.showAndWait();
    }

    @SuppressWarnings("unused")
    public static void changeScene(String fxmlLocation, String title, LocalDate date,boolean transaction,boolean report) {
        try {
            operation = false;
            // Load the new FXML content
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlLocation));
            Parent newView = loader.load();

            // Handle specific scene logic based on the title
            if (title != null) {
                if (title.equals("MainMenu")) {
                    MainController controller = loader.getController();
                    controller.chooseFirstAppearence(transaction, report);
                }
                if (title.equals("LedgerMenu")) {
                    legderMenuController controller = loader.getController();
                    controller.setDate(date);
                } else if (title.equals("transaction")) {
                    operation = true;
                } else if (!title.equals("MainMenu") && !title.equals("LedgerMenu")&& !title.equals("Payment")&& !title.equals("Journal Entry")&& !title.equals("Receipt") && !title.equals("Edit")) {
                    ledgerController controller = loader.getController();
                    controller.setTitle(title, date);
                }else if (title.equals("Payment")) {
                    operation = true;
                    transactionController controller = loader.getController();
                    controller.setTitle(title);
                }else if (title.equals("Journal Entry")) {
                    operation = true;
                    transactionController controller = loader.getController();
                    controller.setTitle(title);
                }else if (title.equals("Receipt")) {
                    operation = true;
                    transactionController controller = loader.getController();
                    controller.setTitle(title);
                }else if(title.equals("Edit")){
                    editController controller = loader.getController();
                }
            }

            // Get the root pane
            StackPane rootPane = (StackPane) App.rootPane;
            rootPane.setStyle("-fx-background-color:#c2dac8;");
            // Add the new view but keep it invisible
            rootPane.getChildren().add(newView);
            newView.setOpacity(0); // Make the new content invisible initially

            // Fade-out the current content
            FadeTransition fadeOut = new FadeTransition(Duration.millis(100), rootPane.getChildren().get(0)); // Fade out the old content
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // Timeline to swap the views after the fade-out transition finishes
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    // After fade-out completes, remove the old view
                    rootPane.getChildren().remove(0);

                    // Fade-in the new content
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(100), newView);
                    fadeIn.setFromValue(0.0);  // Start with transparent new scene
                    fadeIn.setToValue(1.0);    // Fade to fully opaque new scene
                    fadeIn.play();  // Play the fade-in transition
                })
            );

            // Start the fade-out and then the view swap
            fadeOut.setOnFinished(event -> {
                timeline.play(); // Play the timeline to switch views
            });

            // Start the fade-out transition
            fadeOut.play();

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("Error");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
