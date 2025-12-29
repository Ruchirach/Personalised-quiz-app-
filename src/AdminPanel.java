import java.sql.*;
import java.util.Scanner;

public class AdminPanel {

    // Method to create a quiz
    public static void createQuiz(String quizName) {
        try (Connection conn = Database.connect()) {
            String code = "QZ" + (int)(Math.random() * 10000);
            String query = "INSERT INTO Quizzes (quiz_name, access_code) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, quizName);
            pstmt.setString(2, code);
            pstmt.executeUpdate();
            System.out.println("Quiz created successfully! Access Code: " + code);
        } catch (SQLException e) {
            System.out.println("Error creating quiz: " + e.getMessage());
        }
    }

    // Method to add questions to a quiz
    public static void addQuestion(int quizId, String question, String[] options, String correctOption) {
        try (Connection conn = Database.connect()) {
            String query = "INSERT INTO Questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, quizId);
            pstmt.setString(2, question);
            pstmt.setString(3, options[0]);
            pstmt.setString(4, options[1]);
            pstmt.setString(5, options[2]);
            pstmt.setString(6, options[3]);
            pstmt.setString(7, correctOption);
            pstmt.executeUpdate();
            System.out.println("Question added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding question: " + e.getMessage());
        }
    }

    // Admin menu
    public static void adminMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Admin Panel");
            System.out.println("1. Create Quiz");
            System.out.println("2. Add Question");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Quiz Name: ");
                    String quizName = scanner.nextLine();
                    createQuiz(quizName);
                }
                case 2 -> {
                    System.out.print("Enter Quiz ID: ");
                    int quizId = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.print("Enter Question: ");
                    String question = scanner.nextLine();
                    String[] options = new String[4];
                    for (int i = 0; i < 4; i++) {
                        System.out.print("Option " + (char) ('A' + i) + ": ");
                        options[i] = scanner.nextLine();
                    }   System.out.print("Enter Correct Option (A/B/C/D): ");
                    String correctOption = scanner.nextLine();
                    addQuestion(quizId, question, options, correctOption);
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }
}
