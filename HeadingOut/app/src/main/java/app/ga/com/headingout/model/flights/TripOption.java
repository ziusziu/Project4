package app.ga.com.headingout.model.flights;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class TripOption {

    String kind;
    String saleTotal;
    String id;
    List<Slice> slice;
    List<Pricing> pricing;

    public String getKind() {
        return kind;
    }

    public String getSaleTotal() {
        return saleTotal;
    }

    public String getId() {
        return id;
    }

    public List<Slice> getSlice() {
        return slice;
    }

    public List<Pricing> getPricing() {
        return pricing;
    }
}
