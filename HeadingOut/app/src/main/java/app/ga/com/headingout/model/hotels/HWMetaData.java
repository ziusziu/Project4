package app.ga.com.headingout.model.hotels;

import java.util.List;

/**
 * Created by samsiu on 5/9/16.
 */
public class HWMetaData {
    HWHotelMetaData HotelMetaData;

    public HWHotelMetaData getHotelMetaData() {
        return HotelMetaData;
    }

    public class HWHotelMetaData{
        List<HWAmenities> Amenities;
        List<HWNeighborhoods> Neighborhoods;

        public List<HWAmenities> getAmenities() {
            return Amenities;
        }

        public List<HWNeighborhoods> getNeighborhoods() {
            return Neighborhoods;
        }
    }

}
