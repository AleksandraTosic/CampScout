package com.lmb_europa.campscout;

/**
 * Created by AleksandraPC on 07-Sep-17.
 */

public class MyPlace {
    int placeNum;
    String placeCoords;
    String sdate;
    String edate;
    long ID;

    public MyPlace(int placeNumber)
    {
        this.placeNum = placeNumber;
    }

    public long getPlaceID() {
        return ID;
    }
    public void setPlaceID(long placeID) {
        ID = placeID;
    }

    public int getPlaceNum() {
        return placeNum;
    }
    public void setPlaceNum(int placeNumber) {
        placeNum = placeNumber;
    }

    public String getPlaceCoords() {
        return placeCoords;
    }
    public void setPlaceCoords(String placeCoordinates) {
        placeCoords = placeCoordinates;
    }

    public String getStartDate() {
        return sdate;
    }
    public void setStartDate(String reservDate) {
        sdate = reservDate;
    }

    public String getEndDate() {
        return edate;
    }
    public void setEndDate(String reservDate) {
        edate = reservDate;
    }

}
