package business;

import java.util.Scanner;

public class Business {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String response;
        boolean exit = true;

        do {
            System.out.println("------------------------------------------------------");
            System.out.println("-- WELCOME TO BUSINESS PERMIT AND REGISTRATION --");
            System.out.println("------------------------------------------------------");
            System.out.println("-----------------");
            System.out.println("-- MAIN MENU --");
            System.out.println("-----------------");
            System.out.println("1. OWNER");
            System.out.println("2. BUSINESS TYPE");
            System.out.println("3. PERMIT");
            System.out.println("4. EXIT");

            System.out.print("What would you like to do? Choose 1-4: ");
            int action = validateInput(scanner, 1, 4);

            switch (action) {
                case 1:
                    System.out.println("Owner menu selected. Let's proceed.");
                    Owner o = new Owner();
                    o.owner();
                    break;

                case 2:
                    System.out.println("Business Type menu selected. Redirecting...");
                    businesstype bt = new businesstype();
                    bt.type();
                    break;

                case 3:
                    System.out.println("Permit menu selected. Redirecting...");
                    Permit p = new Permit();
                    p.permit();
                    break;

                case 4:
                    System.out.print("Are you sure you want to exit? Type 'yes' to confirm: ");
                    response = getValidatedYesOrNo(scanner);
                    if (response.equalsIgnoreCase("yes")) {
                        System.out.println("Thank you for using the program. Goodbye!");
                        exit = false;
                    } else {
                        System.out.println("Glad to have you back! Returning to the main menu.");
                    }
                    break;
            }
        } while (exit);

        scanner.close();
    }

    private static int validateInput(Scanner scanner, int min, int max) {
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.print("Oops! You Input an Invalid Number. Try again (choose between " + min + " and " + max + "): ");
                }
            } else {
                System.out.print("You Input A Letter or Word instead of a number. Please try again: ");
                scanner.next();
            }
        }
    }

    private static String getValidatedYesOrNo(Scanner scanner) {
        String input;
        while (true) {
            input = scanner.next().trim().toLowerCase();
            if (input.equals("yes") || input.equals("no")) {
                return input;
            } else {
                System.out.print("Invalid input. Please type 'yes' or 'no': ");
            }
        }
    }
}
