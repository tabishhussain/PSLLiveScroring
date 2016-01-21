package com.android.tabishhussain.pslInfo.DataClasses;

/**
 * Created by tabish on 1/21/16.
 */
public class PslSchedule {

    public String team1;
    public String team2;
    public int team1thumbnail;
    public int team2thumbnail;
    public int date;
    public String month;
    public String year;
    public String day;
    public String place;

    public PslSchedule(String team1, String team2, int team1thumbnail, int team2thumbnail, String date, String place) {
        this.team1 = team1;
        this.team2 = team2;
        this.team1thumbnail = team1thumbnail;
        this.team2thumbnail = team2thumbnail;
        String[] timeDate = date.split(":");
        this.date = Integer.parseInt(timeDate[0]);
        month = timeDate[1];
        year = timeDate[2];
        day = timeDate[3];
        this.place = place;
    }

    public String getDate() {
        return month + " " + date + ", " + year + " - " + day;
    }
}
