package com.android.tabishhussain.pslInfo.Utils;

import android.util.Log;

import com.android.tabishhussain.pslInfo.AllMatchesActivity;
import com.android.tabishhussain.pslInfo.DataClasses.MatchStatus;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Tabish Hussain on 12/24/2015.
 * Deserializer for gson
 */
public class MatchStatusDeserializer implements JsonDeserializer<MatchStatus> {

    @Override
    public MatchStatus deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
        MatchStatus matchStatus = new MatchStatus();
        JsonObject matchObject = json.getAsJsonObject();
        String de = matchObject.get("de").getAsString();
        String si = matchObject.get("si").getAsString();
        matchStatus.de = de;
        matchStatus.si = si;
        matchStatus.id =  matchObject.get("id").getAsString();
        matchStatus.setTeams(si);
        matchStatus.setMatchOvers(de);
        matchStatus.setPlayers(de);
        Log.d(AllMatchesActivity.JSON_RESPONSE, matchStatus.toString());
        return matchStatus;
    }

}
