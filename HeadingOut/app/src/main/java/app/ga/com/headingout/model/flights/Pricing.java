package app.ga.com.headingout.model.flights;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class Pricing {
    String kind;
    List<Fare> fare;
    List<SegmentPricing> segmentPricing;
    String baseFareTotal;
    String saleFareTotal;
    String saleTaxTotal;
    String saleTotal;
    Passengers passengers;
    public class Passengers{
        String kind;
        int adultCount;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public int getAdultCount() {
            return adultCount;
        }

        public void setAdultCount(int adultCount) {
            this.adultCount = adultCount;
        }
    }
    List<Tax> tax;
    String fareCalculation;
    String latestTicketingTime;
    String ptc;


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<Fare> getFare() {
        return fare;
    }

    public void setFare(List<Fare> fare) {
        this.fare = fare;
    }

    public List<SegmentPricing> getSegmentPricing() {
        return segmentPricing;
    }

    public void setSegmentPricing(List<SegmentPricing> segmentPricing) {
        this.segmentPricing = segmentPricing;
    }

    public String getBaseFareTotal() {
        return baseFareTotal;
    }

    public void setBaseFareTotal(String baseFareTotal) {
        this.baseFareTotal = baseFareTotal;
    }

    public String getSaleFareTotal() {
        return saleFareTotal;
    }

    public void setSaleFareTotal(String saleFareTotal) {
        this.saleFareTotal = saleFareTotal;
    }

    public String getSaleTaxTotal() {
        return saleTaxTotal;
    }

    public void setSaleTaxTotal(String saleTaxTotal) {
        this.saleTaxTotal = saleTaxTotal;
    }

    public String getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(String saleTotal) {
        this.saleTotal = saleTotal;
    }

    public Passengers getPassenters() {
        return passengers;
    }

    public void setPassenters(Passengers passenters) {
        this.passengers = passenters;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public String getFareCalculation() {
        return fareCalculation;
    }

    public void setFareCalculation(String fareCalculation) {
        this.fareCalculation = fareCalculation;
    }

    public String getLatestTicketingTime() {
        return latestTicketingTime;
    }

    public void setLatestTicketingTime(String latestTicketingTime) {
        this.latestTicketingTime = latestTicketingTime;
    }

    public String getPtc() {
        return ptc;
    }

    public void setPtc(String ptc) {
        this.ptc = ptc;
    }
}
