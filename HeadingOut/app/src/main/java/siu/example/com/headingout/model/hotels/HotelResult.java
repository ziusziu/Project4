package siu.example.com.headingout.model.hotels;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class HotelResult {
    String currencyCode;
    String deepLink;
    String resultId;
    String hwRefNumber;
    String subTotal;
    String taxesAndFees;
    String totalPrice;
    List<AmenityCodes> amenityCodes;
    class AmenityCodes{
        String Code;
        public String getCode() {
            return Code;
        }
    }
    String checkInDate;
    String checkOutDate;
    String neighborhoodId;
    String lodgingTypeCode;
    int nights;
    String averagePricePerNight;
    String recommendationPercentage;
    int rooms;
    String savingsPercentage;
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

    public List<AmenityCodes> getAmenityCodes() {
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

    public String getStarRating() {
        return starRating;
    }
}
