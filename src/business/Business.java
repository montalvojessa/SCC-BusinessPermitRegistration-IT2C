
package business;

import java.util.Scanner;

public class Business {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        String response ;
        boolean exit = true;
        do{
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
        
        System.out.print("Enter Action: ");
        int action = scanner.nextInt();
        
        switch(action){
            
            case 1:
                
                Owner o = new Owner();
                o.owner();
                
                 break;
                 
            case 2:
                businesstype bt = new businesstype();
                bt.type();
                break;
                
            case 3:
                Permit p = new Permit();
                p.permit();
                break;
                
            case 4:
                System.out.print("Exit Selected... type 'yes' to continue:  ");
                response = scanner.next();
                if(response.equalsIgnoreCase("yes")){
                    System.out.println("-- THANK YOU FOR USING THE PROGRAM --");
                    exit = false;       
                }
                
                break;
                
            
            
        }

    }while(exit);
    
}
}
