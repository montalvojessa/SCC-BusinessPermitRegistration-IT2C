package business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Permit {
    public void permit() {
        Scanner scanner = new Scanner(System.in);
        String response;
        Permit pr = new Permit();

        do {
            System.out.println("==================================");
            System.out.println("=== PERMIT PANEL ===");
            System.out.println("==================================");
            System.out.println("1. ADD PERMIT");
            System.out.println("2. VIEW PERMIT");
            System.out.println("3. UPDATE PERMIT");
            System.out.println("4. DELETE PERMIT");
            System.out.println("5. EXIT");

            System.out.print("Enter Action: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            int action = scanner.nextInt();

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

            System.out.print("Do you want to make another transaction? (yes/no): ");
            response = scanner.next();

        } while (response.equalsIgnoreCase("yes"));
    }

    private void addPermit() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        Owner o = new Owner();
        o.viewOwner();

        System.out.print("Select ID of the Owner: ");
        int o_id = sc.nextInt();
        String osql = "SELECT COUNT(*) FROM tbl_owner WHERE o_id = ?";
        while (conf.getSingleValue(osql, o_id) == 0) {
            System.out.print("Owner ID does not exist. Please try again: ");
            o_id = sc.nextInt();
        }

        businesstype bt = new businesstype();
        bt.viewBusiness();
        System.out.print("Select ID of the Business Type: ");
        int b_id = sc.nextInt();
        String bsql = "SELECT COUNT(*) FROM tbl_businesstype WHERE b_id = ?";
        while (conf.getSingleValue(bsql, b_id) == 0) {
            System.out.print("Business ID does not exist. Please try again: ");
            b_id = sc.nextInt();
        }

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

    private void viewPermit() {
        String query = "SELECT p.permit_id, o.o_fname, o.o_lname, b.b_name, b.b_type, p.issue_date, p.p_status " +
                       "FROM tbl_permit p " +
                       "JOIN tbl_owner o ON p.o_id = o.o_id " +
                       "JOIN tbl_businesstype b ON p.b_id = b.b_id";

        String[] headers = {"Permit ID", "Owner First Name", "Owner Last Name", "Business Name", "Business Type", "Issue Date", "Status"};
        String[] columns = {"permit_id", "o_fname", "o_lname", "b_name", "b_type", "issue_date", "p_status"};

        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    private void updatePermit() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Permit ID to update: ");
        int permit_id = sc.nextInt();

        String checkPermitSql = "SELECT COUNT(*) FROM tbl_permit WHERE permit_id = ?";
        while (conf.getSingleValue(checkPermitSql, permit_id) == 0) {
            System.out.print("Permit ID does not exist. Please try again: ");
            permit_id = sc.nextInt();
        }

        System.out.print("Enter new Status (e.g., REGISTERED, PENDING): ");
        String newStatus = sc.next();
        
        String updateQuery = "UPDATE tbl_permit SET p_status = ? WHERE permit_id = ?";
        conf.updateRecord(updateQuery, newStatus, permit_id);

        System.out.println("Permit status updated successfully.");
    }

    private void deletePermit() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Permit ID to delete: ");
        int permit_id = sc.nextInt();

        String checkPermitSql = "SELECT COUNT(*) FROM tbl_permit WHERE permit_id = ?";
        while (conf.getSingleValue(checkPermitSql, permit_id) == 0) {
            System.out.print("Permit ID does not exist. Please try again: ");
            permit_id = sc.nextInt();
        }

        String deleteQuery = "DELETE FROM tbl_permit WHERE permit_id = ?";
        conf.deleteRecord(deleteQuery, permit_id);

        System.out.println("Permit deleted successfully.");
    }
}
