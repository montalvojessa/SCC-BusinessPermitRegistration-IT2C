package business;

import java.util.Scanner;

public class Owner {
    public void owner() {
        Scanner scanner = new Scanner(System.in);
        String response;
        Owner o = new Owner();

        do {
            System.out.println("------------------------------");
            System.out.println("-- OWNER'S PANEL");
            System.out.println("------------------------------");
            System.out.println("1. ADD OWNER");
            System.out.println("2. VIEW OWNER");
            System.out.println("3. UPDATE OWNER");
            System.out.println("4. DELETE OWNER");
            System.out.println("5. EXIT");

            System.out.print("Enter Action: ");
            int action = scanner.nextInt();

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

            System.out.print("Do you want to make another transaction? (yes/no): ");
            response = scanner.next();

        } while (response.equalsIgnoreCase("yes"));
    }

    public void addOwner() {
        Scanner sc = new Scanner(System.in);

        System.out.println("------------------------");
        System.out.print("Owner's First Name: ");
        String o_fname = sc.next();
        if (o_fname.isEmpty()) {
            System.out.println("First Name cannot be empty.");
            return;
        }

        System.out.print("Owner's Last Name: ");
        String o_lname = sc.next();
        if (o_lname.isEmpty()) {
            System.out.println("Last Name cannot be empty.");
            return;
        }

        System.out.print("Owner's Email: ");
        String o_email = sc.next();
        if (!isValidEmail(o_email)) {
            System.out.println("Invalid email format.");
            return;
        }
      
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

        int o_id;
        do {
            System.out.print("Enter Owner's ID to Update: ");
            o_id = input.nextInt();
            if (!isOwnerExists(o_id, conf)) {
                System.out.println("Owner with ID " + o_id + " does not exist. Please enter a valid ID.");
            }
        } while (!isOwnerExists(o_id, conf)); 

        input.nextLine(); 

        System.out.print("Enter new First Name: ");
        String newFirstName = input.nextLine();
        if (newFirstName.isEmpty()) {
            System.out.println("First Name cannot be empty.");
            return;
        }

        System.out.print("Enter new Last Name: ");
        String newLastName = input.nextLine();
        if (newLastName.isEmpty()) {
            System.out.println("Last Name cannot be empty.");
            return;
        }

        System.out.print("Enter new Email: ");
        String newEmail = input.nextLine();
        if (!isValidEmail(newEmail)) {
            System.out.println("Invalid email format.");
            return;
        }

        String updateQuery = "UPDATE tbl_owner SET o_fname = ?, o_lname = ?, o_email = ? WHERE o_id = ?";
        conf.updateRecord(updateQuery, newFirstName, newLastName, newEmail, o_id);
    }

    public void deleteOwner() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        int o_id;
        do {
            System.out.print("Enter Owner's ID to Delete: ");
            o_id = sc.nextInt();
            if (!isOwnerExists(o_id, conf)) {
                System.out.println("Owner with ID " + o_id + " does not exist. Please enter a valid ID.");
            }
        } while (!isOwnerExists(o_id, conf)); 
        
        String deleteQuery = "DELETE FROM tbl_owner WHERE o_id = ?";
        conf.deleteRecord(deleteQuery, o_id);
    }

    private boolean isOwnerExists(int o_id, config conf) {
        String query = "SELECT COUNT(*) FROM tbl_owner WHERE o_id = ?";
        double count = conf.getSingleValue(query, o_id);
        return count > 0;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
