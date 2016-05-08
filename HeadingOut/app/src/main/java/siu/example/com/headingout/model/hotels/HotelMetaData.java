package siu.example.com.headingout.model.hotels;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by samsiu on 5/8/16.
 */
@Root(name="HotelMetaData")
public class HotelMetaData{
    @ElementList(name = "Amenities")
    List<Amenities> amenitiesList;

    @ElementList(name = "Neighborhoods")
    List<HotWireNeighborhoods> neighborhoodsList;

    public List<Amenities> getAmenitiesList() {
        return amenitiesList;
    }

    public List<HotWireNeighborhoods> getNeighborhoodsList() {
        return neighborhoodsList;
    }
}

