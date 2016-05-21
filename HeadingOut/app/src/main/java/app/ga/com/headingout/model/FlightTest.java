package app.ga.com.headingout.model;

/**
 * Created by samsiu on 5/2/16.
 */
public class FlightTest {

    String name;
    String airlineName;
    String departureCity;
    String departureTerminal;
    String arrivalCity;
    String arrivalTerminal;

    public FlightTest(String name, String airlineName, String departureCity, String departureTerminal, String arrivalCity, String arrivalTerminal) {
        this.name = name;
        this.airlineName = airlineName;
        this.departureCity = departureCity;
        this.departureTerminal = departureTerminal;
        this.arrivalCity = arrivalCity;
        this.arrivalTerminal = arrivalTerminal;
    }


    public String getName() {
        return name;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }
}
