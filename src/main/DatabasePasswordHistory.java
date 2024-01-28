package src.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*public class DatabasePasswordHistory {

    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    // Method to update the user's password
    public void updatePassword(Connection conn, int userId, String newEncryptedPassword) {
        String sql = "UPDATE Passwords SET PreviousEncryptedPassword = EncryptedPassword, EncryptedPassword = ? WHERE UserID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEncryptedPassword);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Password update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Establishes a connection to the database
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        DatabasePasswordHistory pm = new DatabasePasswordHistory();
        try (Connection conn = pm.connect()) {
            if (conn != null) {
                // Example usage of the updatePassword method
                // Replace 1 with the actual user ID and "new_encrypted_password" with the encrypted password
                pm.updatePassword(conn, 1, "new_encrypted_password");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
/*