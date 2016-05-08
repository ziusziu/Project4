package siu.example.com.headingout.model.flights.postrequest;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class RequestJson {

    Request request;

    public RequestJson(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
