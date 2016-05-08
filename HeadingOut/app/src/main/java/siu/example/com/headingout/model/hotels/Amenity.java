package siu.example.com.headingout.model.hotels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by samsiu on 5/8/16.
 */
@Root(name="Amenity")
public class Amenity{
    @Element(name = "Code")
    String code;

    @Element(name = "Description")
    String description;

    @Element(name = "Name")
    String name;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}