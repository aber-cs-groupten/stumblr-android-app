package uk.ac.aber.cs.groupten.stumblr.data;

import android.location.Location;
import android.os.Parcel;

import java.util.LinkedList;

public class Route extends StumblrData {
    private LinkedList<Location> coordinates;

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
    public void initRoute() {
        // Initialise LinkedLists
        this.coordinates = new LinkedList<Location>();
        this.route = new LinkedList<Waypoint>();

        // Timestamp
        startTime = getCurrentTime();
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
     * Returns the last Waypoint in the Route.
     * @return The last Waypoint in the Route.
     */
    public Waypoint getWaypoint() {
        return this.route.getLast();
    }

    /**
     * Returns the LinkedList of Waypoint objects.
     * @return The LinkedList of Waypoint objects.
     */
    public LinkedList<Waypoint> getWaypointList() {
        return this.route;
    }

    /**
     * Sets the LinkedList of Waypoint objects.
     * @param wl The list of waypoint objects.
     */
    public void setWaypointList(LinkedList<Waypoint> wl) {
        this.route = wl;
    }

    /**
     * Returns list of coordinates.
     * @return The list of coordinates.
     */
    public LinkedList<Location> getCoordinateList() {
        return this.coordinates;
    }

    /**
     * Sets the list of coordinates.
     * @param list The list of coordinates.
     */
    public void setCoordinateList(LinkedList<Location> list) {
        this.coordinates = list;
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
     * @param longDesc
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
     * @param inParcel
     */
    public void readFromParcel(Parcel inParcel) {
        this.setTitle(inParcel.readString());
        this.setShortDesc(inParcel.readString());
        this.setLongDesc(inParcel.readString());
        inParcel.readList(this.route, null); // Not sure about these two lines yet
        inParcel.readList(this.coordinates, null); // TODO test this method
    }
}
