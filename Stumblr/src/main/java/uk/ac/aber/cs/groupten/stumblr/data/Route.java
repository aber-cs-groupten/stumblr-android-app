package uk.ac.aber.cs.group10.stumblr.data;

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
     * Adds a Waypoint to the tail of the Route LinkedList
     * @param w The waypoint to add
     */
    public void addWaypoint(Waypoint w) {
        this.route.addLast(w);
    }

    /**
     * Returns the last Waypoint to the LinkedList.
     * @return The last Waypoint in the Route.
     */
    public Waypoint getWaypoint() {
        return this.route.getLast();
    }

    public String getLongDesc() {
        return this.longDesc;
    }

    /**
     * Constructor for Route.
     * @param title The title of the Route object.
     * @param shortDesc A short description of the Route.
     * @param longDesc A longer description of the Route.
     */
    public Route(String title, String shortDesc, String longDesc) {
        super(title, shortDesc);

    }

    /**
     * To be implemented. Will return a URL containing filesystem location of bundled Route file
     * (ready for upload to server)
     * @return The URL of the bundle
     */
    public URL bundle() {
        return null;
    }
}
