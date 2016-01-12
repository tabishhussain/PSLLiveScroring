package com.android.tabishhussain.psllivescoring.DataClasses;

/**
 * Created by Tabish Hussain on 12/24/2015.
 * match basic info
 */
public class MatchInfo {
    public String id;
    public String t2;
    public String t1;

    @Override
    public String toString() {
        return "MatchInfo{" +
                "id='" + id + '\'' +
                ", t2='" + t2 + '\'' +
                ", t1='" + t1 + '\'' +
                '}';
    }
}
