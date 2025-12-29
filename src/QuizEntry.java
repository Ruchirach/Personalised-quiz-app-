import javafx.beans.property.SimpleStringProperty;

public class QuizEntry {
    private final SimpleStringProperty quizTitle;
    private final SimpleStringProperty quizCode;
    private final SimpleStringProperty createdOn;
    private final SimpleStringProperty status;

    public QuizEntry(String quizTitle, String quizCode, String createdOn, String status) {
        this.quizTitle = new SimpleStringProperty(quizTitle);
        this.quizCode = new SimpleStringProperty(quizCode);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.status = new SimpleStringProperty(status);
    }

    // Title
    public String getQuizTitle() {
        return quizTitle.get();
    }

    public void setQuizTitle(String title) {
        quizTitle.set(title);
    }

    public SimpleStringProperty quizTitleProperty() {
        return quizTitle;
    }

    // Access Code
    public String getQuizCode() {
        return quizCode.get();
    }

    public void setQuizCode(String code) {
        quizCode.set(code);
    }

    public SimpleStringProperty quizCodeProperty() {
        return quizCode;
    }

    // Created On
    public String getCreatedOn() {
        return createdOn.get();
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn.set(createdOn);
    }

    public SimpleStringProperty createdOnProperty() {
        return createdOn;
    }

    // Status (Active/Inactive)
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
