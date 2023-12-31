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
 * The database table names 'account':
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
     * @return all accounts.
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
     * @param account an object modelling an Account. the account object does not contain an account_id.
     * @return The Account object matching the record inserted into the database, including an account_id.
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here.
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Get an account record from the database account table, which has a matching username.
     * 
     * @param username the account username.
     * @return The Account with matching username and password, including account_id. 
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
     * @return The Account with matching username and password, including account_id.
     */
    public Account getAccountByLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            //Write SQL logic here
            String sql = "SELECT account_id, username, password FROM account WHERE username = ? and password = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
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



}
