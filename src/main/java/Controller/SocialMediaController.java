package Controller;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should refer to prior mini-project labs and lecture materials for 
 * guidance on how a controller may be built.
 * 
 * @author Jennifer Gardner
 * @since December 19, 2023
 */
public class SocialMediaController {

    private SocialMediaService socialMediaService;

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    /**
     * Creates a Javalin controller that has been set up to handle HTTP requests for the Social Media Application. 
     *  
     * The following request endpoints are supported:
     * ## 1: process new User registrations on the endpoint POST localhost:8080/register
     * ## 2: process User logins on the endpoint POST localhost:8080/login
     * ## 3: process the creation of new messages on the endpoint POST localhost:8080/messages
     * ## 4: retrieve all messages on the endpoint GET localhost:8080/messages
     * ## 5: retrieve a message by its ID on the endpoint GET localhost:8080/messages/{message_id}.
     * ## 6: delete a message identified by a message ID on the endpoint DELETE localhost:8080/messages/{message_id}
     * ## 7: update a message text identified by a message ID on the endpoint PATCH localhost:8080/messages/{message_id}
     * ## 8: retrieve all messages written by a particular user on the endpoint GET localhost:8080/accounts/{account_id}/messages
     *
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/account/{account_id}/messages",this::getAllAccountMessagesHandler);

        return app;
    }

    /*
     * Handler to process new user registration, or post a new account.  The registration will be successful if and only 
     * if the username is not blank, the password is at least 4 characters long, and an Account with that username does 
     * not already exist. 
     * 
     * The body will contain a representation of a JSON Account, but will not contain an account_id.
     * - If the registration is successful: the response body will contain a JSON of the Account, including its account_id. 
     *   The response status should be 200 OK, which is the default. The new account should be persisted to the database.
     * - If the registration is not successful, the response status should be 400. (Client error)
     * 
     * @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account requestAccount = mapper.readValue(ctx.body(), Account.class);
        Account responseAccount = socialMediaService.addAccount(requestAccount);
        if(responseAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(responseAccount));
        }
    }

    /* 
     * Handler to process user logins or post account username and password.  The login will be successful 
     * if and only if the username and password provided in the request body JSON match a real account 
     * existing on the database.
     * 
     * The request body will contain a JSON representation of an Account, not containing an account_id. 
     * - If successful, the response body should contain a JSON of the account in the response body, including 
     *   its account_id. The response status should be 200 OK, which is the default.
     * - If the login is not successful, the response status should be 401. (Unauthorized)
     * 
     * @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account requestAccount = mapper.readValue(ctx.body(), Account.class);
        Account responseAccount = socialMediaService.login(requestAccount);
        if(responseAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(responseAccount));
        }
    }

    /*
     * Handler to process the creation of new messages.
     * 
     * The request body will contain a JSON representation of the message, not containing a message_id.
     *  - If successful, the response body should contain a JSON of the message in the response body, including 
     *    its message_id. The response status should be 200 OK, which is the default.
     *  - If the creation of the message is not successful, the response status should be 400. (Client error)
     * 
     * @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
    */
    public void postMessageHandler(Context ctx) throws JsonProcessingException {
       //TODO 
       ObjectMapper mapper = new ObjectMapper();
       Message requestMessage = mapper.readValue(ctx.body(), Message.class);
       Message responseMessage = socialMediaService.getMessage(requestMessage.getMessage_id());
       if(requestMessage != null) {
            ctx.status(400);
       } else {
           ctx.json(mapper.writeValueAsString(responseMessage));
       }
    }

    /*
    ## 4: Our API should be able to retrieve all messages.

    As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

    - The response body should contain a JSON representation of a list containing all messages retrieved from the 
    database. It is expected for the list to simply be empty if there are no messages. The response status should 
    always be 200, which is the default.
    @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
    */
    public void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        //TODO
    }


    /*
    ## 5: Our API should be able to retrieve a message by its ID.


    As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

    - The response body should contain a JSON representation of the message identified by the message_id. It is expected 
    for the response body to simply be empty if there is no such message. The response status should always be 200, which 
    is the default.
    @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
    */
    public void getMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ctx.json(socialMediaService.getMessage(message_id));
    }


    /*
    ## 6: Our API should be able to delete a message identified by a message ID.

    As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

    - The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain the now-deleted message. The response status should be 200, which is the default.
    - If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
    @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        //TODO
    }

    /*
    ## 7: Our API should be able to update a message text identified by a message ID.

    As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. The request body should contain a new message_text values to replace the message identified by message_id. The request body can not be guaranteed to contain any other information.

    - The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
    - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
    */
    public void updateMessageHandler(Context ctx) throws JsonProcessingException {
        //TODO
    }

    /*
    ## 8: Our API should be able to retrieve all messages written by a particular user.

    As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

    - The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    @param ctx the context object handles HTTP requests and generates responses.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
    */
     public void getAllAccountMessagesHandler(Context ctx) throws JsonProcessingException {
        //TODO
     }   

}
