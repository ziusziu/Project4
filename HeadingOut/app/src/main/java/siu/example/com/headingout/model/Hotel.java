package siu.example.com.headingout.model;

/**
 * Created by samsiu on 5/2/16.
 */
public class Hotel {
    String name;
    String city;
    String state;
    String country;
    String rating;

    public Hotel(String name, String city, String state, String country, String rating) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
