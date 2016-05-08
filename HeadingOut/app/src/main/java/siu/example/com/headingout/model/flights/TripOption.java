package siu.example.com.headingout.model.flights;

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

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(String saleTotal) {
        this.saleTotal = saleTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Slice> getSlice() {
        return slice;
    }

    public void setSlice(List<Slice> slice) {
        this.slice = slice;
    }

    public List<Pricing> getPricing() {
        return pricing;
    }

    public void setPricing(List<Pricing> pricing) {
        this.pricing = pricing;
    }
}
