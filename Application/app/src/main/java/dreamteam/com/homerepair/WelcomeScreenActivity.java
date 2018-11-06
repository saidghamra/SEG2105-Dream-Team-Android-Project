package dreamteam.com.homerepair;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;


public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // Getting the username and roleType from SignUpActivity
        Intent intent = getIntent();
        String user = intent.getStringExtra("USERNAME");
        String userRole = intent.getStringExtra("ROLETYPE");

        // Updating the applications UI
        updateUI(user,userRole);
    }

    /**
     * Updates the user with a welcoming message.
     */

    public void updateUI(String username, String roleType){

        // Modifying the value of the text view to welcome the user
        TextView display = findViewById(R.id.welcomeText);
        display.setText("Welcome " + username + "! You are logged in as " + roleType + ".");
    }
}
