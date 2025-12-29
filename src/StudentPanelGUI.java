import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;

public class StudentPanelGUI {

    public static void show(Stage stage, int studentId) {
        stage.setTitle("Student Panel");

        // === Welcome Label ===
        Label welcome = new Label("Welcome Student!");
        welcome.getStyleClass().add("welcome-label");  // Apply CSS

        // === Quiz Code Input ===
        Label codeLabel = new Label("Enter Quiz Code:");
        TextField codeField = new TextField();

        // === Buttons ===
        Button joinQuizBtn = new Button("Join Quiz");
        joinQuizBtn.setMaxWidth(Double.MAX_VALUE);  // Full width

        Button resultsBtn = new Button("📊 View Results");
        resultsBtn.setMaxWidth(Double.MAX_VALUE);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);

        Label statusLabel = new Label();

        // === GridPane Layout ===
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(15);  // More vertical space
        grid.getStyleClass().add("grid-pane");

        // === Add Elements ===
        grid.add(welcome, 0, 0, 2, 1);
        GridPane.setHalignment(welcome, HPos.CENTER);

        grid.add(codeLabel, 0, 1);
        grid.add(codeField, 1, 1);
        grid.add(joinQuizBtn, 1, 2);
        grid.add(resultsBtn, 1, 3);
        grid.add(logoutBtn, 1, 4);
        grid.add(statusLabel, 1, 5);

        // === Root Layout ===
        VBox root = new VBox(grid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // === Scene Setup ===
        Scene scene = new Scene(root, 600, 400);
        URL css = StudentPanelGUI.class.getResource("/styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.out.println("⚠️ styles.css not found!");
        }

        // === Fade Animation ===
        FadeTransition fade = new FadeTransition(Duration.millis(800), grid);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        // === Button Actions ===
        joinQuizBtn.setOnAction(e -> {
            String code = codeField.getText().trim();
            if (code.isEmpty()) {
                statusLabel.setText("⚠️ Please enter a quiz code.");
                return;
            }

            if (!Database.quizExists(code)) {
                statusLabel.setText("❌ Quiz not found.");
            } else if (!Database.isQuizActive(code)) {
                statusLabel.setText("⏳ Quiz has not started yet.");
            } else {
                AttemptQuizGUI.show(stage, code, studentId);
            }
        });

        resultsBtn.setOnAction(e -> ResultsGUI.show(stage, studentId));

        logoutBtn.setOnAction(e -> {
            try {
                new LoginGUI().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        stage.setScene(scene);
        stage.show();
    }
}
