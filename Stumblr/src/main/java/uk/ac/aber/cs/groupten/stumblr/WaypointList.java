package uk.ac.aber.cs.groupten.stumblr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.EmptyStackException;
import java.util.LinkedList;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class WaypointList extends AbstractActivity {

    /**
     * Waypoint intent constant.
     */
    private final int WAYPOINT_INTENT = 3141;

    /**
     * Index to insert new waypoint.
     */
    private int insert_index;

    /**
     * GPS Broadcast reciever.
     */
    private BroadcastReceiver receiver;

    /**
     * GPS Service intent.
     */
    private Intent gpsServiceIntent;

    /**
     * Boolean to show if GPS service is running.
     */
    private boolean serviceRunning = false;

    // ListView objects
    /**
     * Adapter used to put menu items into linked list
     */
    private ArrayAdapter<Waypoint> adapter;

    /**
     * List of menu items.
     */
    private LinkedList<Waypoint> menuItems;

    /**
     * View of the list.
     */
    private ListView listView;

    /**
     * Instance of route.
     */
    private Route route;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     *
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_waypoint_list);

        // Receive Route object
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            route = (Route) extras.get("route"); // May be null, check below

            // Timestamp
            route.setStartTime();

            if (route != null) {
                initialiseListView(); // Sets up all of the variables necessary for the ListView
                drawWaypointList(); // Renders Waypoint list
                startGPSService(); // Starts GPS service and sets up notification
            } else {
                Log.e(TAG, "Route object was null!");
            }
        }
    }

    /**
     * Starts the CreateWaypoint Intent, so that it returns a result.
     *
     * @param v The View object.
     */
    public void startCreateWaypointIntent(View v) {
        Intent cwi = new Intent(getApplicationContext(), CreateWaypoint.class);

        try {
            // Pop latest coordinate from stack and apply to Waypoint
            cwi.putExtra(CreateWaypoint.LOCATION_BUNDLE, route.getCoordinateList().peek());
            startActivityForResult(cwi, WAYPOINT_INTENT);

            // Set index to the end of the list
            insert_index = route.getWaypointList().size();

        } catch (EmptyStackException ese) {
            Log.i(TAG, "No Locations currently in Route. Probably no GPS fix yet.");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Waiting for location...")
                    .setMessage("Hold on, we don't know where you are yet!")
                    .setPositiveButton("OK", null).show();
        }
    }

    /**
     * Calculate time by subtracting the endTime by the startTime.
     * Set the length route was recorded for to the route.
     */
    private void calculateTimestamp() {
        long startTime = route.getStartTime();
        long endTime = route.getCurrentTime();
        long timeLength = endTime - startTime;
        Log.e(TAG, (String.valueOf(timeLength)));

        route.setLengthTime(timeLength);
    }

    /**
     * Called when an Activity is dispatched for a result
     *
     * @param requestCode Integer relating the intent to a request
     * @param resultCode  Used to check if a request was successful
     * @param data        The Intent used
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WAYPOINT_INTENT && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Waypoint newWaypoint = (Waypoint) extras.get(CreateWaypoint.RETURN_BUNDLE); // This may be null

            if (newWaypoint != null) { // Test for null
                // Update the route with the latest Waypoint
                Log.e(TAG, "Size: " + String.valueOf(route.getWaypointList().size()) + " Index: " + String.valueOf(insert_index));
                LinkedList<Waypoint> wps = route.getWaypointList();

                if (insert_index < wps.size()) { // Replace if already exists
                    wps.remove(insert_index);
                }

                wps.add(insert_index, newWaypoint);

                // Log message containing the name of the route
                Log.v(TAG, ("RESULT RETURNED: " + newWaypoint.getTitle()));

                drawWaypointList();
            }
        }
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
        menuItems = new LinkedList<Waypoint>();

        // Set ArrayAdapter and ListView up
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<Waypoint>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, menuItems);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Waypoint w = (Waypoint) listView.getItemAtPosition(position);

                // Using getBaseContext(). not sure if this should work
                insert_index = position;
                Intent i = new Intent(getBaseContext(), CreateWaypoint.class);
                i.putExtra(CreateWaypoint.WAYPOINT_BUNDLE, w);
                startActivityForResult(i, WAYPOINT_INTENT);
            }
        });
    }

    // Waypoint stuff

    /**
     * Renders Waypoint list on screen.
     */
    public void drawWaypointList() {
        adapter.clear();
        listView.clearChoices();

        // Add each Waypoint to the list
        for (Waypoint currentWaypoint : route.getWaypointList()) {
            menuItems.addLast(currentWaypoint);
        }

        adapter.notifyDataSetChanged(); // Make sure that the adapter knows there is new data
        listView.refreshDrawableState(); // Redraw the list on-screen
    }


    // GPS Service interaction

    /**
     * Starts the GPS service.
     */
    private void startGPSService() {
        gpsServiceIntent = new Intent(this, GPSService.class);
        startService(gpsServiceIntent);

        IntentFilter filter = new IntentFilter(); // Filter for the correct intent
        filter.addAction(GPSService.GPS_INTENT);
        filter.addAction(GPSService.GPS_DIALOG);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GPSService.GPS_INTENT)) {
                    Bundle b = intent.getExtras();
                    Location loc = (Location) b.get(GPSService.LOC_BUNDLE_STRING);

                    if (loc != null) { // Test for null
                        route.addCoordinate(loc);
                    }

                } else if (intent.getAction().equals(GPSService.GPS_DIALOG)) {
                    Log.e(TAG, "GPS IS OFF");
                    promptToEnableGPS();
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
        if (serviceRunning) {
            stopService(gpsServiceIntent);
            unregisterReceiver(receiver);
        }
        serviceRunning = false;
    }

    /**
     * Display dialog to prompt the user to enable GPS. Then take them to the settings page.
     */
    public void promptToEnableGPS() {
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

    // Finishes the screen

    /**
     * Finish route method. Display message to check if the user is finished. If button is pressed
     * call finishRoute and then finish the activity.
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
     *
     * @param v The View object passed in by the Android OS.
     */
    public void finishRoute() {
        calculateTimestamp();

        // Checking waypoint list size and setting its textview.
        setContentView(R.layout.activity_finish_route);

        // Start new intent, packaging current Route with it
        Intent i = new Intent(getApplicationContext(), FinishRoute.class);
        i.putExtra("route", this.route);

        // Clean up
        stopGPSService();
        startActivity(i);
    }

    /**
     * onDestroy, stop the GPS service.
     */
    @Override
    public void onDestroy() {
        // Clean up
        stopGPSService();
        super.onDestroy();
    }

    /**
     * When instance state is saved, put instance variables into it as well as route data.
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "WaypointList: onSaveInstanceState");

        // Instance variables
        savedInstanceState.putInt("insert_index", insert_index);
        savedInstanceState.putBoolean("serviceRunning", serviceRunning);
        savedInstanceState.putParcelable("gpsServiceIntent", gpsServiceIntent);

        // Data
        savedInstanceState.putParcelable("route", route);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * When instance state is restored, retrieve instance variables and
     * data from saved instance state.
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG, "WaypointList: onRestoreInstanceState");

        // Instance variables
        insert_index = savedInstanceState.getInt("insert_index");
        serviceRunning = savedInstanceState.getBoolean("serviceRunning");
        gpsServiceIntent = savedInstanceState.getParcelable("gpsServiceIntent");

        // Data
        route = savedInstanceState.getParcelable("route");

        menuItems = new LinkedList<Waypoint>();
        for (Waypoint w : route.getWaypointList()) {
            menuItems.addLast(w);
        }

        initialiseListView();
        //drawWaypointList();
    }

    /**
     * Log when back is pressed.
     */
    @Override
    public void onBackPressed() {
        super.confirmCancel();
    }
}
