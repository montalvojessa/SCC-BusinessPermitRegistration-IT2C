package business;

import java.util.Scanner;

public class Owner {

    public void owner() {
        Scanner scanner = new Scanner(System.in);
        String response = null;
        Owner o = new Owner();

         do {
        System.out.println("------------------------------");
        System.out.println("-- OWNER'S PANEL");
        System.out.println("------------------------------");

        int action = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("1. ADD OWNER");
            System.out.println("2. VIEW OWNER");
            System.out.println("3. UPDATE OWNER");
            System.out.println("4. DELETE OWNER");
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
                o.addOwner();
                break;

            case 2:
                o.viewOwner();
                break;

            case 3:
                o.viewOwner();
                o.updateOwner();
                o.viewOwner();
                break;

            case 4:
                o.viewOwner();
                o.deleteOwner();
                o.viewOwner();
                break;

            case 5:
                System.out.println("Exiting Owner's Panel.");
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

    public void addOwner() {
        Scanner sc = new Scanner(System.in);

        System.out.println("------------------------");
        String o_fname = validateName(sc, "Owner's First Name");
        String o_lname = validateName(sc, "Owner's Last Name");
        String o_email = validateEmail(sc);

        String qry = "INSERT INTO tbl_owner (o_fname, o_lname, o_email) VALUES (?, ?, ?)";
        config conf = new config();
        conf.addRecord(qry, o_fname, o_lname, o_email);
    }

    public void viewOwner() {
        String query = "SELECT * FROM tbl_owner";
        String[] headers = {"ID", "Owner's First Name", "Owner's Last Name", "Owner's Email"};
        String[] columns = {"o_id", "o_fname", "o_lname", "o_email"};

        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    public void updateOwner() {
        Scanner input = new Scanner(System.in);
        config conf = new config();

        int o_id = validatePositiveInt(input, "Enter Owner's ID to Update");
        if (!isOwnerExists(o_id, conf)) {
            System.out.println("Owner with ID " + o_id + " does not exist.");
            return;
        }

        String newFirstName = validateName(input, "Enter new First Name");
        String newLastName = validateName(input, "Enter new Last Name");
        String newEmail = validateEmail(input);

        String updateQuery = "UPDATE tbl_owner SET o_fname = ?, o_lname = ?, o_email = ? WHERE o_id = ?";
        conf.updateRecord(updateQuery, newFirstName, newLastName, newEmail, o_id);
    }

    public void deleteOwner() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int o_id = validatePositiveInt(sc, "Enter Owner's ID to Delete");
        if (!isOwnerExists(o_id, conf)) {
            System.out.println("Owner with ID " + o_id + " does not exist.");
            return;
        }

        String deleteQuery = "DELETE FROM tbl_owner WHERE o_id = ?";
        conf.deleteRecord(deleteQuery, o_id);
    }

    private String validateName(Scanner sc, String prompt) {
        String name;
        do {
            System.out.print(prompt + ": ");
            name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
            } else if (!name.matches("[a-zA-Z ]+") || name.length() < 2) {
                System.out.println("Invalid name. Name must contain only letters and be at least 2 characters long.");
            } else {
                break;
            }
        } while (true);
        return name;
    }

    private String validateEmail(Scanner sc) {
        String email;
        do {
            System.out.print("Owner's Email: ");
            email = sc.nextLine().trim();
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Invalid email format. Please try again.");
            } else {
                break;
            }
        } while (true);
        return email;
    }

    private int validatePositiveInt(Scanner sc, String prompt) {
        int number;
        do {
            System.out.print(prompt + ": ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a positive integer.");
                sc.next();
            }
            number = sc.nextInt();
            if (number <= 0) {
                System.out.println("Number must be positive. Please try again.");
            } else {
                break;
            }
        } while (true);
        return number;
    }

    private boolean isOwnerExists(int o_id, config conf) {
        String query = "SELECT COUNT(*) FROM tbl_owner WHERE o_id = ?";
        double count = conf.getSingleValue(query, o_id);
        return count > 0;
    }
}
