package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.google.firebase.database.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * This class contains the implementation and functionality behind the sign up screen.
 */
public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String[] userTypes={"Admin", "Service Provider", "Home Owner"};     // String list used to store all the user types.
    private String userType,username,password,id;                              // Strings representing user type, username, password, and database id respectively.
    private DatabaseReference databaseReference;                              // Stores a DatabaseReference object for firebase use.
    private EditText usernameText,passwordText;                              // Stores EditText objects for the username and password fields.
    private Spinner spinner;                                                // Stores a Spinner object representing the spinner containing the user types.
    private Button signUpButton,signInButton;                              // Stores a Button object representing the sign up and sign in buttons.
    private ArrayList<User> users;                                        // ArrayList that stores all the users present in the database
    private boolean passwordValid;                                       // boolean used for password validation when signing in

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initializing Variables
        users = new ArrayList<User>();
        passwordValid=false;

        // Retrieving all the users stored in the database
        getUsers();

        // Spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,userTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Text Views
        usernameText = (EditText) findViewById(R.id.userName);
        passwordText = (EditText)findViewById(R.id.password);


        // Sign Up Button
        signUpButton = findViewById(R.id.signup_Button);
        signUpButton.setOnClickListener(new View.OnClickListener() { // On Sign Up Button click

            public void onClick(View v) {

                username = usernameText.getText().toString().trim();
                password = passwordText.getText().toString().trim();

                // Validating text fields input if they're empty
                if (username.equals("") || password.equals("")) {

                    // If input is invalid, display so
                    Toast.makeText(getApplicationContext(), "Please make sure that none of the fields are empty!", Toast.LENGTH_SHORT).show();
                }

                // If the user wants to create an admin account and the username and password fields are both not "admin", app prints so
                else if (userType.equals("Admin") && (!(username.equals("admin") && password.equals("admin")))) {

                        Toast.makeText(getApplicationContext(), "The admin account can have a username: admin and password: admin only!", Toast.LENGTH_SHORT).show();
                }

                // If the user the user is trying to create an account that exists, the app prints so
                else if (userExists()) {

                    // If input is invalid, display so
                    if (userType.equals("Admin")) {

                        Toast.makeText(getApplicationContext(), "An admin account already exists!", Toast.LENGTH_SHORT).show();
                    }
                     else {

                        Toast.makeText(getApplicationContext(), "The user already exists!", Toast.LENGTH_SHORT).show();
                    }
                }

                else {

                    // Displaying toast informing the user that his account is being created
                    Toast.makeText(getApplicationContext(), "Creating an " + userType + " account...", Toast.LENGTH_SHORT).show();

                    // Adding the user
                    addUser();

                    // Resetting the text view values
                    usernameText.setText("");
                    passwordText.setText("");

                    // Welcoming user after successful account creation
                    welcomeUser("SIGNUP");
                }

                passwordValid=false;
            }
        });

        // Sign In Button
        signInButton = findViewById(R.id.signin_Button);
        signInButton.setOnClickListener(new View.OnClickListener() { // On Sign In Button click

            public void onClick(View v) {

                username = usernameText.getText().toString().trim();
                password = passwordText.getText().toString().trim();

                // Validating text fields input if they're empty
                if (username.equals("") || password.equals("")) {

                    // If input is invalid, display so
                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure that none of the fields are empty!", Toast.LENGTH_SHORT).show();
                }

                // If the user exists, the welcome screen is displayed
                else if (userExists() && passwordValid) {

                    Toast.makeText(getApplicationContext(), "Signing you in!", Toast.LENGTH_SHORT).show();

                    // Resetting the text view values
                    usernameText.setText("");
                    passwordText.setText("");

                    // Welcoming user after successful account creation
                    welcomeUser("SIGNIN");
                }

                // If the user doesn't exist, display so
                else {

                    if (!passwordValid) {

                        Toast.makeText(getApplicationContext(), "Password is invalid!", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        Toast.makeText(getApplicationContext(), "The user doesn't exist!", Toast.LENGTH_SHORT).show();
                    }
                }

                passwordValid=false;
            }
        });
    }

    /**
     * This method sets the variable userType to the appropriate type based on
     * the item selected in the spinner. It also resets the text fields usernameText
     * and passwordText after every selection. It also displays a toast informing
     * the user that a certain account type is selected.
     * @param arg0
     * @param arg1
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

        // Setting the user type
        userType = userTypes[position];

        // Resetting the edit text fields after every account type change
        usernameText.setText("");
        passwordText.setText("");

        // Displaying toast with the user type selected
        Toast.makeText(getApplicationContext(), userTypes[position] + " Selected", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method does nothing when no item is selected on the spinner,
     * since an item is already selected by default.
     * @param arg0
     */
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Do nothing
    }

    /**
     * This method reads the ArrayList containing all the users and checks if the
     * user exists. It also checks if the username and password entered are valid
     * compared to the username and password stored in the database.
     *
     * @return true if the user exists in the database, false otherwise.
     */
    public boolean userExists() {

        // boolean result used to return the result
        boolean result=false;

        // For loop used to access all the users stored in the ArrayList users
        for (int i=0; i<users.size();i++) {

            if (users.get(i).getAccountType().equals(userType) && users.get(i).getUsername().equals(username)) {

                result=true;
                break;
            }
        }

        // For loop used to access all the users stored in the ArrayList users
        for (int i=0; i<users.size();i++) {

            try{

                if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(Sha1.hash(password))) {

                    passwordValid=true;
                    id=users.get(i).getId();
                    break;
                }
            }

            catch (UnsupportedEncodingException e) {

                Toast.makeText(getApplicationContext(), "Can't hash your password!", Toast.LENGTH_SHORT).show();
            }
        }

        return result;
    }

    /**
     * This method gets all the users stored in the database.
     */
    public void getUsers() {

        // Connecting to the database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clearing the ArrayList users
                users.clear();

                // Getting all the users in the database
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    // Adding every user in the database to the ArrayList users
                    User user = postSnapShot.getValue(User.class);
                    users.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting users from the database!" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is called when the fields are validated in order to move to WelcomeScreenActivity, AdminWelcomeScreen,
     * ServiceProviderProfileSetup, ServiceProviderInformationScreen, or HomeOwnerChoosesServiceProvider screens.
     *
     * @param type String variable used to determine whether a user is signing up or signing in
     */
    public void welcomeUser(String type){
        Intent intent;

        if (userType.equals("Admin")) {

            // Passing username and account type to AdminWelcomeScreen
            intent = new Intent(this, AdminWelcomeScreen.class);
            startActivity(intent);
        }

        else if (userType.equals("Home Owner")) {

            // Passing username and account type to WelcomeStreetActivity
            intent = new Intent(this, HomeOwnerChooseServiceProvider.class);
            intent.putExtra("HOMEOWNERID",id);
            startActivity(intent);
        }

        // If the service provider is signing up, allow him to set his profile up
        else if (userType.equals("Service Provider") && type.equals("SIGNUP")) {

            // Passing the service provider database id to ServiceProviderProfileSetup
            intent = new Intent(this, ServiceProviderProfileSetUp.class);
            intent.putExtra("SERVICEPROVIDERID",id);
            startActivity(intent);
        }

        // If the service provider is signing in, display his profile information
        else if (userType.equals("Service Provider") && type.equals("SIGNIN")) {

            // Passing the service provider database id to ServiceProviderInformationScreen
            intent = new Intent(this, ServiceProviderInformationScreen.class);
            intent.putExtra("SERVICEPROVIDERDBID",id);
            startActivity(intent);
        }
    }

    /**
     * This method is called when all the validation are successful and adds
     * the user to the database.
     */
    public void addUser() {

        // Connecting to the database, obtaining a unique id, and adding the user
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        id = databaseReference.push().getKey();

        try {

            // Creating a User object to add to the database
            User user =  new User(id,userType,username,Sha1.hash(password));
            databaseReference.child(id).setValue(user);
        }

        catch (UnsupportedEncodingException e) {

            Toast.makeText(getApplicationContext(), "Can't hash your password!", Toast.LENGTH_SHORT).show();
        }
    }
}
