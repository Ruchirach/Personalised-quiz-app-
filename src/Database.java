import java.sql.*;
import java.util.List;

public class Database {

    // ✅ Establish database connection
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:C:/Users/ruchi/OneDrive/Documents/Desktop/PersonalizedQuizApp/db/quizapp.db";
            conn = DriverManager.getConnection(url);
            System.out.println("✅ Database connection established.");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
        return conn;
    }

    // ✅ Save Quiz and associated Questions (with created_on timestamp)
    public static int saveQuiz(String title, String code, List<CreateQuizGUI.Question> questions) {
        int quizId = -1;
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            // ✅ Insert created_on using datetime('now')
            String insertQuiz = "INSERT INTO Quiz (quiz_name, access_code, user_id, created_on) VALUES (?, ?, ?, datetime('now'))";
            PreparedStatement quizStmt = conn.prepareStatement(insertQuiz, Statement.RETURN_GENERATED_KEYS);
            quizStmt.setString(1, title);
            quizStmt.setString(2, code);
            quizStmt.setInt(3, 1); // Replace with dynamic user_id if available
            quizStmt.executeUpdate();

            ResultSet rs = quizStmt.getGeneratedKeys();
            if (rs.next()) {
                quizId = rs.getInt(1);
            }

            // ✅ Insert questions
            String insertQ = "INSERT INTO Question (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement qStmt = conn.prepareStatement(insertQ);
            for (CreateQuizGUI.Question q : questions) {
                qStmt.setInt(1, quizId);
                qStmt.setString(2, q.question);
                qStmt.setString(3, q.a);
                qStmt.setString(4, q.b);
                qStmt.setString(5, q.c);
                qStmt.setString(6, q.d);
                qStmt.setString(7, q.correct);
                qStmt.addBatch();
            }

            qStmt.executeBatch();
            conn.commit();
            System.out.println("✅ Quiz and questions saved successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Error saving quiz: " + e.getMessage());
            e.printStackTrace();
        }

        return quizId;
    }

    // ✅ Update quiz status (start/end)
    public static boolean updateQuizStatus(String code, boolean isActive) {
        try (Connection conn = connect()) {
            String sql = "UPDATE Quiz SET isActive = ? WHERE access_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, isActive ? 1 : 0);
            stmt.setString(2, code);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to update quiz status: " + e.getMessage());
            return false;
        }
    }

    // ✅ Check if quiz exists
    public static boolean quizExists(String code) {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) FROM Quiz WHERE access_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Check if quiz is active
    public static boolean isQuizActive(String code) {
        try (Connection conn = connect()) {
            String sql = "SELECT isActive FROM Quiz WHERE access_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("isActive") == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Get Quiz ID by access code
    public static int getQuizIdByCode(String code) {
        try (Connection conn = connect()) {
            String sql = "SELECT quiz_id FROM Quiz WHERE access_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quiz_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ Save quiz result for a student (includes timestamp)
    public static void saveResult(int userId, int quizId, int score, int total) {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO Results (user_id, quiz_id, score, total, attempted_on) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, quizId);
            stmt.setInt(3, score);
            stmt.setInt(4, total);
            stmt.executeUpdate();
            System.out.println("✅ Result saved!");
        } catch (SQLException e) {
            System.out.println("❌ Failed to save result: " + e.getMessage());
        }
    }
}
