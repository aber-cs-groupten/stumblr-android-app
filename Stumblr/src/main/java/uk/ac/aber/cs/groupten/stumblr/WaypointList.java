package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

/**
 * http://developer.android.com/guide/topics/ui/layout/listview.html
 * For more information on creating a ListView
 */
public class WaypointList extends AbstractActivity {
    private ArrayAdapter<String> adapter; // TODO
    private LinkedList<String> menuItems;
    private ListView listView;
    private Route route;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_waypoint_list);

        route = new Route();

        if (savedInstanceState != null) {
            // do stuff
        }

        /*
         * See:
         * http://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65&aaid=90
         *
         * Also useful:
         * http://www.vogella.com/tutorials/AndroidListView/article.html
         */
        menuItems = new LinkedList<String>();

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
                    "Position : " + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
            }
        });
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

        if (requestCode == 3141 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Waypoint newWaypoint = (Waypoint) extras.get("data"); // This may be null - so test for null below

            if (newWaypoint != null) {
                // Update the route with the latest waypoint
                route.addWaypoint(newWaypoint);
                // Log message containing the name of the route
                Log.v(TAG, ("RESULT RETURNED: " + newWaypoint.getTitle()));

                // Redraw the list of Waypoints
                drawWaypointList();
            }
        }
    }

    /**
     * Renders Waypoint list on screen.
     */
    public void drawWaypointList() {
        for(Waypoint currentWaypoint : route.getWaypointList()){
            String currentTitle = currentWaypoint.getTitle();
            if(! menuItems.contains(currentTitle)){
                menuItems.add(currentTitle);
            }
        }

        if(! menuItems.isEmpty()){
            Log.v(TAG, ("Number of Waypoints: " + menuItems.size()));
        }

        adapter.notifyDataSetChanged();
        listView.refreshDrawableState();
    }

    /**
     * Starts the CreateWaypoint Intent, so that it returns a result.
     * @param v The View object.
     */
    public void startCreateWaypointIntent(View v) {
        startActivityForResult(new Intent(getApplicationContext(), CreateWaypoint.class), 3141);
    }
}
