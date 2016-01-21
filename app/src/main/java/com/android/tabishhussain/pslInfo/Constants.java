package com.android.tabishhussain.pslInfo;

import com.android.tabishhussain.pslInfo.DataClasses.PslSchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tabish on 1/13/16.
 */
public class Constants {
    public static boolean shouldShowAds = false;

    public static List<PslSchedule> allMatchesSchedule = new ArrayList<>();

    static {

        allMatchesSchedule.add(new PslSchedule("Islamabad", "Quetta", R.drawable.isl_logo,
                R.drawable.quetta_logo, "4:Feb:2016:Thursday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Karachi", "Lahore", R.drawable.karachi_logo,
                R.drawable.lahore_logo, "5:Feb:2016:Friday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Peshawar", "Islamabad", R.drawable.peshawar_logo,
                R.drawable.isl_logo, "5:Feb:2016:Friday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Quetta", "Karachi", R.drawable.quetta_logo,
                R.drawable.karachi_logo, "6:Feb:2016:Saturday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Lahore", "Peshawar", R.drawable.lahore_logo,
                R.drawable.peshawar_logo, "6:Feb:2016:Saturday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Islamabad", "Karachi", R.drawable.isl_logo,
                R.drawable.karachi_logo, "7:Feb:2016:Sunday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Quetta", "Peshawar", R.drawable.quetta_logo,
                R.drawable.peshawar_logo, "7:Feb:2016:Sunday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Lahore", "Quetta", R.drawable.lahore_logo,
                R.drawable.quetta_logo, "8:Feb:2016:Monday", "Dubai"));


        allMatchesSchedule.add(new PslSchedule("Islamabad", "Lahore", R.drawable.isl_logo,
                R.drawable.lahore_logo, "10:Feb:2016:Wednesday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Karachi", "Peshawar", R.drawable.karachi_logo,
                R.drawable.peshawar_logo, "11:Feb:2016:Thursday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Islamabad", "Quetta", R.drawable.isl_logo,
                R.drawable.quetta_logo, "11:Feb:2016:Thursday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Karachi", "Lahore", R.drawable.karachi_logo,
                R.drawable.lahore_logo, "12:Feb:2016:Friday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Peshawar", "Islamabad", R.drawable.peshawar_logo,
                R.drawable.isl_logo, "12:Feb:2016:Friday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Quetta", "Karachi", R.drawable.quetta_logo,
                R.drawable.karachi_logo, "13:Feb:2016:Saturday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Lahore", "Peshawar", R.drawable.lahore_logo,
                R.drawable.peshawar_logo, "13:Feb:2016:Saturday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Islamabad", "Karachi", R.drawable.isl_logo,
                R.drawable.karachi_logo, "14:Feb:2016:Sunday", "Sharjah"));
        allMatchesSchedule.add(new PslSchedule("Quetta", "Peshawar", R.drawable.quetta_logo,
                R.drawable.peshawar_logo, "14:Feb:2016:Sunday", "Sharjah"));


        allMatchesSchedule.add(new PslSchedule("Lahore", "Quetta", R.drawable.lahore_logo,
                R.drawable.quetta_logo, "16:Feb:2016:Tuesday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Karachi", "Peshawar", R.drawable.karachi_logo,
                R.drawable.peshawar_logo, "17:Feb:2016:Wednesday", "Dubai"));
        allMatchesSchedule.add(new PslSchedule("Islamabad", "Lahore", R.drawable.isl_logo,
                R.drawable.lahore_logo, "17:Feb:2016:Wednesday", "Dubai"));
    }
}
