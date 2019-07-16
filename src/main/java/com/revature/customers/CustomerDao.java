package com.revature.customers;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.Dao;

public class CustomerDao implements Dao<Customer> {

    Connection connection;

    // Generates a new customer account
    @Override
    public void insert(Customer customer) {
        try {
            PreparedStatement pStatement = connection
                    .prepareStatement("insert into users (username, pw, permissions) values (?,?, 1)");
            pStatement.setString(1, customer.getUsername());
            pStatement.setString(2, customer.getPw());
            pStatement.executeUpdate();

            PreparedStatement pStatement2 = connection.prepareStatement(
                    "insert into names (nameid, firstname, middlename, lastname) VALUES (get_user_id(?), ?, ?, ?)");
            pStatement2.setString(1, customer.getUsername());
            pStatement2.setString(2, customer.getFirstname());
            pStatement2.setString(3, customer.getMiddlename());
            pStatement2.setString(4, customer.getLastname());
            pStatement2.executeUpdate();

            if (customer.getAccountnumber() == 0) {
                PreparedStatement pStatement3 = connection
                        .prepareStatement("insert into customers(custid, balance) values(get_user_id(?),0)");
                pStatement3.setString(1, customer.getUsername());
                // pStatement3.setBigDecimal(2, customer.getBalance());
                pStatement3.executeUpdate();
                System.out.println("\nRegular account created!\n");
            } else {
                PreparedStatement pStatement3 = connection.prepareStatement(
                        "insert into customers(custid, accountnumber, balance) values(get_user_id(?),?,0)");
                pStatement3.setString(1, customer.getUsername());
                pStatement3.setInt(2, customer.getAccountnumber());
                // pStatement3.setBigDecimal(2, customer.getBalance());
                pStatement3.executeUpdate();
                System.out.println("\nJoint account created!\n");
            }

        } catch (SQLException e) {
        }
    }

    public int readAccountNumbers(String user) {
        try {
            int userid = getUserIdByUsername(user);
            System.out.println("readAccountNumbers received this: " + userid + " from getUserIdByUsername");
            PreparedStatement pStatement = connection
                    .prepareStatement("select accountnumber from customers where custid = ?");
            pStatement.setInt(1, userid);
            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();
            int an = resultSet.getInt("accountnumber");
            return an;
        } catch (SQLException e) {
        }
        return 0;
    }

    public String withdrawBalance(String user, BigDecimal bd) {
        try {
            int an = readAccountNumbers(user);

            PreparedStatement pStatement0 = connection
                    .prepareStatement("select balance from customers where accountnumber = ?");
            pStatement0.setInt(1, an);
            ResultSet resultSet1 = pStatement0.executeQuery();
            resultSet1.next();
            String amt1 = resultSet1.getString("balance");

            PreparedStatement pStatement = connection
                    .prepareStatement("update customers set balance = balance - ?::money where accountnumber = ?");
            pStatement.setBigDecimal(1, bd);
            pStatement.setInt(2, an);
            pStatement.executeUpdate();

            PreparedStatement pStatement1 = connection
                    .prepareStatement("select balance from customers where accountnumber = ?");
            pStatement1.setInt(1, an);
            ResultSet resultSet2 = pStatement1.executeQuery();
            resultSet2.next();
            String amt2 = resultSet2.getString("balance");
            return amt1 + " - $" + bd + " = " + amt2;

        } catch (SQLException e) {
        }
        return "Transaction Error";
    }

    public String depositBalance(String user, BigDecimal bd) {
        try {
            int an = readAccountNumbers(user);

            PreparedStatement pStatement0 = connection
                    .prepareStatement("select balance from customers where accountnumber = ?");
            pStatement0.setInt(1, an);
            ResultSet resultSet1 = pStatement0.executeQuery();
            resultSet1.next();
            String amt1 = resultSet1.getString("balance");

            PreparedStatement pStatement = connection
                    .prepareStatement("update customers set balance = balance + ?::money where accountnumber = ?");
            pStatement.setBigDecimal(1, bd);
            pStatement.setInt(2, an);
            pStatement.executeUpdate();

            PreparedStatement pStatement1 = connection
                    .prepareStatement("select balance from customers where accountnumber = ?");
            pStatement1.setInt(1, an);
            ResultSet resultSet2 = pStatement1.executeQuery();
            resultSet2.next();
            String amt2 = resultSet2.getString("balance");
            return amt1 + " + $" + bd + " = " + amt2;

        } catch (SQLException e) {
        }
        return "Transaction Error";
    }

    // refactor to use two account numbers
    public String transferBalance(String user, int an2, BigDecimal bd) {

        try {
            int an1 = readAccountNumbers(user);
            PreparedStatement pStatement0 = connection
                    .prepareStatement("select balance from customers where accountnumber = ?");
            pStatement0.setInt(1, an1);
            ResultSet resultSet1 = pStatement0.executeQuery();
            resultSet1.next();
            String amt1 = resultSet1.getString("balance");

            // subtract from account 1
            PreparedStatement pStatementa = connection
                    .prepareStatement("update customers set balance = balance - ?::money where accountnumber = ?");
            pStatementa.setBigDecimal(1, bd);
            pStatementa.setInt(2, an1);
            pStatementa.executeUpdate();

            PreparedStatement pStatementb = connection
                    .prepareStatement("update customers set balance = balance + ?::money where accountnumber = ?");
            pStatementb.setBigDecimal(1, bd);
            pStatementb.setInt(2, an2);
            pStatementb.executeUpdate();

            PreparedStatement pStatement1 = connection
                    .prepareStatement("select balance from customers where accountnumber = ?");
            pStatement1.setInt(1, an2);
            ResultSet resultSet2 = pStatement1.executeQuery();
            resultSet2.next();
            String amt2 = resultSet2.getString("balance");
            return "New Balance of account number " + an1 + ": " + amt1 + "\nNew Balance of account number " + an2
                    + ": " + amt2;

        } catch (SQLException e) {
        }
        return "Transaction Error";
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
        }
        return 0;
    }

    public int checkAccountStatus(int an) {
        try {
            PreparedStatement pStatement = connection
                    .prepareStatement("select custactive from customers where accountnumber = ?");
            pStatement.setInt(1, an);
            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("custactive");
        } catch (SQLException e) {
        }
        return 0;
    }

    public void approveDenyApplications(Scanner scnr) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from public.full_nopw where statusactive = 'New'");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columns = rsmd.getColumnCount();
            System.out.println("Account Number\tStatus\t\tUser Name\t\tFirst Name\tMiddle Name\tLast Name\t\tBalance");
            while (resultSet.next()) {
                for (int i = 1; i <= columns; i++) {
                    if (i > 1)
                        System.out.print("\t\t");
                    System.out.print(resultSet.getString(i));
                }
                System.out.println();
            }
            System.out.println("Please enter the number of the account you wish to modify:");
            int an = scnr.nextInt();
            System.out.println("1. Approve\n2. Deny\n3. Exit");
            int edit = scnr.nextInt() + 1;
            if (edit == 2 | edit == 3) {
                PreparedStatement pStatement = connection
                        .prepareStatement("update customers set custactive = ? where accountnumber = ?");
                pStatement.setInt(1, edit);
                pStatement.setInt(2, an);
                pStatement.executeQuery();
            } else {
                System.out.println("Exiting...");
                return;
            }
            switch (edit) {
            case 2:
                System.out.println("Approved!\n");
                break;
            case 3:
                System.out.println("Denied!\n");
                break;
            }
            System.out.println();
        } catch (SQLException e) {
        }
    }

    public void cancelAccount(Scanner scnr) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("select * from public.full_nopw where statusactive = 'Approved'");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columns = rsmd.getColumnCount();
            System.out.println("Account Number\tStatus\t\tUser Name\t\tFirst Name\tMiddle Name\tLast Name\t\tBalance");
            while (resultSet.next()) {
                for (int i = 1; i <= columns; i++) {
                    if (i > 1)
                        System.out.print("\t\t");
                    System.out.print(resultSet.getString(i));
                }
                System.out.println();
            }
            System.out.println("Please enter the number of the account you wish to cancel:");
            int an = scnr.nextInt();
            PreparedStatement pStatement = connection
                    .prepareStatement("update customers set custactive = 3 where accountnumber = ?");
            pStatement.setInt(1, an);
            pStatement.executeQuery();
            System.out.println("Account number " + an + "has been canceled.\n");
        } catch (SQLException e) {
            System.out.println("I'm sorry, but that account number does not match our records.\n");
        }
    }

    @Override
    public List<Customer> getAll() {
        Customer customer;
        List<Customer> customers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
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
        }
        return customers;
    }

    public List<Customer> getAccountInfo(int an) {
        Customer customer;
        List<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement pStatement = connection
                    .prepareStatement("select * from public.full_set where accountnumber = ?");
            pStatement.setInt(1, an);
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                customer = new Customer();
                customer.setTitle(resultSet.getString("title"));
                customer.setAccountnumber(an);
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
        }
        return customers;
    }

    public void getAccountList(String u) {
        try {
            PreparedStatement pStatement = connection
                    .prepareStatement("select accountnumber, balance from public.full_nopw where username = ?");
            pStatement.setString(1, u);
            ResultSet resultSet = pStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columns = rsmd.getColumnCount();
            System.out.println("Account Number\t\tBalance");
            while (resultSet.next()) {
                for (int i = 1; i <= columns; i++) {
                    if (i > 1)
                        System.out.print("\t\t\t");
                    System.out.print(resultSet.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
        }
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
        System.out.println("getUserIdByUsername thinks " + username + " is your username");
        int result = 0;
        String sql = "{ ? = call get_user_id(?) }";
        try {
            CallableStatement cStatement = connection.prepareCall(sql);
            cStatement.registerOutParameter(1, Types.INTEGER);
            cStatement.setString(2, username);
            cStatement.execute();
            result = cStatement.getInt(1);
            System.out.println("getUserIdByUsername result is: " + result);
        } catch (SQLException e) {
        }
        return result;
    }

    // public int getUserIdByUsername(String u) {
    // try {
    // PreparedStatement pStatement = connection.prepareStatement("select userid
    // from users where username = ?");
    // pStatement.setString(1, u);
    // ResultSet resultSet = pStatement.executeQuery();
    // int rs = resultSet.getInt("userid");
    // return rs;
    // } catch (SQLException e) {
    // }
    // System.out.println("Something went wrong...");
    // return 0;
    // }

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
        }
        return result;
    }
}