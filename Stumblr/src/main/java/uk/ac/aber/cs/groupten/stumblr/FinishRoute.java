package uk.ac.aber.cs.groupten.stumblr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Stack;

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

public class FinishRoute extends AbstractActivity {
    private Route route;

    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */
    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_finish_route);

        // TODO stuff with savedInstanceState

        // Receive Route object
        Bundle extras = getIntent().getExtras();
        route = (Route) extras.get("route");

        // Log a few messages just to make sure
        Log.v(TAG, route.getTitle());

        // FIXME NULL POINTER ON LN59
        for (Waypoint w : route.getWaypointList()) {
            Log.v(TAG, w.getLocation().toString());
        }
        Log.v(TAG, "Route list size: " + String.valueOf(route.getWaypointList().size()));
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
                if(response.getStatusLine().getStatusCode() == 200){
                    Toast.makeText(getBaseContext(), "File Uploaded Correctly!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getBaseContext(), "File Upload Failed!", Toast.LENGTH_LONG).show();
                }
                return response;
            } catch (Exception e) {
                Log.e(TAG, "IM NOT WORKING " + e.toString());
                Toast.makeText(getBaseContext(), "(Didn't) Upload file!", Toast.LENGTH_LONG).show();
                return null;
            }
        }
    }

    /**
     * Posts the data to the server. Check if internet is available first.
     */
    public void postData(View view) {
        if (checkInternetEnabled() == true || checkWifiEnabled() == true){
            new NetworkTask().execute();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Please Select")
                    .setMessage("Would you like to record another route?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })

                    .show();
        }
        else{
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
        JSONObject data = new JSONObject();
        try {
            JSONObject walk = new JSONObject();
            //Get data out of the Route object and add to the JSON package
            walk.put("walkTitle", route.getTitle());

            walk.put("shortDescription", route.getShortDesc());

            walk.put("longDescription", route.getLongDesc());

            walk.put("walkHours", route.getLengthTimeHours());

            walk.put("startTime", route.getStartTime());

            //walk.put("walkDistance", testRoute.getDistance());

            // Put the walk track into the JSON package
            JSONArray JSONCoordinates = new JSONArray();
            Stack<Location> coordinates =  route.getCoordinateList();
            for(int i = 0; i < coordinates.size(); i++){
                Location currentCoordinate = coordinates.get(i);
                JSONObject currentJSONCoordinate = new JSONObject();
                currentJSONCoordinate.put("Latitude", currentCoordinate.getLatitude());
                currentJSONCoordinate.put("Longitude", currentCoordinate.getLongitude());
                JSONCoordinates.put(i, currentJSONCoordinate);
            }
            walk.put("walkCoordinates", JSONCoordinates);

            //Add data for each waypoint into the JSON package
            JSONArray JSONWaypoints = new JSONArray();
            LinkedList<Waypoint> waypoints = route.getWaypointList();
            for(int i = 0; i < waypoints.size(); i++){ //TODO refactor this into a ForEach loop
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

            data.put("walk", walk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    // End HTTP POST

    /** Ensuring Network Provider is Enabled before submitting route
     *  REFERENCE - http://stackoverflow.com/questions/12806709/android-how-to-tell-if-mobile-network-data-is-enabled-or-disabled-even-when
     **/
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
            mobileDataEnabled = (Boolean)method.invoke(cm);

            Log.d("No exceptions thrown", "Network available");
            return mobileDataEnabled;

        }
        catch (Exception e){
       // if(e instanceof ClassNotFoundException || e instanceof NoSuchMethodException ||
         //       e instanceof IllegalAccessException || e instanceof InvocationTargetException )  {
            //Connectivity Issue Handling
   return mobileDataEnabled;
        }
       }

    public boolean checkWifiEnabled(){
        boolean wifiEnabled = false; //assume Wifi is disabled
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
           wifiEnabled = true;
            return wifiEnabled;
        }
        else{
            return wifiEnabled;
        }
    }




    /*
     * ****************************************************************
     *                         Base64 Encoding                        *
     * ****************************************************************
     */

    // REFERENCE - http://stackoverflow.com/questions/20656649/how-to-convert-bitmap-to-png-and-then-to-base64-in-android
    public void startBase64Intent(View v) {
        //  startActivity(new Intent())
    }

    public String encodeTobase64(Bitmap image)
    {
        Bitmap imagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.v(TAG, imageEncoded);
        return imageEncoded;
    }
}
