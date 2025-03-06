package com.Suhail.BankingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "root@123";


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("Driver loaded !!");
        } catch (ClassNotFoundException e) {
//            System.out.println("Driver cant loaded !!"+e.getMessage());
            e.printStackTrace();
        }

        try {
            Connection connection = DriverManager.getConnection(url,username,password);
//            System.out.println("Connection estb. !!");

            Scanner scanner = new Scanner(System.in);
            User user = new User(connection,scanner);
            Accounts accounts = new Accounts(connection,scanner);
            AccountManager accountManager = new AccountManager(connection,scanner);

            String email;
            long account_number;

            while (true){
                System.out.println("=======WELCOME TO THE BANKING SYSTEM=======");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged In!!");
                            if (!accounts.account_exist(email)) {
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                System.out.print("Enter your choice: ");
                                if (scanner.nextInt() == 1){
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully!!");
                                    System.out.println("Your Account Number is: "+account_number);
                                }
                                else {
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);
                            int choice1 = 0;
                            while (choice1 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter Your Choice: ");

                                choice1 = scanner.nextInt();

                                switch (choice1){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.check_balance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Please Enter Valid Choice!!");
                                        break;
                                }
                            }
                        }
                        else {
                            System.out.println("Incorrect Email or Password!!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!");
                        System.out.println("Existing System....!!");
                        return;
                    default:
                        System.out.println("Please Enter Valid Choice !!");
                        break;
                }
            }

        } catch (SQLException e) {
//            System.out.println("conn. failed !!"+e.getMessage());
            e.printStackTrace();
        }
    }
}
