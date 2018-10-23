package dreamteam.com.homerepair;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WelcomeScreenActivity extends AppCompatActivity {
    private boolean isAdmin = false;
    private static final String WELCOME_MESSAGE = "Welcome {USERNAME}, you are logged in as a {ACCOUNT_TYPE}.";
    private static final String TAG = "sgagb074";
    private static DatabaseReference usersRef = null;
    private User currentUser;
    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        currentUser = createTestUser();


        // SAIDS CODE. THE CODE I ADDED GETS THE USERNAME AND ROLE TYPE FROM THE INTENT TO PRINT TO THE TEXTBOX
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Getting the username from MainActivity
        Intent intent = getIntent();
        String user = intent.getStringExtra("USERNAME");
        String userRole = intent.getStringExtra("ROLETYPE");

        // Modifying the value of the text view
        TextView display = findViewById(R.id.welcomeText);
        display.setText("Welcome " + user + "! You are logged as " + userRole + ".");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // END OF SAIDS CODE

        // When the database is reinitialized we can update the text view throught the updateUI method
        //updateUI();
    }

    /**
     * Updates the user with a welcoming message and if they're an admin, a list of all users in the system
     */

    public void updateUI(){
        //Creating welcome message for user

        String customWelcomeMessage = WELCOME_MESSAGE.replace("{USERNAME}", currentUser.getUsername());
        String accountType = "";
        switch (currentUser.getAccountType()){
            case ADMIN:
                accountType = "Admin";
                break;
            case HOME_OWNER:
                accountType = "Home Owner";
                break;
            case SERVICE_PROVIDER:
                accountType = "Service Provider";
                break;
            default:
                accountType = "unknown";
                break;
        }
        customWelcomeMessage = customWelcomeMessage.replace("{ACCOUNT_TYPE}", accountType);
        //UPDATE UI TEXT VIEW
        Log.d(TAG, customWelcomeMessage);
        if(currentUser.getAccountType() == AccountType.ADMIN){
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Converts the data from the JSON format in Firebase into an array of user objects
                    GenericTypeIndicator<HashMap<String,User>> hashMapOfUsersType = new GenericTypeIndicator<HashMap<String,User>>() {};
                    HashMap<String,User> hashMapOfUser = dataSnapshot.getValue(hashMapOfUsersType);
                    String[] hashMapKeys = hashMapOfUser.keySet().toArray(new String[hashMapOfUser.size()]);
                    for(int i = 0; i < hashMapKeys.length; i++){
                        users.add(hashMapOfUser.get(hashMapKeys[i]));
                        Log.d(TAG, hashMapOfUser.get(hashMapKeys[i]).getUsername());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Error getting users list from firebase " + databaseError.toString());
                }
            });
        }
    }

    /**
     * Returns a user (Method used for testing, will be removed before submission
     * @return the created user
     */

    public User createTestUser(){
        User testUser = new User(AccountType.ADMIN,"santos", "randPas!word");
        return testUser;
    }
}
