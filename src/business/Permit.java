package business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Permit {
    public void permit() {
        Scanner scanner = new Scanner(System.in);
        String response = null;
        Permit pr = new Permit();
        
        do {
        System.out.println("==================================");
        System.out.println("=== PERMIT PANEL ===");
        System.out.println("==================================");

        int action = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("1. ADD PERMIT");
            System.out.println("2. VIEW PERMIT");
            System.out.println("3. UPDATE PERMIT");
            System.out.println("4. DELETE PERMIT");
            System.out.println("5. EXIT");
            System.out.print("Enter Action: ");

            if (scanner.hasNextInt()) {
                action = scanner.nextInt();
                if (action >= 1 && action <= 5) {
                    validInput = true; 
                } else {
                    System.out.println("Invalid choice. Please choose a number between 1 and 5.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); 
            }
        }

        switch (action) {
            case 1:
                pr.addPermit();
                pr.viewPermit();
                break;
            case 2:
                pr.viewPermit();
                break;
            case 3:
                pr.updatePermit();
                break;
            case 4:
                pr.deletePermit();
                break;
            case 5:
                System.out.println("Exiting Permit Panel.");
                return;
            default:
                System.out.println("Invalid option. Please choose again.");
        }

        boolean validResponse = false;
        while (!validResponse) {
            System.out.print("Do you want to make another transaction? (yes/no): ");
            response = scanner.next().trim().toLowerCase();
            if (response.equals("yes") || response.equals("no")) {
                validResponse = true; 
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

    } while (response.equals("yes"));
    }

    public void addPermit() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("Owners List:");
        Owner o = new Owner();
        o.viewOwner();

        int o_id = validatePositiveInt(sc, "Owner ID", conf, "tbl_owner", "o_id");

        System.out.println("Business Types List:");
        businesstype bt = new businesstype();
        bt.viewBusiness();

        int b_id = validatePositiveInt(sc, "Business Type ID", conf, "tbl_businesstype", "b_id");

        String getBusinessDetailsQuery = "SELECT b_type, b_address FROM tbl_businesstype WHERE b_id = ?";
        Object[] businessDetails = conf.getMultipleValues(getBusinessDetailsQuery, b_id);

        if (businessDetails == null || businessDetails.length < 2) {
            System.out.println("Error retrieving business details. Please ensure the business ID is valid.");
            return;
        }
        String b_type = (String) businessDetails[0];
        String b_address = (String) businessDetails[1];

        LocalDate currDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String issue_date = currDate.format(formatter);

        String p_status = "REGISTERED";

        String permitInsertQuery = "INSERT INTO tbl_permit (o_id, b_id, issue_date, p_status, b_type, b_address) VALUES (?, ?, ?, ?, ?, ?)";
        conf.addRecord(permitInsertQuery, o_id, b_id, issue_date, p_status, b_type, b_address);

        System.out.println("Permit added successfully with status: " + p_status);
    }

    public void viewPermit() {
        String query = "SELECT p.permit_id, o.o_fname, o.o_lname, b.b_name, b.b_type, p.issue_date, p.p_status " +
                       "FROM tbl_permit p " +
                       "JOIN tbl_owner o ON p.o_id = o.o_id " +
                       "JOIN tbl_businesstype b ON p.b_id = b.b_id";

        String[] headers = {"Permit ID", "Owner First Name", "Owner Last Name", "Business Name", "Business Type", "Issue Date", "Status"};
        String[] columns = {"permit_id", "o_fname", "o_lname", "b_name", "b_type", "issue_date", "p_status"};

        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    public void updatePermit() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int permit_id = validatePositiveInt(sc, "Permit ID", conf, "tbl_permit", "permit_id");

        System.out.print("Enter new Status (e.g., REGISTERED, PENDING): ");
        String newStatus = validateStatus(sc, "Permit Status");

        String updateQuery = "UPDATE tbl_permit SET p_status = ? WHERE permit_id = ?";
        conf.updateRecord(updateQuery, newStatus, permit_id);

        System.out.println("Permit status updated successfully.");
    }

    public void deletePermit() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int permit_id = validatePositiveInt(sc, "Permit ID", conf, "tbl_permit", "permit_id");

        String deleteQuery = "DELETE FROM tbl_permit WHERE permit_id = ?";
        conf.deleteRecord(deleteQuery, permit_id);

        System.out.println("Permit deleted successfully.");
    }

    private int validatePositiveInt(Scanner sc, String fieldName, config conf, String tableName, String columnName) {
        int id;
        do {
            id = validateNumericInput(sc, fieldName);
            if (!isRecordExists(conf, tableName, columnName, id)) {
                System.out.println(fieldName + " does not exist. Please try again.");
            } else {
                break;
            }
        } while (true);
        return id;
    }

    private int validateNumericInput(Scanner sc, String fieldName) {
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number for " + fieldName + ".");
            sc.next();
        }
        int input = sc.nextInt();
        if (input <= 0) {
            System.out.println(fieldName + " must be a positive number. Please try again.");
            return validateNumericInput(sc, fieldName);
        }
        return input;
    }

    private String validateStatus(Scanner sc, String fieldName) {
        String input;
        do {
            input = sc.nextLine().trim().toUpperCase();
            if (input.matches("REGISTERED|PENDING|EXPIRED")) {
                break;
            } else {
                System.out.println("Invalid " + fieldName + ". Accepted values: REGISTERED, PENDING, EXPIRED.");
            }
        } while (true);
        return input;
    }

    private boolean isRecordExists(config conf, String tableName, String columnName, int id) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        return conf.getSingleValue(query, id) > 0;
    }
}
