import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class TeacherPanelGUI {

    public static void show(Stage stage) {
        stage.setTitle("Teacher Dashboard");

        // === Title Label ===
        Label welcomeLabel = new Label("Welcome to Teacher Panel!");
        welcomeLabel.getStyleClass().add("welcome-label");  // Bold, centered

        // === Buttons ===
        Button createQuizBtn = new Button("➕ Create Quiz");
        Button quizDetailsBtn = new Button("📋 Quiz Details");
        Button manageQuizBtn = new Button("🛠 Manage Quiz");
        Button logoutBtn = new Button("🚪 Logout");

        // === Button Actions ===
        createQuizBtn.setOnAction(e -> CreateQuizGUI.show(stage));
        quizDetailsBtn.setOnAction(e -> TeacherQuizDetailsGUI.show(stage));
        manageQuizBtn.setOnAction(e -> ManageQuizGUI.show(stage));
        logoutBtn.setOnAction(e -> {
            try {
                new LoginGUI().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // === Inner VBox (white box) ===
        VBox innerBox = new VBox(20, welcomeLabel, createQuizBtn, quizDetailsBtn, manageQuizBtn, logoutBtn);
        innerBox.setAlignment(Pos.CENTER);
        innerBox.setPadding(new Insets(20));
        innerBox.getStyleClass().add("grid-pane");  // White rounded box

        // === Outer VBox ===
        VBox root = new VBox(innerBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // === Scene Setup ===
        Scene scene = new Scene(root, 600, 400);
        URL css = TeacherPanelGUI.class.getResource("/styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.out.println("⚠️ styles.css not found!");
        }

        // === Fade-in Animation ===
        FadeTransition fade = new FadeTransition(Duration.millis(800), innerBox);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        stage.setScene(scene);
        stage.show();
    }
}
