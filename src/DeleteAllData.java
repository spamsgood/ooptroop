import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner; // Import statement for Scanner

public class DeleteAllData {

    private static final String URL = "jdbc:mysql://localhost:3306/PasswordManagerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd";

    public static void main(String[] args) {
        // Method call to allow for deletion with confirmation when running this class directly
        deleteAllDataWithConfirmation();
    }

    //method to force delete all data without confirmation
    public static void forceDeleteAllData() {
        deleteAllData();
        System.out.println("All userdata and passwords have been deleted because you entered the Master Password wrong 3 times. :'(");
    }

    // Method to delete all data with a confirmation prompt
    public static void deleteAllDataWithConfirmation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to delete ALL users and their data? This cannot be undone. Type 'YES' to confirm:");
        String confirmation = scanner.nextLine();
        scanner.close();

        if ("YES".equalsIgnoreCase(confirmation)) {
            deleteAllData();
            System.out.println("All users and their data have been deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Private method to handle the actual deletion process
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
