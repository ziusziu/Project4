package app.ga.com.headingout.model.sitaairports;

import java.util.List;

/**
 * Created by samsiu on 6/22/16.
 */
public class SitaAirportData {

    int processingDurationMillis;
    boolean authorisedAPI;
    boolean success;
    String airline;
    String errorMessage;
    List<SitaAirports> airports;

    public int getProcessingDurationMillis() {
        return processingDurationMillis;
    }

    public boolean isAuthorisedAPI() {
        return authorisedAPI;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAirline() {
        return airline;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<SitaAirports> getAirports() {
        return airports;
    }
}
