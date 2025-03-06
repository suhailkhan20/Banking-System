package com.Suhail.BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection , Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Enter your Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String Password = scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User Already Exist for this Email address..");
            return;
        }

        String register_query = "INSERT INTO user(full_name,email,password)VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,Password);

            int affectedrows = preparedStatement.executeUpdate();
            if(affectedrows > 0){
                System.out.println("Registered Successfully !!");
            }else {
                System.out.println("Registered Failed !!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String login(){
        scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();

        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultset = preparedStatement.executeQuery();

            if ((resultset.next())){
                return email;
            }
            else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean user_exist(String email) {
        String exist_query = "SELECT * FROM user WHERE email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(exist_query);
            preparedStatement.setString(1,email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
