package siu.example.com.headingout.model;

/**
 * Created by samsiu on 5/2/16.
 */
public class TestHotels {
    String kind;
    String name;
    String city;
    String state;
    String country;
    String rating;

    public TestHotels(String name, String city, String state, String country, String rating) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
        this.rating = rating;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getRating() {
        return rating;
    }
}
