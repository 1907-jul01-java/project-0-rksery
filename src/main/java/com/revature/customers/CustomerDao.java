package com.revature.customers;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.Dao;

public class CustomerDao implements Dao<Customer> {

    Connection connection;

    // Generates a new customer account
    @Override
    public void insert(Customer customer) {
        try {
            // int userid = getUserIdByUsername(customer.getUsername());
            // System.out.println(customer.getUsername());
            // System.out.println(customer.getPw());
            // System.out.println(customer.getFirstname());
            // System.out.println(customer.getMiddlename());
            // System.out.println(customer.getLastname());
            // System.out.println(customer.getBalance());

            PreparedStatement pStatement = connection
                    .prepareStatement("insert into users (username, pw, permissions) values (?,?, 1)");
            pStatement.setString(1, customer.getUsername());
            pStatement.setString(2, customer.getPw());
            pStatement.executeUpdate();

            // System.out.println("Executed");
            // System.out.println();

            // System.out.println(customer.getUsername());
            // System.out.println(customer.getFirstname());
            // System.out.println(customer.getMiddlename());
            // System.out.println(customer.getLastname());

            PreparedStatement pStatement2 = connection.prepareStatement(
                    "insert into names (nameid, firstname, middlename, lastname) VALUES (get_user_id(?), ?, ?, ?)");
            pStatement2.setString(1, customer.getUsername());
            pStatement2.setString(2, customer.getFirstname());
            pStatement2.setString(3, customer.getMiddlename());
            pStatement2.setString(4, customer.getLastname());
            pStatement2.executeUpdate();

            // System.out.println("Executed");
            // System.out.println();

            // System.out.println(customer.getUsername());
            // System.out.println(customer.getBalance());

            PreparedStatement pStatement3 = connection
                    .prepareStatement("insert into customers(custid, balance) values(get_user_id(?),?)");
            pStatement3.setString(1, customer.getUsername());
            pStatement3.setBigDecimal(2, customer.getBalance());
            pStatement3.executeUpdate();
            connection.close();

            // System.out.println("Executed");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int authenticateUser(String un, String pass) {
        try {
            PreparedStatement pStatement = connection.prepareStatement("select pw from users where username = ?");
            pStatement.setString(1, un.toString());
            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();
            if (pass.equals(resultSet.getString("pw"))) {
                int perm = getPermissionByUsername(un);
                return perm;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Customer> getAll() {
        Customer customer;
        List<Customer> customers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            // change "movies" for your table
            ResultSet resultSet = statement.executeQuery("select * from public.full_set");
            while (resultSet.next()) {
                customer = new Customer();
                customer.setTitle(resultSet.getString("title"));
                customer.setUsername(resultSet.getString("username"));
                customer.setPw(resultSet.getString("pw"));
                customer.setFirstname(resultSet.getString("firstname"));
                customer.setMiddlename(resultSet.getString("middlename"));
                customer.setLastname(resultSet.getString("lastname"));
                String str = resultSet.getString("balance");
                str = str.replaceAll("[$,]", "");
                // System.out.println("After replacing: " + str);
                BigDecimal bd = new BigDecimal(str);
                // System.out.println("Here's the big decimal:" + bd);
                customer.setBalance(bd);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    public CustomerDao(Connection connection) {
        this.connection = connection;
    }

    // callable statement
    public int getUserIdByUsername(String username) {
        int result = 0;
        String sql = "{ ? = call get_user_id(?) }";
        try {
            CallableStatement cStatement = connection.prepareCall(sql);
            cStatement.registerOutParameter(1, Types.INTEGER);
            cStatement.setString(2, username);
            cStatement.execute();
            result = cStatement.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getPermissionByUsername(String username) {
        int result = 0;
        String sql = "{ ? = call get_user_perm(?) }";
        try {
            CallableStatement cStatement = connection.prepareCall(sql);
            cStatement.registerOutParameter(1, Types.INTEGER);
            cStatement.setString(2, username);
            cStatement.execute();
            result = cStatement.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}