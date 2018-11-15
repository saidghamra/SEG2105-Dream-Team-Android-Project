package dreamteam.com.homerepair;

/**
 * Represents a user object from Firebase
 */
public class User {

    private String accountType;
    private String username;
    private String password;
    private String id;

    /**
     * Creates a user object
     * @param username the user's username
     * @param password the user's password
     */
    public User(String id, String accountType,String username, String password){
        this.id=id;
        this.accountType = accountType;
        this.username = username;
        this.password = password;
    }

    /**
     * Creates a user object
     */
    public User () {

    }

    /**
     * Get the type of account the user has
     * @return the user's account type
     */
    public String getAccountType(){
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

    /**
     * Gets the user's id in the database
     * @return the user's database id
     */
    public String getId(){
        return this.id;
    }

}
