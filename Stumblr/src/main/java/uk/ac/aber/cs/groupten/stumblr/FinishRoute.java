package uk.ac.aber.cs.groupten.stumblr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public abstract class FinishRoute extends AbstractActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_abstract);

        if (savedInstanceState == null) {
            // Do stuff
        }
    }


    /*
     * ****************************************************************
     *                      HTTP POST Interaction                     *
     * ****************************************************************
     */
    private class NetworkTask extends AsyncTask<String, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.parityb.it");
            try {
                JSONObject data = getData();
                StringEntity stringData = new StringEntity(data.toString(4));

                httppost.setEntity(stringData);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                return response;
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
                return null;
            }
        }
    }


    /**
     * Posts the data to the server
     */
    public void postData(View view) {
        new NetworkTask().execute();
    }

    private JSONObject getData() {
        //Data to be sent:
        Route testRoute = new Route("Test Title", "Test Short Description", "Test Long Description");
        JSONObject data = new JSONObject();
        try {
            JSONObject walk = new JSONObject();
            //Get data out of the Route object and add to the JSON package
            walk.put("walkTitle", testRoute.getTitle());
            walk.put("shortDescription", testRoute.getShortDesc());
            walk.put("longDescription", testRoute.getLongDesc());
            //walk.put("walkHours", testRoute.getTime());
            //walk.put("walkDistance", testRoute.getDistance());
            //walk.put("walkCoordinates", testRoute.getCoordinates());
            JSONArray locations = new JSONArray();
            //Add data for each waypoint into the JSON package
            LinkedList<Waypoint> waypoints = testRoute.getWaypointList();
            for(int i = 0; i < waypoints.size(); i++){
                Waypoint currentWaypoint = waypoints.get(i);
                JSONObject currentJSONWaypoint = new JSONObject();
                currentJSONWaypoint.put("title", currentWaypoint.getTitle());
                currentJSONWaypoint.put("description", currentWaypoint.getShortDesc());
                //currentJSONWaypoint.put("coordinates", currentWaypoint.getCoordinates());
                //This may have to be base64
                currentJSONWaypoint.put("image", currentWaypoint.getImage());


                locations.put(i,currentJSONWaypoint);
            }
            walk.put("locations", locations);
            data.put("walk", walk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    // End HTTP POST


    /**
     * Will package and upload the current Route object.
     */
    public abstract void uploadRoute(Route r);
}
