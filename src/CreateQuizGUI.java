import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class CreateQuizGUI {

    private static List<Question> questions = new ArrayList<>();

    public static void show(Stage stage) {
        // === UI Elements ===
        Label titleLabel = new Label("Quiz Title:");
        TextField titleField = new TextField();

        Label questionLabel = new Label("Question:");
        TextField questionField = new TextField();

        Label optionsLabel = new Label("Options:");
        TextField optA = new TextField();
        TextField optB = new TextField();
        TextField optC = new TextField();
        TextField optD = new TextField();

        Label correctLabel = new Label("Correct Option:");
        ComboBox<String> correctOption = new ComboBox<>(FXCollections.observableArrayList("A", "B", "C", "D"));
        correctOption.setValue("A");

        Button addQuestionButton = new Button("➕ Add Question");
        Button finishQuizButton = new Button("✅ Finish Quiz");
        Button backButton = new Button("⬅ Back");

        Label statusLabel = new Label("Questions added: 0");

        // === Layout Setup ===
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("grid-pane");  // CSS background box

        // === Adding Elements ===
        int row = 0;
        grid.add(titleLabel, 0, row);
        grid.add(titleField, 1, row++);

        grid.add(questionLabel, 0, row);
        grid.add(questionField, 1, row++);

        grid.add(optionsLabel, 0, row++);
        grid.add(new Label("A:"), 0, row);
        grid.add(optA, 1, row++);
        grid.add(new Label("B:"), 0, row);
        grid.add(optB, 1, row++);
        grid.add(new Label("C:"), 0, row);
        grid.add(optC, 1, row++);
        grid.add(new Label("D:"), 0, row);
        grid.add(optD, 1, row++);

        grid.add(correctLabel, 0, row);
        grid.add(correctOption, 1, row++);

        HBox buttonBox = new HBox(10, addQuestionButton, finishQuizButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, row++);

        grid.add(backButton, 1, row++);
        grid.add(statusLabel, 1, row);

        // === Button Actions ===
        addQuestionButton.setOnAction(e -> {
            if (questionField.getText().isEmpty() || optA.getText().isEmpty()
                    || optB.getText().isEmpty() || optC.getText().isEmpty() || optD.getText().isEmpty()) {
                statusLabel.setText("⚠️ Please fill all fields!");
                return;
            }

            Question q = new Question(
                    questionField.getText(), optA.getText(), optB.getText(), optC.getText(), optD.getText(),
                    correctOption.getValue()
            );
            questions.add(q);

            questionField.clear();
            optA.clear();
            optB.clear();
            optC.clear();
            optD.clear();
            correctOption.setValue("A");

            statusLabel.setText("✅ Questions added: " + questions.size());
        });

        finishQuizButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            if (title.isEmpty() || questions.isEmpty()) {
                statusLabel.setText("⚠️ Quiz title and at least one question required.");
                return;
            }

            String quizCode = "QZ" + (System.currentTimeMillis() % 100000);
            int quizId = Database.saveQuiz(title, quizCode, questions);

            if (quizId != -1) {
                statusLabel.setText("✅ Quiz saved! Code: " + quizCode);
                questions.clear();
                titleField.clear();
            } else {
                statusLabel.setText("❌ Failed to save quiz.");
            }
        });

        backButton.setOnAction(e -> TeacherPanelGUI.show(stage));

        // === Scene Setup ===
        VBox root = new VBox(grid);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 600, 600);
        var css = CreateQuizGUI.class.getResource("/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        // === Fade-in Animation ===
        FadeTransition fade = new FadeTransition(Duration.millis(800), grid);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        stage.setScene(scene);
        stage.setTitle("Create Quiz");
        stage.show();
    }

    public static class Question {
        String question, a, b, c, d, correct;

        public Question(String question, String a, String b, String c, String d, String correct) {
            this.question = question;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.correct = correct;
        }
    }
}
