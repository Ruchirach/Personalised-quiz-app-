import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    // ✅ Register a new user
    public static void registerUser(String username, String password, String role) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // 🔐 Optionally hash in future
            stmt.setString(3, role);
            stmt.executeUpdate();
            System.out.println("✅ Registration successful!");
        } catch (SQLException e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
        }
    }

    // ✅ Get user ID by username (used for quiz results, etc.)
    public static int getUserId(String username) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT id FROM Users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching user ID: " + e.getMessage());
        }
        return -1;
    }

    // ✅ Validate credentials for JavaFX Login GUI
    public static String validate(String username, String password) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT role FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // 🔐 Secure this in future
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
        }
        return null;
    }

    // 🧪 Console-based login support for manual testing
    public static boolean authenticate(String username, String password) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT role FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int userId = getUserId(username);
                System.out.println("✅ Logged in as " + role + " (User ID: " + userId + ")");
                return true;
            } else {
                System.out.println("❌ Invalid credentials.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Authentication error: " + e.getMessage());
        }
        return false;
    }

    // ⚠️ No main() required — used only for GUI logic
}
