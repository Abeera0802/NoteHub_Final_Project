import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.*;

public class SignupScreen {
    public void show(Stage stage) {

        Image backgroundImg = new Image("img.jpg");
        ImageView backgroundView = new ImageView(backgroundImg);
        backgroundView.fitWidthProperty().bind(stage.widthProperty());
        backgroundView.fitHeightProperty().bind(stage.heightProperty());
        VBox root = new VBox(20);

        Label titleLabel = new Label("Signup Page");
        titleLabel.setFont(new Font("Britannic Bold", 40));
        titleLabel.setTextFill(Color.SKYBLUE);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DARKBLUE);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        titleLabel.setEffect(dropShadow);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(300);

        Button signupButton = new Button("Signup");
        signupButton.setFont(new Font("Britannic Bold", 16));
        signupButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;");
        signupButton.setOnMouseEntered(e -> signupButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white; -fx-padding: 10px 20px;"));
        signupButton.setOnMouseExited(e -> signupButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;"));
        signupButton.setEffect(dropShadow);
        signupButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username and password cannot be empty!");
                return;
            }

            handleSignup(stage, username, password);
        });

        Button backButton = new Button("Back");
        backButton.setFont(new Font("Britannic Bold", 16));
        backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white; -fx-padding: 10px 20px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;"));
        backButton.setOnAction(e -> new Main().showLoginPage(stage));
        backButton.setEffect(dropShadow);

        root.getChildren().addAll(titleLabel, usernameField, passwordField, signupButton, backButton);
        root.setAlignment(Pos.CENTER);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, root);

        Scene scene = new Scene(stackPane, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void handleSignup(Stage stage, String username, String password) {
        Path userDirectory = Paths.get("users");

        try {
            if (!Files.exists(userDirectory)) {
                showAlert(Alert.AlertType.ERROR, "Error", "No users exist in the system!");
                return;
            }

            boolean userExists = false;
            boolean credentialsMatch = false;

            // Check all user folders
            for (File userFolder : userDirectory.toFile().listFiles()) {
                if (userFolder.isDirectory()) {
                    Path credentialsFile = userFolder.toPath().resolve("credentials.txt");
                    if (Files.exists(credentialsFile)) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile.toFile()))) {
                            String savedUsername = reader.readLine();
                            String savedPassword = reader.readLine();

                            if (username.equals(savedUsername)) {
                                userExists = true;
                                if (password.equals(savedPassword)) {
                                    credentialsMatch = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (credentialsMatch) {
                new NotesScreen().show(stage, username); // Redirect to saved notes
            } else if (userExists) {
                showAlert(Alert.AlertType.ERROR, "Error", "Password is incorrect for the existing username!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "User with this username does not exist!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred during signup.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
