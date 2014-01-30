package uk.ac.aber.cs.groupten.stumblr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.StumblrData;

public class CreateRoute extends AbstractActivity {
    private Route route;

    private String title;
    private String shortDesc;
    private String longDesc;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     *
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    @Override
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_create_route);

        // Create new blank Route object
        route = new Route();
    }

    public void getTextFromUI() {
        // Get text from fields in UI
        title = ((TextView) findViewById(R.id.routeTitleBox)).getText().toString();
        shortDesc = ((TextView) findViewById(R.id.shortDescriptionBox)).getText().toString();
        longDesc = ((TextView) findViewById(R.id.longDescriptionBox)).getText().toString();

        // Sanitise the Inputs
        title = route.sanitiseStringInput(title);
        shortDesc = route.sanitiseStringInput(shortDesc);
        longDesc = route.sanitiseStringInput(longDesc);
    }

    /**
     * Start the WaypointList activity (list the current Route).
     * Called when the "next" button is clicked in the UI.
     */
    public void startWaypointListIntent(View v) {
        getTextFromUI();

        // Check the length of text fields
        if (StumblrData.isValidData(title)) {
            if (StumblrData.isValidData(shortDesc)) {
                // Set parameters of current Route object
                route.setTitle(title);
                route.setShortDesc(shortDesc);
                route.setLongDesc(longDesc);

                // Start the new Activity
                Intent i = new Intent(getApplicationContext(), WaypointList.class);
                i.putExtra("route", route);
                startActivity(i);

                // Clear this activity
                finish();
            } else {
                Toast.makeText(getBaseContext(),
                        "The short description is too short. It must be > 3 characters.",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getBaseContext(),
                    "The title is too short. It must be > 3 characters.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        getTextFromUI();

        Log.i(TAG, "CreateRoute: onSaveInstanceState");

        savedInstanceState.putString("title", title);
        savedInstanceState.putString("shortDesc", shortDesc);
        savedInstanceState.putString("longDesc", longDesc);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG, "CreateRoute: onRestoreInstanceState");

        title = savedInstanceState.getString("title");
        shortDesc = savedInstanceState.getString("shortDesc");
        longDesc = savedInstanceState.getString("longDesc");

        ((TextView) findViewById(R.id.routeTitleBox)).setText(title);
        ((TextView) findViewById(R.id.shortDescriptionBox)).setText(shortDesc);
        ((TextView) findViewById(R.id.longDescriptionBox)).setText(longDesc);
    }

    @Override
    public void onBackPressed() {
        // Ignore
        Log.v(TAG, "Back pressed in CreateRoute. Ignoring...");
    }
}