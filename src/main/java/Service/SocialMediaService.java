package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

/*
 * to design and create your own Service classes from scratch.
 * You should refer to prior mini-project lab examples and course material for guidance.
 * 
 * @author Jennifer Gardner
 */
public class SocialMediaService {
    
    AccountDAO accountDAO;
    MessageDAO messageDAO;

    /*
     * no-args constructor
     */
    public SocialMediaService() {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    /*
     * ## 1: Our API should be able to process new User registrations. 
     * 
     * The registration requires these conditions: the username is not blank, the password is at least 4 
     * characters long, and an Account with that username does not already exist. If all these conditions 
     * are met, the new account should be persisted to the database.
     * 
     * @param account an object representing a new account
     * @return The newly added account if the add operation was sucessful, including account_id.  
     */
    public Account addAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        Account newAccount = null;

        if( username != null && password != null && password.length() >=4 && username.length() > 0) {
            Account existingAccount = accountDAO.getAccountByUsername(username);
            if(existingAccount == null) {
                newAccount = accountDAO.insertAccount(account);
            }
        }
        return newAccount;
    }

    /* 
     * ## 2: Our API should be able to process User logins. 
     * 
     * The login will be successful if and only if the username and password provided match 
     * a real account existing on the database. 
     * 
     * @param account an object representing an account with login information.
     * @return The account with matching username and password, including account_id.
     */
    public Account login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        Account validAccount = accountDAO.getAccountByLogin(username, password); 
        
        return validAccount;
    }

    /*
     * ## 3: Our API should be able to process the creation of new messages.
     * 
     * The creation of the message will be successful if and only if the message_text is not blank, is not 
     * over 255 characters, and posted_by refers to a real, existing user. If all these conditins are met, 
     * the new message should be persisted to the database.
     * 
     * @param message an object representing a new message to be added to the database.
     * @return the message, including message_id.
     */
    public Message AddMessage(Message message) {
        Message newMessage = null;
        if(message != null && message.getMessage_text() != null 
                && message.getMessage_text() != null && message.getMessage_text().length() > 0
                && message.getMessage_text().length() <= 255) {
            
            Account isRealUser = accountDAO.getAccountByID(message.getMessage_id());
            if(isRealUser != null) {
                newMessage = messageDAO.insertMessage(message);
            }       
        }
        return newMessage;
    }

    /*
     * ## 4: Our API should be able to retrieve all messages.
     * 
     * @return a list containing all messages retrieved from the database. It is expected 
     * for the list to simply be empty if there are no messages.
    */
    public Message[] getAllMessages() {
        return null;
    }


    /*
     * ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * @ param ID the message_id for the requested message.
     * @ return an object representation of the message identified by the message_id. It is expected for the 
     * response body to simply be empty if there is no such message. 
     */
    public Message getMessage(int ID) {
        return null;
    }


    /*
    ## 6: Our API should be able to delete a message identified by a message ID.

    As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

    - The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain the now-deleted message. The response status should be 200, which is the default.
    - If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
    */
    public boolean deleteMessage(Integer ID) {
        return false;
    }

    /*
    ## 7: Our API should be able to update a message text identified by a message ID.

    As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. The request body should contain a new message_text values to replace the message identified by message_id. The request body can not be guaranteed to contain any other information.

    - The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
    - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    */
    public Message UpdateMessage(Message message) {
        return null;
    }

    /*
    ## 8: Our API should be able to retrieve all messages written by a particular user.

    As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

    - The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    */
     public Message[] getAllMessages(Account account) {
        return null;
     }   

}
