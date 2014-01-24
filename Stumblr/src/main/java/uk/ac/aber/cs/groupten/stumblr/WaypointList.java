package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import uk.ac.aber.cs.groupten.stumblr.data.Route;

/**
 * http://developer.android.com/guide/topics/ui/layout/listview.html
 * For more information on creating a ListView
 */
public class WaypointList extends AbstractActivity {
    ListView listView;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_abstract);

        if (savedInstanceState == null) {
            // do stuff
        }

        /* See:
         * http://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65&aaid=90
         *
         * Also useful:
         * http://www.vogella.com/tutorials/AndroidListView/article.html
         */
        setContentView(R.layout.activity_waypoint_list);
        listView = (ListView) findViewById(R.id.listView);

        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    /**
     * Renders Waypoint list on screen.
     * @param r The Route object containing Waypoints to render.
     */
    public void drawWaypointList(Route r) {
        // Do nothing currently
    }

    /**
     *
     * @param v
     */
    public void startCreateWaypointIntent(View v) {
        startActivity(new Intent(getApplicationContext(), CreateWaypoint.class));
    }
}
