package uk.ac.aber.cs.groupten.stumblr.data;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.LinkedList;
import java.util.Stack;

public class Route extends StumblrData implements Parcelable {

    /**
     * startTime used for timestamp of start of the walk.
     */
    private long startTime;

    /**
     * lengthTime used for timestamp for length of walk.
     */
    private long lengthTime;

    /**
     * longDesc
     * A slightly longer description of the contents of the route. Set by the user when
     * they create a Route.
     */
    private String longDesc;

    /**
     * The list of Location objects that reflect the coordinates of the walk.
     */
    private Stack<Location> coordinates;

    /**
     * A LinkedList of Waypoint objects that the Route comprises of.
     */
    private LinkedList<Waypoint> route;

    /**
     * Constructor. Calls initRoute() to initialise route.
     */
    public Route() {
        initRoute();
    }

    /**
     * Helper method for constructor. Calls initLists() to initialise the list and stack.
     */
    private void initRoute() {
        initLists();
    }

    /**
     * List and stack initializer method.
     */
    private void initLists() {
        this.coordinates = new Stack<Location>();
        this.route = new LinkedList<Waypoint>();
    }

    /**
     * Initialise Route object from a Parcel.
     */
    public Route(Parcel p) {
        readFromParcel(p);
    }

    /**
     * @return The LinkedList of Waypoint objects.
     */
    public LinkedList<Waypoint> getWaypointList() {
        return this.route;
    }

    /**
     * @return The list of coordinates.
     */
    public Stack<Location> getCoordinateList() {
        return this.coordinates;
    }

    /**
     * Adds an item to the list of coordinates.
     *
     * @param location The Location object to add.
     */
    public void addCoordinate(Location location) {
        this.coordinates.push(location);
    }

    /**
     * Returns the long description of the Route.
     *
     * @return The long description of the Route.
     */
    public String getLongDesc() {
        return this.longDesc;
    }

    /**
     * Sets the long description.
     *
     * @param longDesc A longer description of the Route.
     */
    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }


    /**
     *
     * @return returns the route distance.
     *
     * Method loops through coordinate list. If list is empty, return 0.0f. Sets current location by calling getLatitude()
     * and getLongitude() methods. On each loop, increment i and set currentLoc to coordinates
     * held in position i of coordinates list. Do same for nextLoc but add 1 so it retrieves the
     * coordinates of the position in front of current location. Using distanceBetween() it
     * calculates distance between the two locations and add results to distance variable.
     */
    public float getDistance() {
        float distance = 0;
        float[] results = new float[1];
        Location currentLoc;
        Location nextLoc;

        if (coordinates.size() <= 1) {
            return 0.0f;
        }

        for (int i = 0; i < (coordinates.size() - 1); i++) {
            currentLoc = coordinates.get(i);
            nextLoc = coordinates.get(i + 1);

            Location.distanceBetween(currentLoc.getLatitude(),
                    currentLoc.getLongitude(),
                    nextLoc.getLatitude(),
                    nextLoc.getLongitude(),
                    results);

            distance += results[0];
        }

        Log.v(TAG, "TOTAL DISTANCE: " + String.valueOf(distance));
        return distance;
    }

    /**
     * Describes contents of parcel.
     *
     * @return Description
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the Route into a Parcel for moving between screens.
     *
     * @param parcel The parcel to be written to.
     * @param i      Flags.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getTitle());
        parcel.writeString(this.getShortDesc());
        parcel.writeString(this.getLongDesc());
        parcel.writeLong(this.getStartTime());
        parcel.writeLong(this.getLengthTime());

        parcel.writeList(this.route);
        parcel.writeList(this.coordinates);
    }

    /**
     * Reads Route data from a parcel.
     *
     * @param inParcel The parcel to read the Route from.
     */
    public void readFromParcel(Parcel inParcel) {
        this.setTitle(inParcel.readString());
        this.setShortDesc(inParcel.readString());
        this.setLongDesc(inParcel.readString());
        this.setStartTime(inParcel.readLong());
        this.setLengthTime(inParcel.readLong());

        initLists();
        inParcel.readList(this.route, Waypoint.class.getClassLoader());
        inParcel.readList(this.coordinates, Location.class.getClassLoader());
    }

    /**
     * Private class that helps create a Parcelable.
     * From: http://stackoverflow.com/a/18167140
     */
    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    /**
     * @return startTime of the walk
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * startTime gets set to current time.
     */
    public void setStartTime() {
        this.startTime = getCurrentTime();
    }

    /**
     * @param start pass in start time
     */
    public void setStartTime(long start) {
        this.startTime = start;
    }

    /**
     * @return lengthTime variable
     */
    public long getLengthTime() {
        return lengthTime;
    }

    /**
     * @param l pass in length time and set lengthTime to it.
     */
    public void setLengthTime(long l) {
        this.lengthTime = l;
    }

    /**
     * @return lengthTime as a casted float divided to be hours.
     */
    public float getLengthTimeHours() {
        return ((float) lengthTime / 3600000);
    }
}
