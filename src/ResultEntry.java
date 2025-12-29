import javafx.beans.property.*;

public class ResultEntry {
    private final StringProperty quizTitle;
    private final IntegerProperty score;
    private final IntegerProperty total;
    private final StringProperty attemptedOn;

    public ResultEntry(String quizTitle, int score, int total, String attemptedOn) {
        this.quizTitle = new SimpleStringProperty(quizTitle);
        this.score = new SimpleIntegerProperty(score);
        this.total = new SimpleIntegerProperty(total);
        this.attemptedOn = new SimpleStringProperty(attemptedOn);
    }

    public StringProperty quizTitleProperty() {
        return quizTitle;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public StringProperty attemptedOnProperty() {
        return attemptedOn;
    }
}
