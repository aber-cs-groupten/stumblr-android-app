//package com.element84.starter;
package uk.ac.aber.cs.groupten.stumblr;

import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.location.Location;
import android.os.Parcel;
import android.util.Config;
import android.widget.TextView;

import junit.framework.Assert;

import uk.ac.aber.cs.groupten.stumblr.*;

@RunWith(RobolectricTestRunner.class)
public class RouteTest {

    @Test
    public void testRouteConstructor() {
        //Create the Test Route
        Route testRoute = new Route();

        //Get the contructed Waypoint List
        LinkedList<Waypoint> testWaypoints = testRoute.getWaypointList();

        //Check that the waypoint list is initialized
        Assert.assertNotNull(testWaypoints);
    }

    @Test
    public void testParceling(){
        //Create the First Test Route
        Route firstTestRoute = new Route();

        //Populate the First Test Route
        firstTestRoute.setTitle("Test");
        firstTestRoute.setShortDesc("TestSD");
        firstTestRoute.setLongDesc("TestLD");
        firstTestRoute.setStartTime("TestST");
        firstTestRoute.setLengthTime("TestLT");

        //Create the test Parcle
        Parcel testParcel = new Parcel();

        //Add the Test Route to the Test Parcel
        firstTestRoute.writeToParcel(testParcel, 0);

        //Create a second Test Route
        Route secondTestRoute = new Route();

        //Read the data from the Test Parcel into the Second Test Route
        secondTestRoute.readFromParcel(testParcel);

        //Test the data in the Second Test Route is the same as the initial test data
        Assert.assertTrue(secondTestRoute.getTitle().equals("Test"));
        Assert.assertTrue(secondTestRoute.getShortDesc().equals("TestSD"));
        Assert.assertTrue(secondTestRoute.getLongDesc().equals("TestLD"));
        Assert.assertTrue(secondTestRoute.getStartTime().equals("TestST"));
        Assert.assertTrue(secondTestRoute.getLengthTime().equals("TestLT"));
    }

    @Test
    public void testSingleWaypointDistance(){
        // Create the Test Route
        Route testRoute = new Route();

        // Create the Test Waypoint
        Location testLocation = new Location();
        testLocation.setLatitude(52.4162);
        testLocation.setLongitude(-4.06606);

        // Add the Test Waypoint to the Test Route
        testroute.addCoordinate(testLocation);

        // Calculate the distance
        float distance = testRoute.getDistance();

        // Check that distance is 0
        Assert.assertTrue(distance == 0.0);
    }

    @Test
    public void testDoubleWaypointDistance(){
        // Create the Test Route
        Route testRoute = new Route();

        // Create the First Test Waypoint
        Location firstTestLocation = new Location();
        firstTestLocation.setLatitude(52.4162);
        firstTestLocation.setLongitude(-4.06606);

        // Create the Second Test Waypoint
        Location secondTestLocation = new Location();
        secondTestLocation.setLatitude(52.4162);
        secondTestLocation.setLongitude(-4.06625);

        // Add the Test Waypoints to the Test Route
        testroute.addCoordinate(firstTestLocation);
        testroute.addCoordinate(secondTestLocation);

        // Calculate the distance
        float distance = testRoute.getDistance();

        // Check that distance is close enough to 12
        Assert.assertTrue(distance > 11.0 && distance < 13.0);
    }

    @Test
    public void testTripleWaypointDistance(){
        // Create the Test Route
        Route testRoute = new Route();

        // Create the First Test Waypoint
        Location firstTestLocation = new Location();
        firstTestLocation.setLatitude(52.4162);
        firstTestLocation.setLongitude(-4.06606);

        // Create the Second Test Waypoint
        Location secondTestLocation = new Location();
        secondTestLocation.setLatitude(52.4162);
        secondTestLocation.setLongitude(-4.06625);

        // Create the Third Test Waypoint
        Location thirdTestLocation = new Location();
        thirdTestLocation.setLatitude(52.4163);
        thirdTestLocation.setLongitude(-4.0661);

        // Add the Test Waypoints to the Test Route
        testroute.addCoordinate(firstTestLocation);
        testroute.addCoordinate(secondTestLocation);
        testroute.addCoordinate(thirdTestLocation);

        // Calculate the distance
        float distance = testRoute.getDistance();

        // Check that distance is close enough to 27
        Assert.assertTrue(distance > 26.0 && distance < 28.0);
    }
}