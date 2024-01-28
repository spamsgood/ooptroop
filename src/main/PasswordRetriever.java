import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PasswordRetriever {

    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    // Method to retrieve and display password information based on site or username
    public void retrievePassword(String input, String searchType) {
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
                    System.out.println("Website: " + retrievedSite);
                    System.out.println("Username: " + retrievedUsername);
                    System.out.println("Encrypted Password: " + retrievedEncryptedPassword);
                    System.out.println("-----------------------------------------");
                }

                if (!found) {
                    System.out.println("No password entry found for the given " + searchType + ": " + input);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

        // Retrieve and display the password and website
        passwordRetriever.retrievePassword(input, searchType);

        scanner.close();
    }
}
