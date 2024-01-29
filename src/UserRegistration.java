import java.sql.*;
import java.util.Scanner;
import javax.crypto.SecretKey;

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

        String masterPassword;
        do {
            System.out.println("Enter New Master Password (at least 8 characters):");
            masterPassword = scanner.nextLine();
        } while (masterPassword.length() < 8);  // Basic password length check

        // Generate a recovery code for the user, which will be used to derive the encryption key.
        String recoveryCode = CryptoUtils.generateRecoveryCode();
        SecretKey key = CryptoUtils.deriveKeyFromRecoveryCode(recoveryCode);

        try {
            // Encrypt the master password using the derived key
            String encryptedMasterPassword = CryptoUtils.encrypt(masterPassword, key);

            String sql = "INSERT INTO Users (Username, EncryptedMasterPassword, RecoveryCode) VALUES (?, ?, ?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, encryptedMasterPassword);
                pstmt.setString(3, recoveryCode);
                pstmt.executeUpdate();
                System.out.println("User registered successfully. Please save this recovery code securely: " + recoveryCode);
            }
        } catch (Exception e) {
            System.out.println("Error encrypting Master Password: " + e.getMessage());
        }
    }
}
