package dreamteam.com.homerepair;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

        // Updates the ListView on first start
        updateDatabase();

        // Adding a service functionality
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting textField Values
                name = serviceName.getText().toString().trim();
                rate = serviceRate.getText().toString().trim();

                // Validating text fields
                if (name.equals("") || rate.equals("")) {

                    Toast.makeText(getApplicationContext(), "Invalid input. Please make sure that none of the fields are empty!", Toast.LENGTH_SHORT).show();
                }

                // If the text fields are valid, add the service to the database
                else {

                    addService(name,Integer.parseInt(rate));

                    // Resetting the TextFields
                    serviceName.setText("");
                    serviceRate.setText("");
                }
            }
        });

        // Pressing on an item in the list functionality
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // PROBLEM MIGHT BE HERE FOR UPDATING A SERVICE BECAUSE OF THE WAY WE'RE GIVING THE METHOD THE ID
                Service service = services.get(position);
                showUpdateDeleteDialog(service.getId(), service.getName(), service.getHourlyRate());
            }
        });
    }

    /**
     * This method populates the list view with all the services present in the database.
     */
    private void updateDatabase() {

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

                updateList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting services list from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method updates the list used to populate the ListView after
     * every database update.
     */
    public void updateList() {

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

        // Getting the database reference and adding the service
        database = FirebaseDatabase.getInstance().getReference("services");
        String id = database.push().getKey();

        // Creating a new service
        Service service = new Service(name, hourlyRate, id);

        database.child(id).setValue(service);

        Toast.makeText(getApplicationContext(), "Added Service!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method updates an existing service in the database.
     * @param id the id of the service in the database
     * @param name the name of the service
     * @param hourlyRate the hourly rate of the service
     */
    private void updateService(String id, String name, int hourlyRate) {

        // Getting the specified service reference
        database = FirebaseDatabase.getInstance().getReference("services").child(id);

        // Updating the service
        Service service = new Service(name, hourlyRate, id);
        database.setValue(service);

        Toast.makeText(getApplicationContext(), "Updated Service!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method deletes a service from the database.
     * @param id the id of the service in the database.
     */
    private void deleteService(String id) {

        // Gettting the specified service reference
        database = FirebaseDatabase.getInstance().getReference("services").child(id);

        // Removing service
        database.removeValue();

        Toast.makeText(getApplicationContext(), "Deleted Service!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is responsible for building the dialog that pops up whenever
     * an item in the ListView is clicked. The pop up dialog is used to update
     * or delete services.
     *
     * @param id The id of the service selected in the database
     * @param productName The name of the service selected
     * @param rateOld The old rate of the service
     */
    private void showUpdateDeleteDialog(final String id, final String productName, final int rateOld) {

        // Building the AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        // Setting up all the buttons and EditText's
        final EditText editName = (EditText) dialogView.findViewById(R.id.editServiceName);
        final EditText editRate  = (EditText) dialogView.findViewById(R.id.editServiceRate);
        final Button updateButton = (Button) dialogView.findViewById(R.id.updateServiceButton);
        final Button deleteButton = (Button) dialogView.findViewById(R.id.deleteServiceButton);

        dialogBuilder.setTitle(productName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // If the update button is clicked
        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Getting the EditText values
                String name = editName.getText().toString().trim();
                String rate = String.valueOf(editRate.getText().toString());

                // Since you can update only the rate or name of a service at one time, or update both at the same time
                if (name.equals("") && rate.equals("")) {

                    Toast.makeText(getApplicationContext(), "Please make sure at least one field is populated!", Toast.LENGTH_SHORT).show();
                }

                // If validation is successful, update the service
                else {

                    // If no Service name was entered, use the old service name and update the service rate
                    if (name.equals("")) {

                        updateService(id, productName, Integer.parseInt(rate));
                    }

                    // If no rate was entered, use the old rate and update the service name
                    else if(rate.equals("")) {

                        updateService(id, name, rateOld);
                    }

                    // If both fields are populated i.e. service name and rate change
                    else {

                        updateService(id, name, Integer.parseInt(rate));
                    }

                    b.dismiss();
                }
            }
        });

        // If the delete button is pressed
        deleteButton.setOnClickListener(new View.OnClickListener() {

            // Delete the service
            @Override
            public void onClick(View view) {
                deleteService(id);
                b.dismiss();
            }
        });
    }
}
