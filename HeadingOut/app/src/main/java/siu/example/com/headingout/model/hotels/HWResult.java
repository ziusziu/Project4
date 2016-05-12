package siu.example.com.headingout.model.hotels;

import java.util.List;

/**
 * Created by samsiu on 5/9/16.
 */
public class HWResult {
    String CurrencyCode;
    String DeepLink;
    String ResultId;
    String HWRefNumber;
    String SubTotal;
    String TaxesAndFees;
    String TotalPrice;
    //List<HWAmenityCodes> AmenityCodes;
    String CheckInDate;
    String CheckOutDate;
    String NeighborhoodId;
    String LodgingTypeCode;
    String Nights;
    String AveragePricePerNight;
    String RecommendationPercentage;
    String Rooms;
    String SavingsPercentage;
    List<String> SpecialTaxItems;
    String StarRating;


    public String getStarRating() {
        return StarRating;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public String getDeepLink() {
        return DeepLink;
    }

    public String getResultId() {
        return ResultId;
    }

    public String getHWRefNumber() {
        return HWRefNumber;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public String getTaxesAndFees() {
        return TaxesAndFees;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }



    public String getCheckInDate() {
        return CheckInDate;
    }

    public String getCheckOutDate() {
        return CheckOutDate;
    }

    public String getNeighborhoodId() {
        return NeighborhoodId;
    }

    public String getLodgingTypeCode() {
        return LodgingTypeCode;
    }

    public String getNights() {
        return Nights;
    }

    public String getAveragePricePerNight() {
        return AveragePricePerNight;
    }

    public String getRecommendationPercentage() {
        return RecommendationPercentage;
    }

    public String getRooms() {
        return Rooms;
    }

    public String getSavingsPercentage() {
        return SavingsPercentage;
    }

    public List<String> getSpecialTaxItems() {
        return SpecialTaxItems;
    }
}
