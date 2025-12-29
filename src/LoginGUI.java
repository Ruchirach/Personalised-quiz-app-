import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;

public class LoginGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login - Personalized Quiz App");

        // === Safe Font Loading ===
        URL fontUrl = getClass().getResource("/fonts/Roboto-VariableFont_wdth,wght.ttf");
        if (fontUrl != null) {
            Font.loadFont(fontUrl.toExternalForm(), 14);
        } else {
            System.out.println("⚠️ Font not found!");
        }

        // === Grid Layout for Form ===
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("grid-pane"); // For CSS

        // === UI Elements ===
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        Label messageLabel = new Label();

        // === Add to Grid ===
        grid.add(userLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(registerButton, 1, 3);
        grid.add(messageLabel, 1, 4);

        // === VBox Layout (just centered form) ===
        VBox root = new VBox(20, grid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // === Scene Setup ===
        Scene scene = new Scene(root, 600, 400);
        URL cssUrl = getClass().getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("⚠️ CSS file not found!");
        }

        // === Button Actions ===
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("⚠️ Username and password cannot be empty.");
                return;
            }

            try {
                String role = Login.validate(username, password);
                int userId = Login.getUserId(username);

                if (role != null && userId != -1) {
                    messageLabel.setText("✅ Login successful as " + role);
                    switch (role.toLowerCase()) {
                        case "student" -> StudentPanelGUI.show(primaryStage, userId);
                        case "teacher" -> TeacherPanelGUI.show(primaryStage);
                        default -> messageLabel.setText("⚠️ Unknown role: " + role);
                    }
                } else {
                    messageLabel.setText("❌ Invalid username or password.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                messageLabel.setText("❌ An error occurred during login.");
            }
        });

        registerButton.setOnAction(e -> {
            try {
                RegisterGUI.show(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
                messageLabel.setText("❌ Unable to open registration page.");
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
