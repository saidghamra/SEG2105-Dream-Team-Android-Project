package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.google.firebase.database.*;

import java.util.ArrayList;


public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // Array userTypes contains all the user types, used for the spinner
    String[] userTypes={"Admin", "Service Provider", "Home Owner"};
    String userType,username,password;

    DatabaseReference database;

    EditText usernameText,passwordText;
    Spinner spinner;
    Button signUpButton;

    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
            }
        });
    }

    // When an item is selected in the spinner
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

    // When no item is selected in the spinner
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Do nothing
    }

    // This method returns true if the user exists in the database. False otherwise.
    public boolean userExists() {

        // boolean result used to return the result
        boolean result=false;

        // Connecting to the database
        database = FirebaseDatabase.getInstance().getReference("users");
        database.addValueEventListener(new ValueEventListener() {

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
                Toast.makeText(getApplicationContext(), "Error getting users list from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // For loop used to access all the users stored in the ArrayList users
        for (int i=0; i<users.size();i++) {

            // If the user wants to create an admin account and an admin account already exists
            if (userType.equals("Admin") && users.get(i).getAccountType().equals("Admin")) {

                result=true;
                break;
            }

            // If the user wants to create a Home Owner or Service Provider account and the account already exists 9same username)
            else if (userType.equals("Home Owner") || userType.equals("Service Provider")) {

                if (users.get(i).getAccountType().equals(userType) && users.get(i).getUsername().equals(username)) {
                    result=true;
                    break;
                }
            }
        }

        return result;
    }

    // Called when the fields are validated in order to move to WelcomeScreenActivity
    public void welcomeUser(){

        // Passing username and account type to WelcomeStreetActivity
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        intent.putExtra("USERNAME",username);
        intent.putExtra("ROLETYPE",userType);
        startActivity(intent);
    }

    // Called when all the validation is successful and the user is ready to be added to the database
    public void addUser() {

        // Creating a User object to add to the database
        User user =  new User(userType,username,password);

        // Connecting to the database, obtaining a unique id, and adding the user
        database = FirebaseDatabase.getInstance().getReference("users");
        String id = database.push().getKey();
        database.child(id).setValue(user);
    }
}
