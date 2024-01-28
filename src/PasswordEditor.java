import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PasswordEditor {

    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    // Method to update password information
    public void updatePassword(String username, String newSite, String newUsername, String newEncryptedPassword, boolean updateAll) {
        String sql;
        if (updateAll) {
            sql = "UPDATE Passwords SET Site = ?, Username = ?, EncryptedPassword = ? WHERE Username = ?";
        } else {
            sql = "UPDATE Passwords SET EncryptedPassword = ? WHERE Username = ?";
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (updateAll) {
                pstmt.setString(1, newSite);
                pstmt.setString(2, newUsername);
                pstmt.setString(3, newEncryptedPassword);
                pstmt.setString(4, username);
            } else {
                pstmt.setString(1, newEncryptedPassword);
                pstmt.setString(2, username);
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password information updated successfully.");
            } else {
                System.out.println("Failed to update password information.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordEditor passwordEditor = new PasswordEditor();

        System.out.println("Enter the Username for the record to be updated:");
        String username = scanner.nextLine();

        System.out.println("Do you want to update (1) Just the password or (2) Other details as well? Enter 1 or 2:");
        String choice = scanner.nextLine();

        String newSite = "";
        String newUsername = "";
        String newEncryptedPassword;

        boolean updateAll = "2".equals(choice);
        if (updateAll) {
            System.out.println("Enter new Site:");
            newSite = scanner.nextLine();

            System.out.println("Enter new Username:");
            newUsername = scanner.nextLine();
        }

        System.out.println("Enter new Encrypted Password:");
        newEncryptedPassword = scanner.nextLine();

        // Update the password information
        passwordEditor.updatePassword(username, newSite, newUsername, newEncryptedPassword, updateAll);

        scanner.close();
    }
}
