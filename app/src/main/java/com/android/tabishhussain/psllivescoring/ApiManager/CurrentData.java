package com.android.tabishhussain.psllivescoring.ApiManager;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.tabishhussain.psllivescoring.AllMatchesActivity;
import com.android.tabishhussain.psllivescoring.DataClasses.MatchInfo;
import com.android.tabishhussain.psllivescoring.DataClasses.MatchStatus;
import com.android.tabishhussain.psllivescoring.Utils.MatchStatusDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabish Hussain on 12/24/2015.
 * Api Manager Class
 */
public class CurrentData {

    public List<MatchInfo> AllMatchInfo = new ArrayList<>();
    public List<MatchStatus> AllMatchStatus = new ArrayList<>();
    protected static CurrentData mCurrentData;

    public interface DataLoadListener {
        void onLoad(CurrentData currentData);

        void onError();
    }

    public static void loadData(DataLoadListener listener) {
        new GetMatchesIdTask(listener).execute();
    }

    public static class GetMatchesIdTask extends AsyncTask<Void, String, String> {
        private static final String All_Matched_URL = "http://cricscore-api.appspot.com/csa";
        private HttpURLConnection urlConnection;
        DataLoadListener listener;

        public GetMatchesIdTask(DataLoadListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(All_Matched_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(false);
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder inputLine = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    inputLine.append(line);
                bufferedReader.close();
                return inputLine.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<String> ids = new ArrayList<>();
            CurrentData currentData = new CurrentData();
            if (!TextUtils.isEmpty(result)) {
                Gson gson = new GsonBuilder().create();
                Type infoType = new TypeToken<List<MatchInfo>>() {
                }.getType();
                currentData.AllMatchInfo = gson.fromJson(result, infoType);
                for (MatchInfo matchInfo : currentData.AllMatchInfo) {
                    ids.add(matchInfo.id);
                }
            }
            new GetMatchStatusTask(ids, currentData, listener).execute();
        }
    }

    public static class GetMatchStatusTask extends AsyncTask<Void, String, String> {
        private static final String All_Matched_STATUS_URL = "http://cricscore-api.appspot.com/csa?id=";
        List<String> matchIds = new ArrayList<>();
        private HttpURLConnection urlConnection;
        CurrentData currentData;
        DataLoadListener listener;

        public GetMatchStatusTask(List<String> matchIds, CurrentData currentData, DataLoadListener listener) {
            this.matchIds = matchIds;
            this.currentData = currentData;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String ids = "";
                for (String id : matchIds) {
                    ids += id + "+";
                }
                Log.d(AllMatchesActivity.TAG, All_Matched_STATUS_URL + ids);
                URL url = new URL(All_Matched_STATUS_URL + ids);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(false);
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder inputLine = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    inputLine.append(line);
                bufferedReader.close();
                return inputLine.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(MatchStatus.class, new MatchStatusDeserializer()).create();
                Type statusType = new TypeToken<List<MatchStatus>>() {
                }.getType();
                currentData.AllMatchStatus = gson.fromJson(result, statusType);
                mCurrentData = currentData;
                listener.onLoad(mCurrentData);
            }
        }
    }

    @Override
    public String toString() {
        return "CurrentData{" +
                "AllMatchInfo=" + AllMatchInfo +
                ", AllMatchStatus=" + AllMatchStatus +
                '}';
    }
}
