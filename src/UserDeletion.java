import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UserDeletion {

    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    public void deleteUser(String username) {
        String sql = "DELETE FROM Passwords WHERE Username = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("All password entries for Username: " + username + " deleted successfully.");
            } else {
                System.out.println("No matching password entries found for Username: " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL exception during user deletion: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDeletion userDeletion = new UserDeletion();

        System.out.println("Enter Username to delete all entries:");
        String username = scanner.nextLine();

        userDeletion.deleteUser(username);

        scanner.close();
    }
}
