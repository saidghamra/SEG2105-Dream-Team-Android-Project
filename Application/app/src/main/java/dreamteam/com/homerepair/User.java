package dreamteam.com.homerepair;

/**
 * Represents a user object from Firebase
 */
public class User {

    private AccountType accountType;
    private String username;
    private String password;

    /**
     * Creates a user object
     * @param username the user's username
     * @param password the user's password
     */
    public User(AccountType accountType,String username, String password){
        this.accountType = accountType;
        this.username = username;
        this.password = password;
    }

    /**
     * Get the type of account the user has
     * @return the user's account type
     */
    public AccountType getAccountType(){
        return this.accountType;
    }

    /**
     * Gets the user's username
     * @return the user's username
     */

    public String getUsername(){
        return this.username;
    }

    /**
     * Gets the user's password
     * @return the user's password
     */

    public String getPassword(){
        return this.password;
    }




}
