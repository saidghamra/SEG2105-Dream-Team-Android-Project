package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.google.firebase.database.*;

import java.util.ArrayList;

/**
 * This class contains the implementation and functionality behind the sign up screen.
 */
public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] userTypes={"Admin", "Service Provider", "Home Owner"};     // String list used to store all the user types.
    String userType,username,password;                                 // Strings representing user type, username, and password respectively.
    DatabaseReference databaseReference;                              // Stores a DatabaseReference object for firebase use.
    EditText usernameText,passwordText;                              // Stores EditText objects for the username and password fields.
    Spinner spinner;                                                // Stores a Spinner object representing the spinner containing the user types.
    Button signUpButton,signInButton;                              // Stores a Button object representing the sign up and sign in buttons.
    ArrayList<User> users;                                        // ArrayList that stores all the users present in the database
    boolean passwordValid;                                       // boolean used for password validation when signing in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        users = new ArrayList<User>();
        passwordValid=false;

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
                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure that none of the fields are empty!", Toast.LENGTH_SHORT).show();
                }

                // If the user wants to create an admin account and the username and password fields are both not "admin", app prints so
                else if (userType.equals("Admin") && (!(username.equals("admin") && password.equals("admin")))) {

                        Toast.makeText(getApplicationContext(), "Invalid input. The admin account can have a username: admin and password: admin only!", Toast.LENGTH_SHORT).show();
                }

                // If the user the user is trying to create an account that exists, the app prints so
                else if (userExists()) {

                    // If input is invalid, display so
                    if (userType.equals("Admin")) {

                        Toast.makeText(getApplicationContext(), "Invalid input. An admin account already exists!", Toast.LENGTH_SHORT).show();
                    }
                     else {

                        Toast.makeText(getApplicationContext(), "Invalid input. The user already exists!", Toast.LENGTH_SHORT).show();
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
                    welcomeUser();
                }
                passwordValid=false;
            }
        });

        // Sign Up Button
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
                    welcomeUser();
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
     * This method reads the database and checks if the user exists in the database.
     * @return true if the user exists in the database, false otherwise.
     */
    public boolean userExists() {

        // boolean result used to return the result
        boolean result=false;

        // Connecting to the database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
                Toast.makeText(getApplicationContext(), "Error getting users list from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // For loop used to access all the users stored in the ArrayList users
        for (int i=0; i<users.size();i++) {

            if (users.get(i).getAccountType().equals(userType) && users.get(i).getUsername().equals(username)) {
                result=true;
                break;
            }
        }

        // For loop used to access all the users stored in the ArrayList users
        for (int i=0; i<users.size();i++) {

            if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password)) {

                passwordValid=true;
                break;
            }
        }

        return result;
    }

    /**
     * This method is called when the fields are validated in order to move to WelcomeScreenActivity or AdminWelcomeScreen
     */
    public void welcomeUser(){
        Intent intent;

        if (userType.equals("Admin")) {

            // Passing username and account type to WelcomeStreetActivity
            intent = new Intent(this, AdminWelcomeScreen.class);
            startActivity(intent);
        }

        else {
            // Passing username and account type to WelcomeStreetActivity
            intent = new Intent(this, WelcomeScreenActivity.class);
            intent.putExtra("USERNAME",username);
            intent.putExtra("ROLETYPE",userType);
            startActivity(intent);
        }
    }

    /**
     * This method is called when all the validation are successful and adds
     * the user to the database.
     */
    public void addUser() {

        // Creating a User object to add to the database
        User user =  new User(userType,username,password);

        // Connecting to the database, obtaining a unique id, and adding the user
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(user);
    }
}
