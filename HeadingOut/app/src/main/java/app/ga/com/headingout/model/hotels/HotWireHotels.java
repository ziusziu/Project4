package app.ga.com.headingout.model.hotels;


import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */


public class HotWireHotels{

    HWErrors errors;
    HWMetaData MetaData;
    List<HWResult> Result;


    public HWErrors getErrors() {
        return errors;
    }

    public HWMetaData getMetaData() {
        return MetaData;
    }

    public List<HWResult> getResult() {
        return Result;
    }
}

