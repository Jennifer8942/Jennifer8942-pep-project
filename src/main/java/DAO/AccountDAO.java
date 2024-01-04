package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Model.Account;
import Util.ConnectionUtil;

/*
 * Mediates the transformation of data between the Java Class Account to rows in the
 * database table Account. 
 * 
 * The database table named 'account':
 *   account_id      int             primary key
 *   username        varchar(255)    unique
 *   password        varchar(255)
 *
 * @author Jennifer Gardner
 */
public class AccountDAO {
    
    /**
     * Retrieve all accounts from the account table.
     *
     * @return all accounts. List of Account objects which model an account record.
     */
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT account_id, username, password FROM account;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), 
                                              rs.getString("username"),
                                              rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /*
     * Add an account record into the database which matches the values contained in the account object.
     * 
     * @param account an object modelling an Account that does not contain an account_id.
     * @return The generated account_id of the newly inserted account record.
     */
    public int insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        int generated_account_id = 0;
        try {
            //Write SQL logic here.
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();

            ResultSet pkrs = ps.getGeneratedKeys();
            while(pkrs.next()){
                generated_account_id = (int) pkrs.getLong(1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return generated_account_id;
    }

    /*
     * Get an account record from the database account table, which has a matching username.
     * 
     * @param username the account username.
     * @return The Account with matching username, including account_id. Null if does not exist.
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            //Write SQL logic here
            String sql = "SELECT account_id, username, password FROM account WHERE username = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                account = new Account(rs.getInt("account_id"), 
                                              rs.getString("username"),
                                              rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    /*
     * Get an account record from the database account table, which has login credentials matching the paramaters.
     * 
     * @param username the account username
     * @param password the account password
     * @return The Account with matching username and password, including account_id.  Null if does not exist.
     */
    public Account getAccountByLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            // Select account with matching username and password
            String sql = "SELECT account_id, username, password FROM account WHERE username = ? and password = ?;";

            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                account = new Account(rs.getInt("account_id"), 
                                              rs.getString("username"),
                                              rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    /*
     * Get an account record from the database account table, which has a matching account_id.
     * 
     * @param username the account ID.
     * @return The Account with matching account_id. 
     */
    public Account getAccountByID(int ID) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            //Write SQL logic here
            String sql = "SELECT account_id, username, password FROM account WHERE account_id = ?;";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                account = new Account(rs.getInt("account_id"), 
                                              rs.getString("username"),
                                              rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

}
