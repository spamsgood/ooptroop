import java.sql.*;
import java.util.Scanner;
import javax.crypto.SecretKey;

public class PasswordRetriever {

    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";
    private static final String TEST_RECOVERY_CODE = "MwpHq2KIJ7OxVhEacH0/5Q==";

    public void retrievePassword(String input, String searchType, SecretKey key) {
        String sql = "SELECT Site, Username, EncryptedPassword FROM Passwords WHERE " + searchType + " = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, input);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    String retrievedSite = rs.getString("Site");
                    String retrievedUsername = rs.getString("Username");
                    String retrievedEncryptedPassword = rs.getString("EncryptedPassword");

                    String decryptedPassword = CryptoUtils.decrypt(retrievedEncryptedPassword, key);

                    System.out.println("Website: " + retrievedSite);
                    System.out.println("Username: " + retrievedUsername);
                    System.out.println("Decrypted Password: " + decryptedPassword);
                    System.out.println("-----------------------------------------");
                }

                if (!found) {
                    System.out.println("No password entry found for the given " + searchType + ": " + input);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL exception during password retrieval: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during password retrieval: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordRetriever passwordRetriever = new PasswordRetriever();

        System.out.println("Search by (1) Username or (2) Site? Enter 1 or 2:");
        String choice = scanner.nextLine();

        String searchType;
        String input;
        if ("1".equals(choice)) {
            searchType = "Username";
            System.out.println("Enter Username:");
            input = scanner.nextLine();
        } else {
            searchType = "Site";
            System.out.println("Enter Site:");
            input = scanner.nextLine();
        }

        // Master password verification
        int masterPasswordAttempts = 0;
        while (masterPasswordAttempts < 3) {
            System.out.println("Enter Master Password:");
            String enteredMasterPassword = scanner.nextLine();

            if (checkMasterPassword(enteredMasterPassword)) {
                // Proceed with password retrieval
                SecretKey key = CryptoUtils.deriveKeyFromRecoveryCode(TEST_RECOVERY_CODE);
                passwordRetriever.retrievePassword(input, searchType, key);
                break;
            } else {
                System.out.println("Incorrect Master Password.");
                masterPasswordAttempts++;
            }
        }

        if (masterPasswordAttempts >= 3) {
            // Call DeleteAllData method without confirmation check
            DeleteAllData.forceDeleteAllData();
        }

        scanner.close();
    }

    private static boolean checkMasterPassword(String enteredPassword) {
        String sql = "SELECT EncryptedMasterPassword, RecoveryCode FROM Users WHERE Username = 'admin'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedEncryptedPassword = rs.getString("EncryptedMasterPassword");
                    String storedRecoveryCode = rs.getString("RecoveryCode");
                    SecretKey key = CryptoUtils.deriveKeyFromRecoveryCode(storedRecoveryCode);
                    String decryptedPassword = CryptoUtils.decrypt(storedEncryptedPassword, key);
                    return decryptedPassword.equals(enteredPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL exception during password retrieval: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during password retrieval: " + e.getMessage());
        }
        return false;
    }
}
