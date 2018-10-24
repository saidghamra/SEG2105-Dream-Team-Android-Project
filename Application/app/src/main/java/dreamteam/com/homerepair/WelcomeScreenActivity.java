package dreamteam.com.homerepair;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WelcomeScreenActivity extends AppCompatActivity {
    private DatabaseReference database;
    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        database = FirebaseDatabase.getInstance().getReference("users");

        // Getting the username and roleType from SignUpActivity
        Intent intent = getIntent();
        String user = intent.getStringExtra("USERNAME");
        String userRole = intent.getStringExtra("ROLETYPE");

        // Updating the applications UI
        updateUI(user,userRole);
    }

    /**
     * Updates the user with a welcoming message and if they're an admin, a list of all users in the system
     */

    public void updateUI(String username, String roleType){

        // Modifying the value of the text view to welcome the user
        TextView display = findViewById(R.id.welcomeText);
        display.setText("Welcome " + username + "! You are logged in as " + roleType + ".");

        // If the user is an admin, a list of all the users is displayed (BONUS)
        if(roleType.equals("Admin")){

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

                    // Method called to list all the users for the admin
                    listUsersForAdmin();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    // Displaying a toast if the application cant connect to the database
                    Toast.makeText(getApplicationContext(), "Error getting users list from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // This method populates the ListView listview with all the user accounts present in the database in a nice String format
    public void listUsersForAdmin() {

        // Used to store the strings that are going to be displayed in the ListView
        String[] toDisplay = new String[users.size()];

        // Populating String Array toDisplay with a nice string representation of the users and their functionality
        for(int i = 0; i < users.size(); i++){

            toDisplay[i] = (users.get(i).getAccountType() +" username: " + users.get(i).getUsername());
            System.out.println( toDisplay[i]);
        }

        // Configuring the ListView
        ListView listview = (ListView) findViewById(R.id.userLists);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,toDisplay);
        listview.setAdapter(adapter);
    }
}
