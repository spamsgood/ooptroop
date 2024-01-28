import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import javax.crypto.SecretKey;

public class PasswordSaved {
    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    // Method to save a new password entry
    public void savePassword(String site, String username, String plainPassword, String recoveryCode) {
        try {
            // Encrypt the password using the recovery code
            SecretKey key = CryptoUtils.deriveKeyFromRecoveryCode(recoveryCode);
            String encryptedPassword = CryptoUtils.encrypt(plainPassword, key);

            String sql = "INSERT INTO Passwords (Site, Username, EncryptedPassword, CreatedAt, UpdatedAt) VALUES (?, ?, ?, NOW(), NOW())";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, site);
                pstmt.setString(2, username);
                pstmt.setString(3, encryptedPassword);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Password saved successfully.");
                } else {
                    System.out.println("Failed to save the password.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error encrypting the password: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordSaved passwordSaved = new PasswordSaved();

        System.out.println("Enter Site:");
        String site = scanner.nextLine();

        System.out.println("Enter Username:");
        String username = scanner.nextLine();

        System.out.println("Enter Password:");
        String plainPassword = scanner.nextLine();

        System.out.println("Enter your Recovery Code:");
        String recoveryCode = scanner.nextLine();

        // Save the new password
        passwordSaved.savePassword(site, username, plainPassword, recoveryCode);

        scanner.close();
    }
}
