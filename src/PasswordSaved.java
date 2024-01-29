import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;

public class PasswordSaved {
    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    // Hardcoded recovery code for testing purposes
    private static final String TEST_RECOVERY_CODE = "MwpHq2KIJ7OxVhEacH0/5Q==";

    // Method to save a new password entry
    public void savePassword(String site, String username, String plainPassword) {
        try {
            // Use the hardcoded test recovery code for encryption
            SecretKey key = CryptoUtils.deriveKeyFromRecoveryCode(TEST_RECOVERY_CODE);
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
        System.out.println("User authentication automatically accepted for debugging purposes.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordSaved passwordSaved = new PasswordSaved();

        // Site validation
        Pattern sitePattern = Pattern.compile("^www\\.[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,}$");
        String site;
        do {
            System.out.println("Enter Site (in format www.sitename.extension):");
            site = scanner.nextLine();
        } while (!sitePattern.matcher(site).matches());

        System.out.println("Enter Username:");
        String username = scanner.nextLine();

        // Password validation
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$");
        String plainPassword;
        do {
            System.out.println("Enter Password (at least 6 characters, including both letters and numbers):");
            plainPassword = scanner.nextLine();
        } while (!passwordPattern.matcher(plainPassword).matches());

        // Save the new password using the hardcoded test recovery code
        passwordSaved.savePassword(site, username, plainPassword);

        scanner.close();
    }
}
