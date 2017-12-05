package com.sabel.ufsa;

import java.sql.*;

public class MessageService {

    private static final String DBCONURL = "jdbc:sqlite:D:\\uebung_message.sqlite";
    private Connection dbConnection;
    private PreparedStatement prepStatementInsert;
    private PreparedStatement prepStatementSelect;
    private PreparedStatement prepStatementSelectId;
    private PreparedStatement prepStatementUpdate;


    public MessageService() throws SQLException {
        this.dbConnection = DriverManager.getConnection(MessageService.DBCONURL);
        this.prepStatementInsert = this.dbConnection.prepareStatement("INSERT INTO message (id,text,tstamp) VALUES (?,?,?)");
        this.prepStatementSelectId = this.dbConnection.prepareStatement("SELECT id FROM message ORDER BY id DESC LIMIT 1");
        this.prepStatementSelect = this.dbConnection.prepareStatement("SELECT id, text, tstamp FROM message");
        this.prepStatementUpdate = this.dbConnection.prepareStatement("UPDATE message SET text = ?, tstamp = ? WHERE id = ?");
    }

    public void close(){
        if(this.dbConnection != null){
            try{
                this.dbConnection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private int getNextId(){
        //Da sqlite kein AUTOINCREMNET kenne hier die letzte benutze ID auslesen und um 1 erhöhen
        try {
            ResultSet resultSet = this.prepStatementSelectId.executeQuery();

            if(resultSet.next()){
                return resultSet.getInt("id")+1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean writeMessage(Message message){
        try {
            this.prepStatementInsert.setInt(1,this.getNextId());
            this.prepStatementInsert.setString(2,message.getText());
            this.prepStatementInsert.setLong(3,message.getTimestamp());

            return this.prepStatementInsert.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

     public boolean modifyMessage(int id, Message message) {
         try {
             this.prepStatementUpdate.setString(1,message.getText());
             this.prepStatementUpdate.setLong(2,message.getTimestamp());
             this.prepStatementUpdate.setInt(3,id);

             return this.prepStatementUpdate.execute();
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return false;
     }

    public String getAListOfAllMessages() {
        ResultSet resultSet = null;
        try {
            resultSet = this.prepStatementSelect.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }

        String outString = "";

        if (resultSet != null) {
            outString = "id\tText\ttimpestamp" + String.format("%n");

            // Alle Nachrichten durchlaufen
            try {
                while (resultSet.next()) {
                    outString += String.valueOf(resultSet.getInt("id") + "\t" + resultSet.getString("text") + "\t" + resultSet.getLong("tstamp") + String.format("%n"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return outString;
    }
}


