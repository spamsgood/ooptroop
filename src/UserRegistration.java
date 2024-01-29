import java.sql.*;
import java.util.Scanner;
// import javax.crypto.SecretKey; // Commented out as it's not needed for now

public class UserRegistration {
    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "P@ssw0rd";

    public static void main(String[] args) {
        UserRegistration registration = new UserRegistration();
        registration.registerNewUser();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }

    public void registerNewUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter New Username:");
        String username = scanner.nextLine();
        System.out.println("Enter New Master Password:");
        String masterPassword = scanner.nextLine();

        /*
        // The following code is commented out for bypassing the recovery code requirement.
        // Generate a recovery code for the user, which will be used to derive the encryption key.
        // String recoveryCode = CryptoUtils.generateRecoveryCode();
        // SecretKey key = CryptoUtils.deriveKeyFromRecoveryCode(recoveryCode);

        // Encrypt the master password using the derived key
        // String encryptedMasterPassword = CryptoUtils.encrypt(masterPassword, key);
        */

        // Placeholder for encrypted password - using plain master password for debugging purposes
        String encryptedMasterPassword = masterPassword; // For debugging

        System.out.println("User authentication automatically accepted for debugging purposes.");

        String sql = "INSERT INTO Users (Username, EncryptedMasterPassword, RecoveryCode) VALUES (?, ?, 'DEBUG')";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedMasterPassword); // Storing plain password for debugging
            pstmt.executeUpdate();
            System.out.println("User registered successfully. Note: Recovery code is not required for debugging.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
