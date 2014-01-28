package uk.ac.aber.cs.groupten.stumblr;

import android.content.BroadcastReceiver;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.EmptyStackException;
import java.util.LinkedList;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class WaypointList extends AbstractActivity implements LocationListener {
    // Constants
    private final int WAYPOINT_INTENT = 3141;

    // GPS broadcast receiver
    private BroadcastReceiver receiver;
    private Intent gpsServiceIntent;
    private boolean serviceRunning = false;

    // ListView objects
    private ArrayAdapter<String> adapter;
    private LinkedList<String> menuItems;
    private ListView listView;

    // Data objects
    private Route route;
    private final String listEmptyString = "List is empty! Add a waypoint below...";

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_waypoint_list);

        // Receive Route object
        Bundle extras = getIntent().getExtras();
        route = (Route) extras.get("route");

        // Timestamp
        route.setStartTime();

        // Check for null
        if (route != null) {
            initialiseListView(); // Sets up all of the variables necessary for the ListView
            drawWaypointList(); // Renders Waypoint list
            startGPSService(); // Starts GPS service and sets up notification
        } else {
            Log.e(TAG, "Route object was null!");
        }
    }

    /**
     * Called when an Activity is dispatched for a result
     * @param requestCode Integer relating the intent to a request
     * @param resultCode Used to check if a request was successful
     * @param data The Intent used
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WAYPOINT_INTENT && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Waypoint newWaypoint = (Waypoint) extras.get("data"); // This may be null

            if (newWaypoint != null) { // Test for null
                // Update the route with the latest Waypoint
                route.addWaypoint(newWaypoint);

                // Log message containing the name of the route
                Log.v(TAG, ("RESULT RETURNED: " + newWaypoint.getTitle()));

                // Redraw the list of Waypoints
                if (menuItems.getFirst().equals(listEmptyString)) {
                    menuItems.remove(listEmptyString);
                }
                drawWaypointList();
            }
        }
    }

    // GPS Service interaction
    /**
     * Starts the GPS service.
     */
    private void startGPSService() {
        gpsServiceIntent = new Intent(this, GPSService.class);
        startService(gpsServiceIntent);

        IntentFilter filter = new IntentFilter(); // Filter for the correct intent
        filter.addAction(GPSService.INTENT_STRING);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                Location loc = (Location) b.get(GPSService.LOC_BUNDLE_STRING);

                if (loc != null) { // Test for null
                    route.addCoordinate(loc);
                }
            }
        };

        registerReceiver(receiver, filter);
        serviceRunning = true;
    }

    /**
     * Halts the GPS service.
     */
    private void stopGPSService() {
        if (serviceRunning == true) {
            stopService(gpsServiceIntent);
            unregisterReceiver(receiver);
        }
        serviceRunning = false;
    }

    // ListView interaction
    /**
     * Initialises the ListView and Adapter objects.
     * See: http://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65&aaid=90
     * and: http://developer.android.com/guide/topics/ui/layout/listview.html
     * Also useful: http://www.vogella.com/tutorials/AndroidListView/article.html
     */
    private void initialiseListView() {
        // Initialise list of Strings to display in
        menuItems = new LinkedList<String>();
        menuItems.add(listEmptyString);

        // Set ArrayAdapter and ListView up
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, menuItems);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position : " + position + "  ListItem : " + itemValue, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Waypoint stuff
    /**
     * Renders Waypoint list on screen.
     */
    public void drawWaypointList() {
        // Add each Waypoint to the list
        // FIXME

        listView.clearChoices();
        int i = 1;
        for(Waypoint currentWaypoint : route.getWaypointList()){
            String currentTitle = currentWaypoint.getTitle();
            menuItems.add(i + ": " + currentTitle);
            i++;
        }

        adapter.notifyDataSetChanged(); // Make sure that the adapter knows there is new data
        listView.refreshDrawableState(); // Redraw the list on-screen
    }

    /**
     * Starts the CreateWaypoint Intent, so that it returns a result.
     * @param v The View object.
     */
    public void startCreateWaypointIntent(View v) {
        Intent cwi = new Intent(getApplicationContext(), CreateWaypoint.class);

        try {
            // Pop latest coordinate from stack and apply to Waypoint
            cwi.putExtra("loc", route.getCoordinateList().peek());
            // Begin activity
            startActivityForResult(cwi, WAYPOINT_INTENT);
        } catch (EmptyStackException ese) {
            Log.i(TAG, "No Locations currently in Route. Probably no GPS fix yet.");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Waiting for location...")
                    .setMessage("Hold on, we don't know where you are yet!")
                    .setPositiveButton("OK", null).show();
        }
    }

    // Location interaction
    /**
     * Obtain coordinates from Android system and add to current Waypoint.
     * Adapted from: https://sites.google.com/site/androidhowto/how-to-1/using-the-gps
     */
    @Override
    public void onLocationChanged(Location loc) {
        route.addCoordinate(loc);
    }

    /**
     * Prompt for GPS, and error handling
     * See: http://hedgehogjim.wordpress.com/2013/03/20/programmatically-enable-android-location-services/
     */
    @Override
    public void onProviderDisabled(String s) {
        // Build alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services!");

        // Set actions
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // Create alert
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}

    // Finishes the screen
    /**
     * See: http://stackoverflow.com/a/2258147
     */
    public void confirmFinishRoute(View v) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Finish Route")
                .setMessage("Are you sure you want to finish recording the route?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WaypointList.this.finishRoute();
                        WaypointList.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Passes the current Route object to FinishRoute and starts the activity.
     * @param v The View object passed in by the Android OS.
     */
    public void finishRoute() {
        // Calculate timestamp
        long startTime = route.getStartTime();
        long endTime = route.getCurrentTime();
        long timeLength = endTime - startTime;

        // Apply timestamp
        route.setLengthTime(timeLength);
        Log.e(TAG, (String.valueOf(timeLength)));

        // Start new intent, packaging current Route with it
        Intent i = new Intent(getApplicationContext(), FinishRoute.class);
        i.putExtra("route", this.route);

        // Clean up
        stopGPSService();
        startActivity(i);
    }
}
