import java.sql.*;
import java.util.*;

public class TeacherPanel {

    // Display the teacher menu
    public static void teacherMenu(int userId) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("===== Teacher Panel =====");
                System.out.println("1. Create Quiz");
                System.out.println("2. View All Quizzes");
                System.out.println("3. View Student Results");
                System.out.println("4. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> createQuiz(userId);
                    case 2 -> viewAllQuizzes(userId);
                    case 3 -> viewStudentResults();
                    case 4 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    // Create a quiz
    public static void createQuiz(int userId) {
        try (Scanner scanner = new Scanner(System.in); Connection conn = Database.connect()) {
            System.out.print("Enter Quiz Name: ");
            String quizName = scanner.nextLine();

            String insertQuiz = "INSERT INTO Quizzes (quiz_name, user_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuiz, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, quizName);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int quizId = -1;
            if (rs.next()) {
                quizId = rs.getInt(1);
            }

            while (true) {
                System.out.print("Enter Question Text: ");
                String questionText = scanner.nextLine();
                System.out.print("Option A: ");
                String optionA = scanner.nextLine();
                System.out.print("Option B: ");
                String optionB = scanner.nextLine();
                System.out.print("Option C: ");
                String optionC = scanner.nextLine();
                System.out.print("Option D: ");
                String optionD = scanner.nextLine();
                System.out.print("Correct Option (A/B/C/D): ");
                String correctOption = scanner.nextLine();

                String insertQuestion = "INSERT INTO Questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertQuestion);
                pstmt.setInt(1, quizId);
                pstmt.setString(2, questionText);
                pstmt.setString(3, optionA);
                pstmt.setString(4, optionB);
                pstmt.setString(5, optionC);
                pstmt.setString(6, optionD);
                pstmt.setString(7, correctOption);
                pstmt.executeUpdate();

                System.out.print("Add another question? (yes/no): ");
                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                    break;
                }
            }

            System.out.println("Quiz created successfully with ID: " + quizId);
        } catch (SQLException e) {
            System.out.println("Error creating quiz: " + e.getMessage());
        }
    }

    // View all quizzes created by the teacher
    public static void viewAllQuizzes(int userId) {
        try (Connection conn = Database.connect()) {
            String query = "SELECT quiz_id, quiz_name FROM Quizzes WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("===== All Quizzes =====");
            while (rs.next()) {
                System.out.println("Quiz ID: " + rs.getInt("quiz_id") + " - Name: " + rs.getString("quiz_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching quizzes: " + e.getMessage());
        }
    }

    // View student results for a specific quiz
    public static void viewStudentResults() {
        try (Scanner scanner = new Scanner(System.in); Connection conn = Database.connect()) {
            System.out.print("Enter Quiz ID to view results: ");
            int quizId = scanner.nextInt();

            String query = "SELECT user_id, score FROM Results WHERE quiz_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, quizId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("===== Student Results =====");
            while (rs.next()) {
                System.out.println("Student ID: " + rs.getInt("user_id") + " - Score: " + rs.getInt("score"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching results: " + e.getMessage());
        }
    }
}
