package siu.example.com.headingout.model.flights;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class Trip {
    String kind;
    String requestId;
    FlightData data;
    List<TripOption> tripOption;


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public FlightData getData() {
        return data;
    }

    public void setData(FlightData data) {
        this.data = data;
    }

    public List<TripOption> getTripOption() {
        return tripOption;
    }

    public void setTripOption(List<TripOption> tripOption) {
        this.tripOption = tripOption;
    }
}
