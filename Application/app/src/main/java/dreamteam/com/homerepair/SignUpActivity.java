package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;


public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // Array userTypes contains all the user types, used for the spinner
    String[] userTypes={"Admin", "Service Provider", "Home Owner"};
    String userType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,userTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Text Views
        final EditText username = (EditText) findViewById(R.id.userName);
        final EditText password = (EditText)findViewById(R.id.password);


        // Sign Up Button
        Button button = findViewById(R.id.signup_Button);
        button.setOnClickListener(new View.OnClickListener() { // On Sign Up Button click

            public void onClick(View v) {

                String test = username.getText().toString().trim();
                String test2 = password.getText().toString().trim();

                // Validating input
                if (!validateInput(test,test2)) {

                    // If input is invalid, display so
                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure that none of the fields are null!", Toast.LENGTH_SHORT).show();
                }

                else {

                    // Displaying toast informing the user that his account is being created
                    Toast.makeText(getApplicationContext(), "Creating an " + userType + " account...", Toast.LENGTH_SHORT).show();

                    if (userType.equals("Admin")) {

                        // Check if there is an admin, if not add the admin
                    }

                    else {

                        // add the user
                        addUser(test,test2);
                    }

                    // Welcoming user after successful account creation
                    welcomeUser(test,userType);
                }
            }
        });
    }

    // When an item is selected in the spinner
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

        // Setting the user type
        userType = userTypes[position];

        // Displaying toast with the user type selected
        Toast.makeText(getApplicationContext(), userTypes[position] + " Selected", Toast.LENGTH_SHORT).show();
    }

    // When no item is selected in the spinner
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Do nothing
    }

    // Validating the users input in both username and password fields
    public boolean validateInput(String username, String password) {
        // boolean result used to return whether the inputs are valid or not
        boolean result = true;

        // if any of the username and password text fields are null
        if (username.equals("") || password.equals("")) {

            result = false;
        }

        // WE SHOULD ALSO CHECK IF THE USER ALREADY EXISTS
        return result;
    }

    // Called when the fields are validated in order to move to WelcomeScreenActivity
    public void welcomeUser(String username, String userType){

        // Passing username and account type to WelcomeStreetActivity
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        intent.putExtra("USERNAME",username);
        intent.putExtra("ROLETYPE",userType);
        startActivity(intent);
    }

    // Called when all the validation is successful and the user is ready to be added
    public void addUser(String username, String password) {

        User user;

        if (userType.equals("Admin")) {

            user = new User(AccountType.ADMIN,username,password);
        }

        else if (userType.equals("Service Provider")) {

            user = new User(AccountType.SERVICE_PROVIDER,username,password);
        }

        else if (userType.equals("Home Owner")) {

            user = new User(AccountType.HOME_OWNER,username,password);
        }

        // ADD USER TO DATABASE
    }

}
