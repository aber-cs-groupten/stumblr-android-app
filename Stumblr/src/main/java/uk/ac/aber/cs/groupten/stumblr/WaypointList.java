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

/**
 * http://developer.android.com/guide/topics/ui/layout/listview.html
 * For more information on creating a ListView
 */
public class WaypointList extends AbstractActivity {
    private LinkedList<String> menuItems;// = new LinkedList<String>();
    private ListView listView;
    private Route route;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */

    //                  V THIS IS TEST DATA V
    Route route = new Route("Test Title", "Test Short Description", "Test Long Description");


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
        listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, menuItems);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                //If the "Add Waypoint" button is pushed, start the Create Waypoint screen
                if(itemValue.equals("Add Waypoint")){
                    startActivityForResult(new Intent(getApplicationContext(), CreateWaypoint.class), 3141);
                }
                else{
                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position : " + itemPosition + "  ListItem : " + itemValue,Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
    }

    /**
     * Passes the current Route object to FinishRoute and starts the activity.
     */
    public void finishRoute() {
        Intent i = new Intent();
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
            }
        }
    }

    /**
     * Renders Waypoint list on screen.
     * @param r The Route object containing Waypoints to render.
     */
    public void drawWaypointList(Route r) {
        // Do nothing currently
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //When the screen is viewed, update the list and add the "Add Waypoint" button to the end
        if(hasFocus){

            //THIS IS TEST DATA
            /*Waypoint testWaypoint1 = new Waypoint("Test1", "Test1Desc");
            Waypoint testWaypoint2 = new Waypoint("Test2", "Test2Desc");
            route.addWaypoint(testWaypoint1);
            route.addWaypoint(testWaypoint2);
            route.addWaypoint(testWaypoint2);*/
            //ENDS TEST DATA

            LinkedList<Waypoint> waypoints = route.getWaypointList();

            for(Waypoint currentWaypoint: waypoints){
                String currentTitle = currentWaypoint.getTitle();
                if(!menuItems.contains(currentTitle)){
                    menuItems.add(currentTitle);
                }
            }

            if(!menuItems.isEmpty()){
                Log.v(TAG, ("Last menuItems Value " + menuItems.getLast()));
            }

            menuItems.add("Add Waypoint");
        }
        //When the screen is un-viewed remove the "Add Waypoint" button
        else{
            menuItems.removeLast();
        }
    }

    /**
     *
     * @param v
     */
    /*public void startCreateWaypointIntent(View v) {
        startActivity(new Intent(getApplicationContext(), CreateWaypoint.class));
    }*/
}
