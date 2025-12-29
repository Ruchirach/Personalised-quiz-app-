import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;

public class RegisterGUI {

    public static void show(Stage stage) {
        stage.setTitle("Register - Personalized Quiz App");

        // === GridPane Layout ===
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);  // Center the grid
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("grid-pane"); // For CSS (white box style)

        // === UI Elements ===
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label roleLabel = new Label("Role:");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "student", "teacher");
        roleBox.setValue("student");

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");

        Label statusLabel = new Label();

        // === Add Elements to Grid ===
        grid.add(userLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(roleLabel, 0, 2);
        grid.add(roleBox, 1, 2);
        grid.add(registerButton, 1, 3);
        grid.add(backButton, 1, 4);
        grid.add(statusLabel, 1, 5);

        // === VBox Root Layout (Center content) ===
        VBox root = new VBox(20, grid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // === Scene Setup ===
        Scene scene = new Scene(root, 600, 400);  // Match Login size

        // === Load CSS Safely ===
        URL css = RegisterGUI.class.getResource("/styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.out.println("⚠️ styles.css not found!");
        }

        // === Fade-in Animation ===
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), grid);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // === Button Actions ===
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleBox.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("⚠️ Please fill all fields.");
                return;
            }

            Login.registerUser(username, password, role);
            statusLabel.setText("✅ Registration successful!");
        });

        backButton.setOnAction(e -> {
            try {
                new LoginGUI().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
                statusLabel.setText("❌ Failed to return to login.");
            }
        });

        stage.setScene(scene);
        stage.show();
    }
}
