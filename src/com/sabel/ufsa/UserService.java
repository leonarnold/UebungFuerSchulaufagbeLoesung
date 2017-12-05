package com.sabel.ufsa;

import java.sql.*;

public class UserService {

    private static final String DBCONURL = "jdbc:sqlite:D:\\uebung_user.sqlite";
    private Connection dbConnection;
    private PreparedStatement prepStatementInsert;
    private PreparedStatement prepStatementSelect;

    public UserService() throws SQLException {
        this.dbConnection = DriverManager.getConnection(UserService.DBCONURL);
        this.prepStatementInsert = this.dbConnection.prepareStatement("INSERT INTO user (name,password) VALUES (?,?)");
        this.prepStatementSelect = this.dbConnection.prepareStatement("SELECT name, password  FROM user WHERE name like ?");
    }

    public boolean writeUser(User user) {

        try {
            this.prepStatementInsert.setString(1,user.getName());
            this.prepStatementInsert.setString(2,user.getPassword());
            return this.prepStatementInsert.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public User getUser(String name) {
        ResultSet resultSet = null;

        try {
            this.prepStatementSelect.setString(1,name);
            resultSet = this.prepStatementSelect.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(resultSet != null){
            String userName = null;
            String password = null;

            try {
                if(resultSet.next()) {
                    userName = resultSet.getString(1);
                    password = resultSet.getString(2);
                    return new User(userName,password);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

