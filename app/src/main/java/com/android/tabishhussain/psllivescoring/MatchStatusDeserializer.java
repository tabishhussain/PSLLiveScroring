package com.android.tabishhussain.psllivescoring;

import android.util.Log;

import com.android.tabishhussain.psllivescoring.DataClasses.MatchStatus;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tabish Hussain on 12/24/2015.
 */
public class MatchStatusDeserializer implements JsonDeserializer<MatchStatus> {

    @Override
    public MatchStatus deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
        MatchStatus matchStatus = new MatchStatus();
        JsonObject matchObject = json.getAsJsonObject();
        String de = matchObject.get("de").getAsString();
        String si = matchObject.get("si").getAsString();
        matchStatus.setTeams(si);
        matchStatus.setMatchOvers(de);
        matchStatus.setMatchStatus(de);
        Log.d(AllMatchesActivity.TAG, matchStatus.toString());
        return matchStatus;
    }

}
