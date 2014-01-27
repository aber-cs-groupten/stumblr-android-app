package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
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

public class WaypointList extends AbstractActivity {
    private int WAYPOINT_INTENT = 3141;
    private ArrayAdapter<String> adapter;
    private LinkedList<String> menuItems;
    private ListView listView;
    private Route route;
    private String listEmptyString = "List is empty! Add a waypoint below...";

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_waypoint_list);

        //TODO it should be getting the route from the previous screen
        Bundle extras = getIntent().getExtras();
        route = (Route) extras.get("route");

        if (route == null) {
            Log.e(TAG, "Route object was null...");
            route = new Route();
            // TODO
        } else {
            Log.v(TAG, "Route title passed into WaypointList: " + route.getTitle());
        }

        /*
         * See: http://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65&aaid=90
         * and: http://developer.android.com/guide/topics/ui/layout/listview.html
         * Also useful: http://www.vogella.com/tutorials/AndroidListView/article.html
         */
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

        drawWaypointList();
    }

    /**
     * Passes the current Route object to FinishRoute and starts the activity.
     */
    public void finishRoute(View v) {
        Intent i = new Intent(getApplicationContext(), FinishRoute.class);
        i.putExtra("route", this.route);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WAYPOINT_INTENT && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Waypoint newWaypoint = (Waypoint) extras.get("data"); // This may be null - so test for null below

            if (newWaypoint != null) {
                // Update the route with the latest waypoint
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

    /**
     * Renders Waypoint list on screen.
     */
    public void drawWaypointList() {
        // Add each Waypoint to the list
        // TODO perhaps tweak this method... its complexity is greater than necessary
        if (route.getWaypointList() == null) {
            Log.e(TAG, "route.WaypointList returns null?!");
        }
        for(Waypoint currentWaypoint : route.getWaypointList()){
            String currentTitle = currentWaypoint.getTitle();
            if(! menuItems.contains(currentTitle)){
                menuItems.add(currentTitle);
            }
        }

        if(! menuItems.isEmpty()){
            Log.v(TAG, ("Number of Waypoints: " + menuItems.size()));
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
}
