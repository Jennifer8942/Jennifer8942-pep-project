package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

/*
 * Mediates the transformation of data between the Java Class Message to rows in the
 * database table message.
 *  
 * The database table named 'message':
 * 
 *   message_id          int             primary key 
 *   posted_by           int             foreign key to account.account_id
 *   message_text        varchar(255)
 *   time_posted_epoch   bigint
 *
 * @author Jennifer Gardner
 */
public class MessageDAO {
    
    /**
     * Retrieve all messages from the message table.
     *
     * @return all messages.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"), 
                                              rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

     /**
     * Retrieve all messages from the message table which have the requested account_id.
     *
     * @param ID the account_id requested.
     * @return all messages.
     */
    public List<Message> getAllMessages(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE account_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"), 
                                              rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /*
     * Add a message record into the database which matches the values contained in the message object.
     * 
     * @param message an object modelling a Message. The message object does not contain a message_id.
     * @return The Message object matching the record inserted into the database, including a message_id.
     */
    public Message insertMessage(Message message) {
        Message newMessage = null;
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here.
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1, message.getMessage_id());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                 newMessage = new Message(rs.getInt("message_id"), 
                                rs.getInt("posted_by"),
                                rs.getString("message_text"), 
                                rs.getInt("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return newMessage;
    }

    /**
     * Get a message record from the database which has the requested message_id.
     * 
     * @param ID the requested message_id.
     * @return the Message object matching the record retrieved from the database.
     */
    public Message getMessage(int ID) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        
        try {
            //Write SQL logic here.
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE ID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1, ID);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                 message = new Message(rs.getInt("message_id"), 
                                rs.getInt("posted_by"),
                                rs.getString("message_text"), 
                                rs.getInt("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return message;
    }

    /**
     * Deleted a message record from the the database which has the requsted message_id.  
     * 
     * @param ID the requested message_id.
     * @return the Message object matching the deleted record or null if no matching record existed.
     */
    public Message deleteMessage(int ID) {
        Message deletedMessage = getMessage(ID);
        if(deletedMessage != null) {

            Connection connection = ConnectionUtil.getConnection();
            try {
                //Write SQL logic here.
                String sql = "DELETE FROM message WHERE message_id = ?;" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                //write preparedStatement's setString and setInt methods here.
                preparedStatement.setInt(1, ID);
                ResultSet rs = preparedStatement.executeQuery();

                /*if(rs.next()){
                    newMessage = new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"), 
                                    rs.getInt("time_posted_epoch"));
                }*/
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return deletedMessage;
    }
}
