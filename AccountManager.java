package com.Suhail.BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter your Security Pin: ");
        String pin = scanner.nextLine();

        try {
        connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2,pin);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double curr_balance = resultSet.getDouble("balance");
                    if (amount<=curr_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";

                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);

                        int rawaffected = preparedStatement1.executeUpdate();
                        if(rawaffected>0){
                            System.out.println("Rs. "+amount+" Debit Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction failed !!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient balance !!");
                    }
                } else {
                        System.out.println("Invalid Pin !!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.setAutoCommit(true);
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter your Security Pin: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2,pin);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
//                    double curr_balance = resultSet.getDouble("balance");
//                    if (amount>=curr_balance){
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);

                        int rawaffected = preparedStatement1.executeUpdate();
                        if(rawaffected>0){
                            System.out.println("Rs. "+amount+" Credit Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction failed !!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
//                    }else {
//                        System.out.println("Insufficient balance!!");
//                    }
                } else {
                    System.out.println("Invalid Pin !!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long Sender_account_number) throws SQLException {

        scanner.nextLine();
        System.out.print("Enter Receiver account number: ");
        long receiverAccNum = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin: ");
        String pin = scanner.nextLine();



        try {
            connection.setAutoCommit(false);
            if (receiverAccNum != 0 && Sender_account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin=?");
                preparedStatement.setLong(1,Sender_account_number);
                preparedStatement.setString(2,pin);

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double curr_balance = resultSet.getDouble("balance");
                    if (amount<=curr_balance){
                               String withdrawQuery = "UPDATE accounts SET balance = balance-? WHERE account_number = ?";
                               String depositQuery = "UPDATE accounts SET balance = balance+? WHERE account_number = ?";

                              PreparedStatement depositStatement = connection.prepareStatement(depositQuery);
                              PreparedStatement withdrawStatement = connection.prepareStatement(withdrawQuery);
                              depositStatement.setDouble(1,amount);
                              depositStatement.setLong(2, receiverAccNum);
                              withdrawStatement.setDouble(1, amount);
                              withdrawStatement.setLong(2, Sender_account_number);
                              int rawsaffectedWithdraw = withdrawStatement.executeUpdate();
                              int rawsaffectedDeposited = depositStatement.executeUpdate();

                              if (rawsaffectedWithdraw>0 && rawsaffectedDeposited>0){

                                  connection.commit();
                                  System.out.println("transaction successfull !!");
                                  System.out.println("Rs. "+amount+" Transferred successfully!!");
                                  connection.setAutoCommit(true);
                              }
                              else {
                                  connection.rollback();
                                  System.out.println("Transacton Failed !!");
                                  connection.setAutoCommit(true);
                              }
                    }else {
                        System.out.println("Insuffisient Balance!!");
                    }
                }else {
                    System.out.println("Invalid security pin!!");
                }
            }else {
                System.out.println("Invalid account number!!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void check_balance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Your security pin: ");
        String pin = scanner.nextLine();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number=? AND security_pin = ? ");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,pin);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Available balance is: "+balance);
            }
            else {
                System.out.println("Invalid Pin!!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

