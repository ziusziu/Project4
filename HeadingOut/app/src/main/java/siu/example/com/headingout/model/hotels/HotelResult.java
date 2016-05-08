package siu.example.com.headingout.model.hotels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
@Root(name="HotelResult")
public class HotelResult {

    @Element(name = "CurrencyCode")
    String currencyCode;

    @Element(name = "DeepLink")
    String deepLink;

    @Element(name = "ResultId")
    String resultId;

    @Element(name = "HWRefNumber")
    String hwRefNumber;

    @Element(name = "SubTotal")
    String subTotal;

    @Element(name = "TaxesAndFees")
    String taxesAndFees;

    @Element(name = "TotalPrice")
    String totalPrice;

    @ElementList(name = "AmenityCodes")
    List<Code> amenityCodes;

    @Element(name = "Code")
    String code;

    @Element(name = "CheckInDate")
    String checkInDate;

    @Element(name = "CheckOutDate")
    String checkOutDate;

    @Element(name = "NeighborhoodId")
    String neighborhoodId;

    @Element(name = "LodgingTypeCode")
    String lodgingTypeCode;

    @Element(name = "Nights")
    int nights;

    @Element(name = "AveragePricePerNight")
    String averagePricePerNight;

    @Element(name = "RecommendationPercentage")
    String recommendationPercentage;

    @Element(name = "Rooms")
    int rooms;

    @Element(name = "SavingsPercentage")
    String savingsPercentage;

    @Element(name = "SpecialTaxItems", required = false)
    String specialTaxItems;

    @Element(name = "StarRating")
    String starRating;


    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public String getResultId() {
        return resultId;
    }

    public String getHwRefNumber() {
        return hwRefNumber;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public String getTaxesAndFees() {
        return taxesAndFees;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public List<Code> getAmenityCodes() {
        return amenityCodes;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public String getNeighborhoodId() {
        return neighborhoodId;
    }

    public String getLodgingTypeCode() {
        return lodgingTypeCode;
    }

    public int getNights() {
        return nights;
    }

    public String getAveragePricePerNight() {
        return averagePricePerNight;
    }

    public String getRecommendationPercentage() {
        return recommendationPercentage;
    }

    public int getRooms() {
        return rooms;
    }

    public String getSavingsPercentage() {
        return savingsPercentage;
    }

    public String getSpecialTaxItems() {
        return specialTaxItems;
    }

    public String getStarRating() {
        return starRating;
    }
}
