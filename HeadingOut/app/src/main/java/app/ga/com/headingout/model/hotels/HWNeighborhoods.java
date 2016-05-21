package app.ga.com.headingout.model.hotels;

import java.util.List;

/**
 * Created by samsiu on 5/9/16.
 */
public class HWNeighborhoods {
    String Centroid;
    String City;
    String Country;
    String Id;
    String Name;
    List<HWShape> Shape;
    String State;

    public class HWShape{
        String Shape;

        public String getShape() {
            return Shape;
        }
    }

    public String getCentroid() {
        return Centroid;
    }

    public String getCity() {
        return City;
    }

    public String getCountry() {
        return Country;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public List<HWShape> getShape() {
        return Shape;
    }

    public String getState() {
        return State;
    }
}
