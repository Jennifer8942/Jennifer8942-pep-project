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
     * @return all messages.  List of Message objects which model a message record.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message;";

            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"), 
                                              rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

     /**
     * Retrieve all messages from the message table which have the requested account_id as the posted_by value.
     *
     * @param ID the account_id for the requested posted_by.
     * @return all messages.  List of Message objects.
     */
    public List<Message> getAllMessages(int ID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE posted_by = ?;";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                                              rs.getInt("posted_by"),
                                              rs.getString("message_text"), 
                                              rs.getLong("time_posted_epoch"));
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
     * @return the generated message_id of the newly inserted message record in the database.
     */
    public int insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        int generated_message_id = 0;
        try {
            //Write SQL logic here.
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);" ;
            
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet pkrs = ps.getGeneratedKeys();
            if(pkrs.next()){
                generated_message_id = (int) pkrs.getLong(1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return generated_message_id;
    }

    /*
     * Update a message record's message_text field in the database which matches the values 
     * contained in the message object.
     * 
     * @param message an object modelling a Message. 
     * @return The message_id of the record updated in the database.
     */
    public int updateMessageText(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        int id = 0;
        try {
            //Write SQL logic here.
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;" ;
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());

            int result = ps.executeUpdate();
            if(result > 0) {
                id = message.getMessage_id();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return id;
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
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE message_id = ?;";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                 message = new Message(rs.getInt("message_id"), 
                                rs.getInt("posted_by"),
                                rs.getString("message_text"), 
                                rs.getLong("time_posted_epoch"));
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
     * @return true if the matching record was deleted or false if no matching record existed.
     */
    public boolean deleteMessage(int ID) {
        Connection connection = ConnectionUtil.getConnection();
        int result = 0;
        try {
            //Write SQL logic here.
            String sql = "DELETE FROM message WHERE message_id = ?;" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1, ID);
            result = preparedStatement.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return result > 0 ? true : false;
    }
}
