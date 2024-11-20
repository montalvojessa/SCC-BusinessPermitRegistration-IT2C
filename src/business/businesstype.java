package business;

import java.util.Scanner;

public class businesstype {
    public void type() {
        Scanner scanner = new Scanner(System.in);
        String response;
        businesstype bt = new businesstype();

        do {
            System.out.println("==================================");
            System.out.println("=== BUSINESS TYPE PANEL ===");
            System.out.println("==================================");

            int action = 0;
            boolean validInput = false;

            while (!validInput) {
                System.out.println("1. ADD BUSINESS");
                System.out.println("2. VIEW BUSINESSES");
                System.out.println("3. UPDATE BUSINESS");
                System.out.println("4. DELETE BUSINESS");
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
                    bt.addBusiness();
                    break;
                case 2:
                    bt.viewBusiness();
                    break;
                case 3:
                    bt.viewBusiness();
                    bt.updateBusiness();
                    bt.viewBusiness();
                    break;
                case 4:
                    bt.viewBusiness();
                    bt.deleteBusiness();
                    bt.viewBusiness();
                    break;
                case 5:
                    System.out.println("Exiting Business Type Panel.");
                    return;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }

            System.out.print("Do you want to make another transaction? (yes/no): ");
            response = scanner.next();

        } while (response.equalsIgnoreCase("yes"));
    }

    public void addBusiness() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.println("=======================");

        String b_name = validateName(sc, "Business Name");
        String b_type = validateName(sc, "Business Type");
        String b_address = validateAddress(sc, "Business Address");

        String qry = "INSERT INTO tbl_businesstype (b_name, b_type, b_address) VALUES (?, ?, ?)";
        conf.addRecord(qry, b_name, b_type, b_address);
    }

    public void viewBusiness() {
        String query = "SELECT * FROM tbl_businesstype";
        String[] headers = {"ID", "Business Name", "Business Type", "Business Address"};
        String[] columns = {"b_id", "b_name", "b_type", "b_address"};

        config conf = new config();
        conf.viewRecords(query, headers, columns);
    }

    public void updateBusiness() {
        Scanner input = new Scanner(System.in);
        config conf = new config();

        int b_id = validatePositiveInt(input, "Enter Business ID to Update");
        if (!isBusinessExists(b_id, conf)) {
            System.out.println("Error: Business with ID " + b_id + " does not exist.");
            return;
        }

        String newBName = validateName(input, "Enter new Business Name");
        String newBType = validateName(input, "Enter new Business Type");
        String newBAddress = validateAddress(input, "Enter new Business Address");

        String updateQuery = "UPDATE tbl_businesstype SET b_name = ?, b_type = ?, b_address = ? WHERE b_id = ?";
        conf.updateRecord(updateQuery, newBName, newBType, newBAddress, b_id);
    }

    public void deleteBusiness() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int b_id = validatePositiveInt(sc, "Enter Business ID to Delete");
        if (!isBusinessExists(b_id, conf)) {
            System.out.println("Error: Business with ID " + b_id + " does not exist.");
            return;
        }

        String deleteQuery = "DELETE FROM tbl_businesstype WHERE b_id = ?";
        conf.deleteRecord(deleteQuery, b_id);
    }

    private String validateName(Scanner sc, String prompt) {
        String name;
        do {
            System.out.print(prompt + ": ");
            name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
            } else if (!name.matches("[a-zA-Z ]{2,}")) {
                System.out.println("Invalid name. Name must contain only letters, spaces, and be at least 2 characters long.");
            } else {
                break;
            }
        } while (true);
        return name;
    }

    private String validateAddress(Scanner sc, String prompt) {
        String address;
        do {
            System.out.print(prompt + ": ");
            address = sc.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Address cannot be empty. Please try again.");
            } else if (address.length() < 5) {
                System.out.println("Invalid address. Address must be at least 5 characters long.");
            } else {
                break;
            }
        } while (true);
        return address;
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

    private boolean isBusinessExists(int b_id, config conf) {
        String query = "SELECT COUNT(*) FROM tbl_businesstype WHERE b_id = ?";
        double count = conf.getSingleValue(query, b_id);
        return count > 0;
    }
}
