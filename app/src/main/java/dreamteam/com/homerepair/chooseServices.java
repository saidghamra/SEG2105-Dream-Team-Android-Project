package dreamteam.com.homerepair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chooseServices extends AppCompatActivity {

    private ListView adminServices_List, serviceProviderServices_List;          // Stores two ListView objects that will display services offered by the admin and chosen by the service provider respectively
    private Button done;                                                       // Stores a Button object representing the button the service provider clicks when he's done adding services
    private ArrayList<String> adminServices,serviceProviderServices;          // Stores two ArrayList objects containing the services offered by the admin and the services chosen by the service provider respectively
    private DatabaseReference database;                                      // Stores a DatabaseReference object


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_services);

        // Initializing ArrayLists
        serviceProviderServices = new ArrayList<>();
        adminServices = new ArrayList<>();

        // Getting services offered by the admin from the database
        getServices();

        // Initializing the ListViews
        adminServices_List = findViewById(R.id.services_Offered);
        serviceProviderServices_List = findViewById(R.id.services_Chosen);
        updateListViews();

        // If an item inside the listView adminServices_List is clicked
        adminServices_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Removing the service from the services offered and adding it to the services the service provider chose
                serviceProviderServices.add(adminServices.get(position));
                adminServices.remove(adminServices.get(position));

                // Updating the ListView to reflect the changes
                updateListViews();

            }
        });

        // If an item inside the listView serviceProviderServices_List is clicked
        serviceProviderServices_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Removing the service from the services the service provider chose and adding it to the services the admin offers
                adminServices.add(serviceProviderServices.get(position));
                serviceProviderServices.remove(serviceProviderServices.get(position));

                // Updating the ListView to reflect the changes
                updateListViews();

            }
        });

        // Initializing Button
        done = (Button) findViewById(R.id.services_doneButton);

        // When the done button is clicked, send the ArrayList containing the services the service provider chose back to the other activity
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("SERVICESCHOSEN", serviceProviderServices);
                setResult(5,intent);
                finish();
            }
        });
    }

    /**
     * This method updates the ListViews containing the services offered
     * by the admin and the services chosen by the service provider respectively.
     */
    public void updateListViews() {

        // Updating adminServices_List ListView
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,adminServices);
        adminServices_List.setAdapter(adapter1);

        // Updating the serviceProviderServices_List ListView
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,serviceProviderServices);
        serviceProviderServices_List.setAdapter(adapter2);
    }


    /**
     * This method gets all the services the Admin has added to the databse
     * so the service provider can choose what services he provides.
     */
    private void getServices() {

        database = FirebaseDatabase.getInstance().getReference("services");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clearing the ArrayList adminServices
                adminServices.clear();

                // Getting all the services in the database and adding them to the ArrayList adminServices
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Service service = postSnapShot.getValue(Service.class);
                    adminServices.add(service.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Displaying a toast if the application cant connect to the database
                Toast.makeText(getApplicationContext(), "Error getting services list from firebase " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
