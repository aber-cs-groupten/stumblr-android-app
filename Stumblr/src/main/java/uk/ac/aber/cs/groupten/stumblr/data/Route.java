package uk.ac.aber.cs.groupten.stumblr.data;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.Stack;

public class Route extends StumblrData implements Parcelable {
    /**
     * Timestamp for start of walk.
     */
    private long startTime;

    /**
     * Timestamp for end of walk.
     */
    private long endTime;

    /**
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
     * Constructor
     */
    public Route() {
        initRoute();
    }

    /**
     * Helper method for constructor
     */
    private void initRoute() {
        initLists(); // Initialise List and Stack
        // Timestamp
        startTime = getCurrentTime();
    }

    private void initLists() {
        // Initialise Lists
        this.coordinates = new Stack<Location>();
        this.route = new LinkedList<Waypoint>();
    }

    /**
     * Constructor for Route.
     * @param title The title of the Route object.
     * @param shortDesc A short description of the Route.
     * @param longDesc A longer description of the Route.
     */
    public Route(String title, String shortDesc, String longDesc) {
        super(title, shortDesc);
        this.setLongDesc(longDesc);
        initRoute();
    }

    /**
     * Initialise Route object from a Parcel.
     */
    public Route(Parcel p) {
        readFromParcel(p);
    }

    /**
     * Checks if the data in the Route is valid or not, and returns a boolean.
     * @return If the data is valid or not. (true = valid)
     */
    public boolean isValidData() {
        return false;
    }

    /**
     * Adds a Waypoint to the tail of the Route
     * @param w The waypoint to add
     */
    public void addWaypoint(Waypoint w) {
        this.route.addLast(w);
    }

    /**
     * Returns the LinkedList of Waypoint objects.
     * @return The LinkedList of Waypoint objects.
     */
    public LinkedList<Waypoint> getWaypointList() {
        return this.route;
    }

    /**
     * Returns list of coordinates.
     * @return The list of coordinates.
     */
    public Stack<Location> getCoordinateList() {
        return this.coordinates;
    }

    /**
     * Adds an item to the list of coordinates.
     * @param location The Location object to add.
     */
    public void addCoordinate(Location location) {
        this.coordinates.push(location);
    }

    /**
     * Returns the long description of the Route.
     * @return The long description of the Route.
     */
    public String getLongDesc() {
        return this.longDesc;
    }

    /**
     * Sets the long description.
     * @param longDesc A longer description of the Route.
     */
    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    /**
     * Describes contents of parcel.
     * @return Description
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the Route into a Parcel for moving between screens.
     * @param parcel The parcel to be written to.
     * @param i Flags.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getTitle());
        parcel.writeString(this.getShortDesc());
        parcel.writeString(this.getLongDesc());
        parcel.writeList(this.route);
        parcel.writeList(this.coordinates);
    }

    /**
     * Reads Route data from a parcel.
     * @param inParcel The parcel to read the Route from.
     */
    public void readFromParcel(Parcel inParcel) {
        this.setTitle(inParcel.readString());
        this.setShortDesc(inParcel.readString());
        this.setLongDesc(inParcel.readString());

        initLists();
        inParcel.readList(this.route, null); // Not sure about these two lines yet
        inParcel.readList(this.coordinates, null); // TODO test this method
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
}
