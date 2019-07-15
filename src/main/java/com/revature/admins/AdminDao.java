package com.revature.admins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.revature.Dao;

public class AdminDao implements Dao<Admin> {

    private DataSource dataSource;

    // generates a new admin account
    @Override
    public void insert(Admin admin) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pStatement = connection
                    .prepareStatement("insert into users(username, pw, permissions) values(?,?,3)");
            pStatement.setString(1, admin.getUsername());
            pStatement.setString(2, admin.getPw());

            pStatement.executeUpdate();
            PreparedStatement pStatement2 = connection.prepareStatement(
                    "insert into names(nameid, firstname, middlename, lastname) values(get_user_id('?'),?,?,?)");
            pStatement2.setString(1, admin.getUsername());
            pStatement2.setString(2, admin.getFirstname());
            pStatement2.setString(3, admin.getMiddlename());
            pStatement2.setString(4, admin.getLastname());
            pStatement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Admin> getAll() {
        Admin admin;
        List<Admin> admins = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            // change "movies" for your table
            ResultSet resultSet = statement.executeQuery("select * from public.full_set");
            while (resultSet.next()) {
                admin = new Admin();
                admin.setTitle(resultSet.getString("title"));
                admin.setUsername(resultSet.getString("username"));
                admin.setPw(resultSet.getString("pw"));
                admin.setFirstname(resultSet.getString("firstname"));
                admin.setMiddlename(resultSet.getString("middlename"));
                admin.setLastname(resultSet.getString("lastname"));
                admins.add(admin);
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

    // public AdminDao(Connection connection) {
    // this.connection = connection;
    // }
}