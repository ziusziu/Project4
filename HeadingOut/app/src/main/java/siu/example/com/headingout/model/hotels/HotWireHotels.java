package siu.example.com.headingout.model.hotels;


import java.io.Serializable;
import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */


public class HotWireHotels implements Serializable{

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

