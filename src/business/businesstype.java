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
            System.out.println("1. ADD BUSINESS");
            System.out.println("2. VIEW BUSINESSES");
            System.out.println("3. UPDATE BUSINESS");
            System.out.println("4. DELETE BUSINESS");
            System.out.println("5. EXIT");

            System.out.print("Enter Action: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            int action = scanner.nextInt();

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
        System.out.print("Business Name: ");
        String b_name = sc.nextLine();
        if (b_name.isEmpty()) {
            System.out.println("Business name cannot be empty.");
            return;
        }

        System.out.print("Business Type: ");
        String b_type = sc.nextLine();
        if (b_type.isEmpty()) {
            System.out.println("Business type cannot be empty.");
            return;
        }

        System.out.print("Business Address: ");
        String b_address = sc.nextLine();
        if (b_address.isEmpty()) {
            System.out.println("Business address cannot be empty.");
            return;
        }

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

        System.out.print("Enter Business ID to Update: ");
        while (!input.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid Business ID.");
            input.next();
        }
        int b_id = input.nextInt();

        if (!isBusinessExists(b_id, conf)) {
            System.out.println("Business with ID " + b_id + " does not exist.");
            return;
        }

        input.nextLine();  

        System.out.print("Enter new Business Name: ");
        String newBName = input.nextLine();
        if (newBName.isEmpty()) {
            System.out.println("Business name cannot be empty.");
            return;
        }

        System.out.print("Enter new Business Type: ");
        String newBType = input.nextLine();
        if (newBType.isEmpty()) {
            System.out.println("Business type cannot be empty.");
            return;
        }

        System.out.print("Enter new Business Address: ");
        String newBAddress = input.nextLine();
        if (newBAddress.isEmpty()) {
            System.out.println("Business address cannot be empty.");
            return;
        }

        String updateQuery = "UPDATE tbl_businesstype SET b_name = ?, b_type = ?, b_address = ? WHERE b_id = ?";
        conf.updateRecord(updateQuery, newBName, newBType, newBAddress, b_id);
    }

    public void deleteBusiness() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Business ID to Delete: ");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid Business ID.");
            sc.next();
        }
        int b_id = sc.nextInt();

        if (!isBusinessExists(b_id, conf)) {
            System.out.println("Business with ID " + b_id + " does not exist.");
            return;
        }

        String deleteQuery = "DELETE FROM tbl_businesstype WHERE b_id = ?";
        conf.deleteRecord(deleteQuery, b_id);
    }

    private boolean isBusinessExists(int b_id, config conf) {
        String query = "SELECT COUNT(*) FROM tbl_businesstype WHERE b_id = ?";
        double count = conf.getSingleValue(query, b_id);
        return count > 0;
    }
}
