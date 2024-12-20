import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class AddNotesScreen {
    public void show(Stage stage, String username) {
        Image backgroundImg = new Image("bg.jpg");
        ImageView backgroundView = new ImageView(backgroundImg);
        backgroundView.fitWidthProperty().bind(stage.widthProperty());
        backgroundView.fitHeightProperty().bind(stage.heightProperty());
        VBox root = new VBox(20);

        Label titleLabel = new Label("Add Notes for " + username);
        titleLabel.setFont(new Font("Britannic Bold", 55));
        titleLabel.setTextFill(Color.DARKBLUE);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.SKYBLUE);
        dropShadow.setOffsetX(6);
        dropShadow.setOffsetY(6);
        titleLabel.setEffect(dropShadow);

        Label Ntitle = new Label("Note's Title");
        Ntitle.setFont(new Font("Britannic Bold", 30));
        Ntitle.setTextFill(Color.DARKBLUE);

        TextField noteTitleField = new TextField();
        noteTitleField.setPromptText("Enter note title...");
        noteTitleField.setMaxWidth(600);

        Label content = new Label("Note's Content");
        content.setFont(new Font("Britannic Bold", 30));
        content.setTextFill(Color.DARKBLUE);

        TextArea noteArea = new TextArea();
        noteArea.setPromptText("Write your note here...");
        noteArea.setWrapText(true);
        noteArea.setMaxWidth(600);

        Button saveButton = new Button("Save Note");
        saveButton.setFont(new Font("Britannic Bold", 16));
        saveButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white; -fx-padding: 10px 20px;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;"));

        saveButton.setEffect(dropShadow);

        Button backButton = new Button("Back");
        backButton.setFont(new Font("Britannic Bold", 16));
        backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white; -fx-padding: 10px 20px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: skyblue; -fx-padding: 10px 20px;"));

        // Save note to user's file
        saveButton.setOnAction(e -> {
            String title = noteTitleField.getText();
            String note = noteArea.getText();
            if (!title.isEmpty() && !note.isEmpty()) {
                saveNoteForUser(username, title, note);
                noteArea.clear();
                noteTitleField.clear();
            } else {
                System.out.println("Title and note cannot be empty!");
            }
        });

        // Go back to the NotesScreen
        backButton.setEffect(dropShadow);
        backButton.setOnAction(e -> new NotesScreen().show(stage, username));

        root.getChildren().addAll(titleLabel,Ntitle, noteTitleField,content, noteArea, saveButton, backButton);
        root.setAlignment(Pos.CENTER);

        StackPane mainroot = new StackPane();
        mainroot.getChildren().addAll(backgroundView, root);

        Scene scene = new Scene(mainroot, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void saveNoteForUser(String username, String title, String note) {
        Path userFolder = Paths.get("users/" + username);
        if (!Files.exists(userFolder)) {
            System.out.println("User folder does not exist!");
            return;
        }

        // Generate a filename using the title
        String noteFileName = title.replaceAll("\\s+", "_") + ".txt";
        File noteFile = new File(userFolder.toFile(), noteFileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(noteFile))) {
            writer.write(note);
            System.out.println("Note saved successfully in file: " + noteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
