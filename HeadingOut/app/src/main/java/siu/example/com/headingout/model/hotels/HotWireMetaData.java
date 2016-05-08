package siu.example.com.headingout.model.hotels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by samsiu on 5/8/16.
 */
@Root(name = "MetaData")
public class HotWireMetaData {

    @Element(name = "HotelMetaData")
    HotelMetaData hotelMetaData;

    public HotelMetaData getHotelMetaData() {
        return hotelMetaData;
    }

    public class HotelMetaData{
        @ElementList(name = "Amenities")
        List<HotWireAmenities> amenitiesList;

        @ElementList(name = "Neighborhoods")
        List<HotWireNeighborhoods> neighborhoodsList;

        public List<HotWireAmenities> getAmenitiesList() {
            return amenitiesList;
        }

        public List<HotWireNeighborhoods> getNeighborhoodsList() {
            return neighborhoodsList;
        }
    }


}
