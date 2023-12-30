package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

}
