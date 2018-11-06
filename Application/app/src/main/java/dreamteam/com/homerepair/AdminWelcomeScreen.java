package dreamteam.com.homerepair;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminWelcomeScreen extends AppCompatActivity {

    private DatabaseReference database;                             // Stores a DatabaseReference object for firebase use.
    private ArrayList<Service> services = new ArrayList<>();       // ArrayList containing all the services in the database.
    Button addButton;
    EditText serviceName,serviceRate;
    ListView list;
    String name, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome_screen);

        // Initializing EditTexts
        serviceName = findViewById(R.id.serviceName);
        serviceRate = findViewById(R.id.serviceRate);

        // Initializing the ListView
        list = (ListView) findViewById(R.id.servicesList);

        // Initializing the Add Button
        addButton = findViewById(R.id.add_button);

        updateList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = serviceName.getText().toString().trim();
                rate = serviceRate.getText().toString().trim();

                // Validating text fields
                if (name.equals("") || rate.equals("")) {

                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure that none of the fields are empty!", Toast.LENGTH_SHORT).show();
                }

                // If the text fields are valid, add the service to the database
                else {

                    addService(name,Integer.parseInt(rate));
                    updateList();

                    // Resetting the TextFields
                    serviceName.setText("");
                    serviceRate.setText("");
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Service service = services.get(position);
                System.out.println(service.toString());

                deleteService(service.getName());
            }
        });
    }


    /**
     * This method populates the list view with all the services present in the database.
     */
    private void updateList() {

        database = FirebaseDatabase.getInstance().getReference("services");

        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clearing the ArrayList users
                services.clear();

                // Getting all the users in the database
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    // Adding every user in the database to the ArrayList users
                    Service service = postSnapShot.getValue(Service.class);
                    services.add(service);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting services list from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////

        // Used to store the strings that are going to be displayed in the ListView
        String[] toDisplay = new String[services.size()];

        // Populating String Array toDisplay with a nice string representation of the services
        for(int i = 0; i < services.size(); i++){

            toDisplay[i] = (services.get(i).getName() + ": $" + services.get(i).getHourlyRate());
        }

        // Configuring the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,toDisplay);
        list.setAdapter(adapter);
    }

    /**
     * This method adds a service to the database.
     */
    private void addService(String name, int hourlyRate) {

        // Creating a new service
        Service service = new Service(name, hourlyRate);
        // Getting the database reference
        database = FirebaseDatabase.getInstance().getReference("services");
        String id = database.push().getKey();
        database.child(id).setValue(service);

        Toast.makeText(getApplicationContext(), "Added Service!", Toast.LENGTH_SHORT).show();

    }

    /**
     * This method updates a service in the database.
     */
    private void updateService(String id, String name, int hourlyRate) {

        // Getting the specified service reference
        database = FirebaseDatabase.getInstance().getReference("services").child(id);

        // Updating the service
        Service service = new Service(name, hourlyRate);
        database.setValue(service);

        Toast.makeText(getApplicationContext(), "Updated Service!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method deletes a service in the database.
     */
    private void deleteService(String id) {

        // Gettting the specified service reference
        database = FirebaseDatabase.getInstance().getReference("services").child(id);

        // Removing service
        database.removeValue();

        Toast.makeText(getApplicationContext(), "Deleted Service!", Toast.LENGTH_SHORT).show();
    }
}
