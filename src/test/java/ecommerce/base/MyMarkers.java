package ecommerce.base;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * A list of all Apache log4j markers for this project
 */
public class MyMarkers {

    public static final Marker TEST = MarkerManager.getMarker("TEST");
    public static final Marker ENVIRONMENT_FILE = MarkerManager.getMarker("ENVIRONMENT_FILE");
    public static final Marker API_CALLS = MarkerManager.getMarker("API");
    public static final Marker DRIVER = MarkerManager.getMarker("DRIVER");
    public static final Marker STEP = MarkerManager.getMarker("STEP");
    public static final Marker PAGE_OBJECTS = MarkerManager.getMarker("PAGE_OBJECTS");

    public static final Marker TEST_RESULT = MarkerManager.getMarker("TEST_RESULT");


}
