package uk.ac.aber.cs.groupten.stumblr;

import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class WaypointList extends AbstractActivity implements LocationListener {
    // Constants
    private final int WAYPOINT_INTENT = 3141;

    // Location objects
    private LocationManager lm;

    // ListView objects
    private ArrayAdapter<String> adapter;
    private LinkedList<String> menuItems;
    private ListView listView;

    // Data objects
    private Route route;
    private String listEmptyString = "List is empty! Add a waypoint below...";

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

        // Set up location updates (this class implements a Listener)
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);

        initialiseListView(); // Sets up all of the variables necessary for the ListView
        drawWaypointList();
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
            Waypoint newWaypoint = (Waypoint) extras.get("data"); // This may be null - so test for null below

            if (newWaypoint != null) {
                // Update the route with the latest Waypoint
                route.addWaypoint(newWaypoint);
                // Log message containing the name of the route
                Log.v(TAG, ("RESULT RETURNED: " + newWaypoint.getTitle()));

                // Redraw the list of Waypoints
                if (menuItems.getFirst() == listEmptyString) {
                    menuItems.remove(listEmptyString);
                }
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
        menuItems = new LinkedList<String>();
        menuItems.add(listEmptyString);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, menuItems);

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

    /**
     * Renders Waypoint list on screen.
     */
    public void drawWaypointList() {
        // Add each Waypoint to the list
        // TODO tweak this method... its complexity is greater than necessary
        for(Waypoint currentWaypoint : route.getWaypointList()){
            String currentTitle = currentWaypoint.getTitle();
            if(! menuItems.contains(currentTitle)){
                menuItems.add(currentTitle);
            }
        }

        adapter.notifyDataSetChanged(); // Make sure that the adapter knows there is new data
        listView.refreshDrawableState(); // Redraw the list on-screen
    }

    /**
     * Starts the CreateWaypoint Intent, so that it returns a result.
     * @param v The View object.
     */
    public void startCreateWaypointIntent(View v) {
        startActivityForResult(new Intent(getApplicationContext(), CreateWaypoint.class), WAYPOINT_INTENT);
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

    // These methods aren't used yet
    // TODO @Martin may find these useful
    @Override
    public void onProviderDisabled(String s) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onStatusChanged(String s, int i, Bundle b) {}

    // Finishes the screen
    /**
     * Passes the current Route object to FinishRoute and starts the activity.
     * @param v The View object passed in by the Android OS.
     */
    public void finishRoute(View v) {
        lm.removeUpdates(this); // Stop receiving location updates - route is finished!

        // Start new intent, packaging current Route with it
        Intent i = new Intent(getApplicationContext(), FinishRoute.class);
        i.putExtra("route", this.route);
        startActivity(i);
    }
}
