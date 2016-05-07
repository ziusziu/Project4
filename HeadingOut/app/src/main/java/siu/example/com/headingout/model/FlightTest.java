package siu.example.com.headingout.model;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }
}
