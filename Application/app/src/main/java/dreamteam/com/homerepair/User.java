package dreamteam.com.homerepair;

/**
 * Represents a user object from Firebase
 */
public class User {
    private String email;
    private String password;
    private String username;
    private AccountType accountType;

    /**
     * Creates a user object with default values (null for email,password, username) & account type of home owner
     */
    public User(){
        this.accountType =AccountType.HOME_OWNER;
    }

    /**
     * Creates a user object
     * @param username the user's username
     * @param email the user's email
     * @param password the user's password
     */
    public User(AccountType accountType,String username, String email, String password){
        this.accountType = accountType;
        this.username = username;
        this.email = email;
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
     * Gets the user's email
     * @return the user's email
     */

    public String getEmail(){
        return this.email;
    }

    /**
     * Gets the user's password
     * @return the user's password
     */

    public String getPassword(){
        return this.password;
    }




}
