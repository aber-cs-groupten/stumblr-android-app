package uk.ac.aber.cs.groupten.stumblr.data;

import android.os.Parcel;

import java.net.URL;
import java.util.LinkedList;

/**
 * Created by charles on 29/11/13.
 */
public class Route extends StumblrData {
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
     * Constructor for Route.
     * @param title The title of the Route object.
     * @param shortDesc A short description of the Route.
     * @param longDesc A longer description of the Route.
     */
    public Route(String title, String shortDesc, String longDesc) {
        super(title, shortDesc);
        this.longDesc = longDesc;
    }

    /**
     * To be implemented. Will return a URL containing filesystem location of bundled Route file
     * (ready for upload to server)
     * @return The URL of the bundle
     */
    public URL bundle() {
        return null;
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
    }

    /**
     * Reads Route data from a parcel.
     * @param inParcel
     */
    public void readFromParcel(Parcel inParcel) {
        this.setTitle(inParcel.readString());
        this.setShortDesc(inParcel.readString());
        this.setLongDesc(inParcel.readString());
    }
}
