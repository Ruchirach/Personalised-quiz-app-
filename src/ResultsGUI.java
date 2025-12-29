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
import java.sql.*;

public class ResultsGUI {

    public static void show(Stage stage, int studentId) {
        stage.setTitle("Quiz Results");

        // === Title Label ===
        Label title = new Label("📊 My Quiz Results");
        title.getStyleClass().add("results-title");  // CSS styling

        // === TableView Setup ===
        TableView<ResultEntry> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ResultEntry, String> quizCol = new TableColumn<>("Quiz");
        quizCol.setCellValueFactory(data -> data.getValue().quizTitleProperty());

        TableColumn<ResultEntry, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty().asObject());

        TableColumn<ResultEntry, Integer> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data -> data.getValue().totalProperty().asObject());

        TableColumn<ResultEntry, String> dateCol = new TableColumn<>("Attempted On");
        dateCol.setCellValueFactory(data -> data.getValue().attemptedOnProperty());

        table.getColumns().addAll(quizCol, scoreCol, totalCol, dateCol);
        table.setItems(loadResults(studentId));

        // === Back Button ===
        Button backBtn = new Button("⬅ Back");
        backBtn.setMaxWidth(Double.MAX_VALUE);  // Full-width button
        backBtn.setOnAction(e -> StudentPanelGUI.show(stage, studentId));

        // === GridPane for Styling ===
        VBox innerBox = new VBox(15, title, table, backBtn);
        innerBox.setPadding(new Insets(20));
        innerBox.getStyleClass().add("grid-pane");  // White rounded box

        // === Root VBox ===
        VBox root = new VBox(innerBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // === Scene Setup ===
        Scene scene = new Scene(root, 700, 450);
        var css = ResultsGUI.class.getResource("/styles.css");
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

    private static ObservableList<ResultEntry> loadResults(int studentId) {
        ObservableList<ResultEntry> list = FXCollections.observableArrayList();
        String query = """
            SELECT q.quiz_name, r.score, r.total, r.attempted_on
            FROM Results r
            JOIN Quiz q ON r.quiz_id = q.quiz_id
            WHERE r.user_id = ?
            ORDER BY r.attempted_on DESC
        """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String quizName = rs.getString("quiz_name");
                int score = rs.getInt("score");
                int total = rs.getInt("total");
                String date = rs.getString("attempted_on");

                list.add(new ResultEntry(quizName, score, total, date));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
