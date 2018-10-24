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
    private ArrayList<User> users = new ArrayList<User>();

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



        //UPDATE UI TEXT VIEW
        if(roleType.equals("Admin")){

            database.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // Clearing the ArrayList users
                    users.clear();

                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                        User user = postSnapShot.getValue(User.class);
                        users.add(user);
                        //users.add(user.getUsername()+":");
                    }

                    //listUsersForAdmin();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    // Displaying a toast if the application cant connect to the database
                    Toast.makeText(getApplicationContext(), "Error getting users list from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void listUsersForAdmin() {

//        ArrayList<String> toprint = new ArrayList<String>();
//
//        for(int i = 0; i < users.size(); i++){
//
//            toprint.add(users.get(i).getUsername()+":" + users.get(i).getAccountType());
//        }

        ListView listview = (ListView) findViewById(R.id.userLists);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,users);

        //listview.setAdapter(adapter);
    }
}
