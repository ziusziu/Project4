package siu.example.com.headingout.model.hotels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by samsiu on 5/8/16.
 */
@Root(name="Neighborhoods")
public class HotWireNeighborhoods {
    @Element(name = "Centroid")
    String centroid;

    @Element(name = "City")
    String city;

    @Element(name = "Country")
    String country;

    @Element(name = "Id")
    String id;

    @Element(name = "Name")
    String name;

    @Element(name = "Shape", required = false)
    String shape;

    @Element(name = "State")
    String state;

    public String getCentroid() {
        return centroid;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getShape() {
        return shape;
    }
}
