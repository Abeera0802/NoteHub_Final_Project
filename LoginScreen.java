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

public class LoginScreen {

    public void show(Stage stage) {
        Image backgroundImg = new Image("img.jpg");
        ImageView backgroundView = new ImageView(backgroundImg);
        backgroundView.fitWidthProperty().bind(stage.widthProperty());
        backgroundView.fitHeightProperty().bind(stage.heightProperty());
        VBox root = new VBox(20);

        Label label = new Label("Login Page");
        label.setFont(new Font("Britannic Bold", 50));
        label.setTextFill(Color.web("#81D4D8"));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DARKBLUE);
        dropShadow.setOffsetX(6);
        dropShadow.setOffsetY(6);
        label.setEffect(dropShadow);

        // Input fields for username and password
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(300);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setFont(new Font("Britannic Bold", 16));
        loginButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white; -fx-padding: 10px 20px;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;"));

        loginButton.setEffect(dropShadow);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setFont(new Font("Britannic Bold", 16));
        backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white; -fx-padding: 10px 20px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;"));
        backButton.setEffect(dropShadow);
        backButton.setOnAction(e -> new Main().showLoginPage(stage));

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username and Password cannot be empty!");
                return;
            }

            if (isExistingUser(username, password)) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Username or Password already exists!");
            } else {
                createUserFiles(username, password);
                new NotesScreen().show(stage, username); // Open notes screen
            }
        });

        root.getChildren().addAll(label, usernameField, passwordField, loginButton, backButton);
        root.setAlignment(Pos.CENTER);

        StackPane mainroot = new StackPane();
        mainroot.getChildren().addAll(backgroundView, root);

        Scene scene = new Scene(mainroot, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private boolean isExistingUser(String username, String password) {
        File userFolder = new File("users/" + username);
        File credentialsFile = new File(userFolder, "credentials.txt");

        if (credentialsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
                String savedUsername = reader.readLine().split(": ")[0];
                String savedPassword = reader.readLine().split(": ")[0];

                return username.equals(savedUsername) && password.equals(savedPassword);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void createUserFiles(String username, String password) {
        try {
            // Create a folder for the user
            File userFolder = new File("users/" + username);
            if (!userFolder.exists()) {
                userFolder.mkdirs();
            }

            // Save the user's credentials
            File credentialsFile = new File(userFolder, "credentials.txt");
            try (FileWriter writer = new FileWriter(credentialsFile)) {
                writer.write(username + "\n");
                writer.write(password + "\n");
            }

            System.out.println("User files created for: " + username);
        } catch (IOException e) {
            e.printStackTrace();
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
