import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class DeleteAllData {

    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to delete ALL users and their data? This cannot be undone. Type 'YES' to confirm:");
        String confirmation = scanner.nextLine();

        if ("YES".equalsIgnoreCase(confirmation)) {
            deleteAllData();
            System.out.println("All users and their data have been deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }

        scanner.close();
    }

    private static void deleteAllData() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Delete all data from Passwords table
            String sqlDeletePasswords = "DELETE FROM Passwords";
            stmt.executeUpdate(sqlDeletePasswords);

            // Delete all data from Users table
            String sqlDeleteUsers = "DELETE FROM Users";
            stmt.executeUpdate(sqlDeleteUsers);

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
