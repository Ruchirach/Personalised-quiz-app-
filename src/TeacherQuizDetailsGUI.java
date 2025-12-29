import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;

public class TeacherQuizDetailsGUI {

    public static void show(Stage stage) {
        stage.setTitle("Quiz Details");

        // === Title Label ===
        Label title = new Label("📋 My Created Quizzes");
        title.getStyleClass().add("quiz-title");

        // === TableView ===
        TableView<QuizEntry> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("table-view"); // Apply CSS class

        TableColumn<QuizEntry, String> titleCol = new TableColumn<>("Quiz Title");
        titleCol.setCellValueFactory(data -> data.getValue().quizTitleProperty());

        TableColumn<QuizEntry, String> codeCol = new TableColumn<>("Access Code");
        codeCol.setCellValueFactory(data -> data.getValue().quizCodeProperty());

        TableColumn<QuizEntry, String> dateCol = new TableColumn<>("Created On");
        dateCol.setCellValueFactory(data -> data.getValue().createdOnProperty());

        TableColumn<QuizEntry, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        table.getColumns().addAll(titleCol, codeCol, dateCol, statusCol);
        table.setItems(loadQuizEntries());

        // === Back Button ===
        Button backBtn = new Button("⬅ Back");
        backBtn.setOnAction(e -> TeacherPanelGUI.show(stage));

        // === Layout for white box ===
        VBox grid = new VBox(15, title, table, backBtn);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("grid-pane");  // White box with rounded corners

        // === Root VBox with background ===
        VBox root = new VBox(grid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");  // Apply background image

        // === Scene Setup ===
        Scene scene = new Scene(root, 700, 500);
        URL css = TeacherQuizDetailsGUI.class.getResource("/styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.out.println("⚠️ styles.css not found!");
        }

        // === Fade-in Animation ===
        FadeTransition fade = new FadeTransition(Duration.millis(800), grid);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        stage.setScene(scene);
        stage.show();
    }

    private static ObservableList<QuizEntry> loadQuizEntries() {
        ObservableList<QuizEntry> list = FXCollections.observableArrayList();
        String query = "SELECT quiz_name, access_code, created_on, isActive FROM Quiz ORDER BY created_on DESC";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("quiz_name");
                String code = rs.getString("access_code");
                String createdOn = rs.getString("created_on");
                String status = (rs.getInt("isActive") == 1) ? "Active" : "Inactive";

                list.add(new QuizEntry(title, code, createdOn, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
