import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttemptQuizGUI {

    public static void show(Stage stage, String quizCode, int studentId) {
        List<CreateQuizGUI.Question> questions = fetchQuestions(quizCode);
        if (questions.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "❌ No questions found for this quiz.");
            alert.showAndWait();
            StudentPanelGUI.show(stage, studentId);
            return;
        }

        // === VBox Layout ===
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("grid-pane"); // Rounded white pane

        // === Title ===
        Label title = new Label("📚 Attempt Quiz - Code: " + quizCode);
        title.getStyleClass().add("quiz-title");
        layout.getChildren().add(title);

        // === Question Blocks ===
        List<ToggleGroup> toggleGroups = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            CreateQuizGUI.Question q = questions.get(i);

            Label qLabel = new Label((i + 1) + ". " + q.question);
            qLabel.getStyleClass().add("question-label");

            RadioButton a = new RadioButton(q.a);
            RadioButton b = new RadioButton(q.b);
            RadioButton c = new RadioButton(q.c);
            RadioButton d = new RadioButton(q.d);

            ToggleGroup group = new ToggleGroup();
            a.setToggleGroup(group);
            b.setToggleGroup(group);
            c.setToggleGroup(group);
            d.setToggleGroup(group);
            toggleGroups.add(group);

            VBox qBox = new VBox(8, qLabel, a, b, c, d);
            qBox.getStyleClass().add("question-box");
            layout.getChildren().add(qBox);
        }

        // === Submit Button ===
        Button submitBtn = new Button("Submit Quiz");
        submitBtn.getStyleClass().add("button");

        submitBtn.setOnAction(e -> {
            int score = 0;
            int total = questions.size();

            for (int i = 0; i < questions.size(); i++) {
                CreateQuizGUI.Question q = questions.get(i);
                ToggleGroup group = toggleGroups.get(i);
                RadioButton selected = (RadioButton) group.getSelectedToggle();
                if (selected != null && selected.getText().equalsIgnoreCase(getCorrectAnswer(q))) {
                    score++;
                }
            }

            int quizId = Database.getQuizIdByCode(quizCode);
            Database.saveResult(studentId, quizId, score, total);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "✔️ Quiz submitted successfully!");
            alert.showAndWait();

            StudentPanelGUI.show(stage, studentId);
        });

        layout.getChildren().add(submitBtn);

        // === Scene Setup ===
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 600, 700);

        var css = AttemptQuizGUI.class.getResource("/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        // === Fade-in Animation ===
        FadeTransition fade = new FadeTransition(Duration.millis(800), layout);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        stage.setScene(scene);
        stage.setTitle("Attempt Quiz");
        stage.show();
    }

    private static String getCorrectAnswer(CreateQuizGUI.Question q) {
        return switch (q.correct.toUpperCase()) {
            case "A" -> q.a;
            case "B" -> q.b;
            case "C" -> q.c;
            case "D" -> q.d;
            default -> "";
        };
    }

    private static List<CreateQuizGUI.Question> fetchQuestions(String code) {
        List<CreateQuizGUI.Question> questionList = new ArrayList<>();
        try (Connection conn = Database.connect()) {
            String query = """
                SELECT q.question_text, q.option_a, q.option_b, q.option_c, q.option_d, q.correct_option
                FROM Question q
                JOIN Quiz z ON q.quiz_id = z.quiz_id
                WHERE z.access_code = ?
            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String question = rs.getString("question_text");
                String a = rs.getString("option_a");
                String b = rs.getString("option_b");
                String c = rs.getString("option_c");
                String d = rs.getString("option_d");
                String correct = rs.getString("correct_option");
                questionList.add(new CreateQuizGUI.Question(question, a, b, c, d, correct));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questionList;
    }
}
