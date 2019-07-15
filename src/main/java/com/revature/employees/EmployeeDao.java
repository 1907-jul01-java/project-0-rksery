//make one of these for each table (Accounts, Employees, Admins, etc)

package com.revature.employees;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.Dao;

public class EmployeeDao implements Dao<Employee> {
    Connection connection;

    @Override
    public void insert(Employee employee) {
        try {
            PreparedStatement pStatement = connection
                    .prepareStatement("insert into users(username, pw, permissions) values(?,?,3)");
            pStatement.setString(1, employee.getUsername());
            pStatement.setString(2, employee.getPw());

            pStatement.executeUpdate();
            PreparedStatement pStatement2 = connection.prepareStatement(
                    "insert into names(nameid, firstname, middlename, lastname) values(get_user_id('?'),?,?,?)");
            pStatement2.setString(1, employee.getUsername());
            pStatement2.setString(2, employee.getFirstname());
            pStatement2.setString(3, employee.getMiddlename());
            pStatement2.setString(4, employee.getLastname());
            pStatement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> getAll() {
        Employee employee;
        List<Employee> employees = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            // change "movies" for your table
            ResultSet resultSet = statement.executeQuery("select * from public.full_set");
            while (resultSet.next()) {
                employee = new Employee();
                employee.setTitle(resultSet.getString("title"));
                employee.setUsername(resultSet.getString("username"));
                employee.setPw(resultSet.getString("pw"));
                employee.setFirstname(resultSet.getString("firstname"));
                employee.setMiddlename(resultSet.getString("middlename"));
                employee.setLastname(resultSet.getString("lastname"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    public EmployeeDao(Connection connection) {
        this.connection = connection;
    }
}