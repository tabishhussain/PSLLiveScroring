package com.android.tabishhussain.pslInfo.ApiManager;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.android.tabishhussain.pslInfo.AllMatchesActivity;
import com.android.tabishhussain.pslInfo.DataClasses.MatchInfo;
import com.android.tabishhussain.pslInfo.DataClasses.MatchStatus;
import com.android.tabishhussain.pslInfo.R;
import com.android.tabishhussain.pslInfo.Utils.MatchStatusDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tabish Hussain on 12/24/2015.
 * Api Manager Class
 */
public class CurrentData {

    public List<MatchInfo> AllMatchInfo = new ArrayList<>();
    public List<MatchStatus> AllMatchStatus = new ArrayList<>();
    protected static CurrentData mCurrentData;
    private static final String ns = null;

    public interface DataLoadListener {
        void onLoad(CurrentData currentData, boolean isJson);

        void onError(String error);
    }

    public static void loadData(Context context, DataLoadListener listener) {
        new GetMatchesIdTask(context, listener).execute();
    }

    public static class GetMatchesIdTask extends AsyncTask<Void, String, String> {
        private static final String All_Matched_URL = "http://cricscore-api.appspot.com/csa";
        private HttpURLConnection urlConnection;
        DataLoadListener listener;
        Context c;

        public GetMatchesIdTask(Context context, DataLoadListener listener) {
            this.listener = listener;
            c = context;
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
            if (result != null) {
                List<String> ids = new ArrayList<>();
                CurrentData currentData = new CurrentData();
                if (!TextUtils.isEmpty(result)) {
                    Gson gson = new GsonBuilder().create();
                    Type infoType = new TypeToken<List<MatchInfo>>() {
                    }.getType();
                    currentData.AllMatchInfo = gson.fromJson(result, infoType);
                    String[] pslTeams = c.getResources().getStringArray(R.array.psl_teams);
                    for (MatchInfo matchInfo : currentData.AllMatchInfo) {
                        Log.d(AllMatchesActivity.JSON_RESPONSE, matchInfo.toString());
                        for (int i = 0 ; i < pslTeams.length ; i++){
                            if(matchInfo.t1.toLowerCase(Locale.US).contains(pslTeams[i])
                                    ||matchInfo.t2.toLowerCase(Locale.US).contains(pslTeams[i])) {
                                ids.add(matchInfo.id);
                            }
                        }
                    }
                }
                new GetMatchStatusTask(ids, currentData, listener).execute();
            } else {
                new GetXmlFeeds(listener).execute();
            }
        }
    }

    public static class GetMatchStatusTask extends AsyncTask<Void, String, String> {
        private static final String All_Matched_STATUS_URL = "http://cricscore-api.appspot.com/csa?id=";
        List<String> matchIds = new ArrayList<>();
        private HttpURLConnection urlConnection;
        CurrentData currentData;
        DataLoadListener listener;

        public GetMatchStatusTask(List<String> matchIds, CurrentData currentData
                , DataLoadListener listener) {
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
                Log.d(AllMatchesActivity.JSON_RESPONSE, All_Matched_STATUS_URL + ids);
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
                        .registerTypeAdapter(MatchStatus.class, new MatchStatusDeserializer())
                        .create();
                Type statusType = new TypeToken<List<MatchStatus>>() {
                }.getType();
                currentData.AllMatchStatus = gson.fromJson(result, statusType);
                mCurrentData = currentData;
                listener.onLoad(mCurrentData, true);
            } else {
                listener.onError("Sorry for the inconvenience :(\n" +
                        "Server is under maintenance please try later..");
            }
        }
    }

    public static class GetXmlFeeds extends AsyncTask<Void, Void, Void> {

        private static final String All_Matched_URL = "http://static.cricinfo.com/rss/livescores.xml";
        private HttpURLConnection urlConnection;
        DataLoadListener listener;
        CurrentData mCurrentData;

        public GetXmlFeeds(DataLoadListener listener) {
            this.listener = listener;
            mCurrentData = new CurrentData();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(All_Matched_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(false);
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                mCurrentData.AllMatchStatus = parse(inputStream);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (MatchStatus matchStatus : mCurrentData.AllMatchStatus) {
                Log.d(AllMatchesActivity.XML_RESPONSE, matchStatus + "");
            }
            listener.onLoad(mCurrentData, false);
        }

        public List<MatchStatus> parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return readFeed(parser);
            } finally {
                in.close();
            }
        }

        private List<MatchStatus> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            List<MatchStatus> entries = new ArrayList<>();

            parser.require(XmlPullParser.START_TAG, ns, "rss");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("channel")) {
                    parser.require(XmlPullParser.START_TAG, ns, "channel");
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        name = parser.getName();
                        if (name.equals("item")) {
                            entries.add(readEntry(parser));
                        } else {
                            skip(parser);
                        }
                    }
                }
            }
            return entries;
        }

        private MatchStatus readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "item");
            String si = null;
            String id = null;
            MatchStatus matchStatus;
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {
                    si = readTitle(parser);
                } else if (name.equals("guid")) {
                    id = readId(parser);
                } else {
                    skip(parser);
                }
            }
            matchStatus = new MatchStatus();
            matchStatus.id = id;
            matchStatus.si = si;
            matchStatus.setTeams(si);
            return matchStatus;
        }

        private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "title");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "title");
            return title;
        }

        private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "guid");
            String url = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "guid");
            Pattern idPattern = Pattern.compile("\\d+");
            Matcher matcher = idPattern.matcher(url);
            if (matcher.find()) {
                return matcher.group();
            }
            return url;
        }

        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }


        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
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
