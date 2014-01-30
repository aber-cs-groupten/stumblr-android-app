package uk.ac.aber.cs.groupten.stumblr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Stack;

import uk.ac.aber.cs.groupten.stumblr.data.Route;
import uk.ac.aber.cs.groupten.stumblr.data.Waypoint;

public class FinishRoute extends AbstractActivity {
    private Route route;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     *
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_finish_route);

        // Receive Route object
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            route = (Route) extras.get("route");

            // Log a few messages just to make sure
            Log.v(TAG, route.getTitle());

            //Total Distance TextView
            TextView textView1 = (TextView) findViewById(R.id.distanceVariable);
            float temp = route.getDistance();
            temp = Math.round(temp);
            textView1.setText(String.valueOf(temp));

            //Total Waypoints Text View
            int wpTemp = route.getWaypointList().size();
            TextView textView = (TextView) findViewById(R.id.numwpView);
            textView.setText(String.valueOf(wpTemp));

            for (Waypoint w : route.getWaypointList()) {
                Log.v(TAG, w.getLocation().toString());
            }

            Log.v(TAG, "Route list size: " + String.valueOf(route.getWaypointList().size()));
            Log.v(TAG, "Route distance: " + route.getDistance());
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

            // This server can be used for testing
            //HttpPost httppost = new HttpPost("http://www.parityb.it");

            // This is the real server
            HttpPost httppost = new HttpPost("http://users.aber.ac.uk/mal60/group_project/JSON_decode.php");

            try {
                JSONObject data = getData();
                StringEntity stringData = new StringEntity(data.toString());

                httppost.setEntity(stringData);
                httppost.setHeader("Content-Type", "application/json");
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                return response;
            } catch (Exception e) {
                Log.e(TAG, "IM NOT WORKING " + e.toString()); // FIXME proper log message
                return null;
            }
        }

        protected void onPostExecute(HttpResponse result) {
            if (result == null) {
                Toast.makeText(getBaseContext(), "Upload failed, server not available :(", Toast.LENGTH_LONG).show();
            } else if (result.getStatusLine().getStatusCode() == 200) {
                // Exit gracefully
                finish();

                Toast.makeText(getBaseContext(), "Upload Successful :)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Upload failed :(\nError: " + result.getStatusLine().getStatusCode(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Posts the data to the server. Check if internet is available first.
     */
    public void postData(View v) {
        if (checkInternetEnabled() || checkWifiEnabled()) {
            new NetworkTask().execute();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Unable to Access Internet");
            builder.setMessage("Please enable Internet Services or Wifi");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private JSONObject getData() {
        //Data to be sent:
        JSONObject walk = new JSONObject();
        try {
            //Get data out of the Route object and add to the JSON package
            walk.put("walkTitle", route.getTitle());
            walk.put("shortDescription", route.getShortDesc());
            walk.put("longDescription", route.getLongDesc());
            walk.put("walkHours", route.getLengthTimeHours());
            walk.put("totalDistance", route.getDistance());

            // Put the walk track into the JSON package
            JSONArray JSONCoordinates = new JSONArray();
            Stack<Location> coordinates = route.getCoordinateList();
            for (int i = 0; i < coordinates.size(); i++) {
                Location currentCoordinate = coordinates.get(i);
                JSONObject currentJSONCoordinate = new JSONObject();
                currentJSONCoordinate.put("latitude", currentCoordinate.getLatitude());
                currentJSONCoordinate.put("longitude", currentCoordinate.getLongitude());
                JSONCoordinates.put(i, currentJSONCoordinate);
            }

            // Put coordinates
            walk.put("walkCoordinates", JSONCoordinates);

            // FIXME
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Distance....")
                    .setMessage(String.valueOf(route.getDistance()))
                    .setPositiveButton("OK", null);

            //Add data for each waypoint into the JSON package
            JSONArray JSONWaypoints = new JSONArray();
            LinkedList<Waypoint> waypoints = route.getWaypointList();

            for (int i = 0; i < waypoints.size(); i++) {
                Waypoint currentWaypoint = waypoints.get(i);
                JSONObject currentJSONWaypoint = new JSONObject();
                currentJSONWaypoint.put("title", currentWaypoint.getTitle());
                currentJSONWaypoint.put("description", currentWaypoint.getShortDesc());
                currentJSONWaypoint.put("timestamp", currentWaypoint.getTimestamp());
                currentJSONWaypoint.put("latitude", currentWaypoint.getLocation().getLatitude());
                currentJSONWaypoint.put("longitude", currentWaypoint.getLocation().getLongitude());

                //Get Image and Convert to base64
                Bitmap image = currentWaypoint.getImage();
                if (image != null) {
                    String base64Image = encodeTobase64(image);
                    currentJSONWaypoint.put("image", base64Image);
                }
                JSONWaypoints.put(i, currentJSONWaypoint);
            }

            walk.put("waypoints", JSONWaypoints);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        writeFile(walk.toString());
        return walk;
    }

    // End HTTP POST

    /**
     * Ensuring Network Provider is Enabled before submitting route
     * REFERENCE - http://stackoverflow.com/questions/12806709/android-how-to-tell-if-mobile-network-data-is-enabled-or-disabled-even-when
     */
    public boolean checkInternetEnabled() {

        boolean mobileDataEnabled = false; // Assume disabled
        Context context;
        context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d("Connectivity Service", "Getting System Context");
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(cm);

            Log.d("No exceptions thrown", "Network available");
            return mobileDataEnabled;

        } catch (Exception e) {
            // if(e instanceof ClassNotFoundException || e instanceof NoSuchMethodException ||
            //       e instanceof IllegalAccessException || e instanceof InvocationTargetException )  {
            //Connectivity Issue Handling
            return mobileDataEnabled;
        }
    }

    public boolean checkWifiEnabled() {
        boolean wifiEnabled = false; //assume Wifi is disabled
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            wifiEnabled = true;
        }
        return wifiEnabled;
    }

    /*
     * ****************************************************************
     *                         Base64 Encoding                        *
     * ****************************************************************
     */
    // REFERENCE - http://stackoverflow.com/questions/20656649/how-to-convert-bitmap-to-png-and-then-to-base64-in-android
    public String encodeTobase64(Bitmap image) {
        Bitmap imagex = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean success = imagex.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        if(!success){
            Toast.makeText(getBaseContext(), "Base64 Encode Failed!", Toast.LENGTH_LONG).show();
        }
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.v(TAG, imageEncoded);
        return imageEncoded;
    }

    // File writing
    public void writeFile(String s) {
        try {
            Context c = this;
            FileOutputStream fos = c.openFileOutput("JSON.txt", Context.MODE_PRIVATE);
            OutputStreamWriter oos = new OutputStreamWriter(fos);

            // Write and close
            oos.write(s);
            oos.close();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, fnfe.getMessage());
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }
    }
}
