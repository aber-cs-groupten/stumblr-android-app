package uk.ac.aber.cs.groupten.stumblr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import uk.ac.aber.cs.groupten.stumblr.data.Route;

public class Home extends AbstractActivity {
    /**
     * Loads the activity on creation (using a bundle if one is present)
     * @param savedInstanceState The bundle containing the saved instance state.
     */

    TextView textLat;
    TextView textLong;

    public void stumblrOnCreate(Bundle savedInstanceState) {
        // Called by super().onCreate
        setContentView(R.layout.activity_home);

        textLat = (TextView)findViewById(R.id.textLat);
        textLong = (TextView)findViewById(R.id.textLong);

        LocationManager locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locLis = new myLocationListener();
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLis);

        if (savedInstanceState == null) {
            // do stuff
        }
    }

    class myLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            if(location != null)
            {
                double lng = location.getLongitude();
                double lat = location.getLatitude();

                textLat.setText(Double.toString(lat));
                textLong.setText(Double.toString(lng));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //implemented LocationListener Methods
        }

        @Override
        public void onProviderEnabled(String s) {
            //implemented LocationListener Methods
        }

        @Override
        public void onProviderDisabled(String s) {
            //implemented LocationListener Methods
        }


        // SENDING-POST //
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
        JSONObject data = new JSONObject();
        try {
            JSONObject walk = new JSONObject();
            walk.put("walkTitle", "Road Runner");
            walk.put("shortDescription", "meep meep!");
            walk.put("longDescription", "meep meep meep meep!");
            walk.put("walkHours", 42.0);
            walk.put("walkDistance", 42.0);
            JSONArray locations = new JSONArray();
            locations.put(1);
            locations.put(2);
            locations.put(3);
            locations.put(4);
            walk.put("locations", locations);
            data.put("walk", walk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
// END SENDING-POST //

    /**
     * Begin the Route entry activity
     */
    public void startCreateRouteIntent(View v) {
        startActivity(new Intent(getApplicationContext(), CreateRoute.class));
    }


// TAKING-PHOTO //
    /**
     * Start Camera activity
     */
    public void startCamera(View v) {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageView mImageView = new ImageView(getApplicationContext());
            Bundle extras = data.getExtras();
            Bitmap b = (Bitmap) extras.get("data");
            if (b != null) {
                mImageView.setImageBitmap(b);
            }
        }
    }
// END TAKING-PHOTO //
}
}
