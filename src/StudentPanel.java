import java.sql.*;
import java.util.*;

public class StudentPanel {

    // Method to display the student menu
    public static void studentMenu(int userId) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("===== Student Panel =====");
            System.out.print("Enter Quiz Code: ");
            String quizCode = scanner.nextLine();
            
            int quizId = getQuizIdByCode(quizCode);
            if (quizId == -1) {
                System.out.println("Invalid Quiz Code!");
                return;
            }

            int score = takeQuiz(scanner, quizId);
            saveResult(userId, quizId, score);
            System.out.println("Your Score: " + score);
            showRanking(quizId);
        }
    }

    // Method to get user ID by username
    public static int getUserId(String username) {
        try (Connection conn = Database.connect()) {
            String query = "SELECT id FROM Users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user ID: " + e.getMessage());
        }
        return -1;
    }

    // Method to get quiz ID by access code
    public static int getQuizIdByCode(String code) {
        try (Connection conn = Database.connect()) {
            String query = "SELECT quiz_id FROM Quizzes WHERE access_code = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quiz_id");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching quiz ID: " + e.getMessage());
        }
        return -1;
    }

    // Method to take the quiz and calculate the score
    public static int takeQuiz(Scanner scanner, int quizId) {
        int score = 0;
        try (Connection conn = Database.connect()) {
            String query = "SELECT * FROM Questions WHERE quiz_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String question = rs.getString("question_text");
                String optionA = rs.getString("option_a");
                String optionB = rs.getString("option_b");
                String optionC = rs.getString("option_c");
                String optionD = rs.getString("option_d");
                String correctOption = rs.getString("correct_option");

                System.out.println("Question: " + question);
                System.out.println("A: " + optionA);
                System.out.println("B: " + optionB);
                System.out.println("C: " + optionC);
                System.out.println("D: " + optionD);
                System.out.print("Your Answer (A/B/C/D): ");
                String answer = scanner.nextLine();

                if (answer.equalsIgnoreCase(correctOption)) {
                    score++;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during quiz: " + e.getMessage());
        }
        return score;
    }

    // Save the quiz result for the student
    public static void saveResult(int userId, int quizId, int score) {
        try (Connection conn = Database.connect()) {
            String query = "INSERT INTO Results (student_id, quiz_id, score) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, quizId);
            pstmt.setInt(3, score);
            pstmt.executeUpdate();
            System.out.println("Result saved successfully!");
        } catch (SQLException e) {
            System.out.println("Error saving result: " + e.getMessage());
        }
    }

    // Display the ranking of students who took the quiz
    public static void showRanking(int quizId) {
        try (Connection conn = Database.connect()) {
            String query = "SELECT student_id, score FROM Results WHERE quiz_id = ? ORDER BY score DESC";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("===== Ranking =====");
            int rank = 1;
            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                int score = rs.getInt("score");
                System.out.println(rank + ". Student ID: " + studentId + " - Score: " + score);
                rank++;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching ranking: " + e.getMessage());
        }
    }
}
