package com.android.tabishhussain.pslInfo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.tabishhussain.pslInfo.ApiManager.CurrentData;
import com.android.tabishhussain.pslInfo.DataClasses.MatchStatus;
import com.android.tabishhussain.pslInfo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class ListAdapter extends BaseAdapter {

    CurrentData mCurrentData = new CurrentData();
    CurrentData mTypeFilteredData = new CurrentData();
    CurrentData mDayFilteredData = new CurrentData();
    Context context;
    ViewHolder mHolder;
    List<String> countries;

    public ListAdapter(Context context) {
        this.context = context;
        countries = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().openRawResource(
                    context.getResources().getIdentifier("raw/internationalteams",
                            "raw", context.getPackageName()));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                countries.add(line);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mDayFilteredData.AllMatchStatus.size();
    }

    @Override
    public Object getItem(int position) {
        return mDayFilteredData.AllMatchStatus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater.from(context));
            convertView = inflater.inflate(R.layout.row_match_fragment, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }
        mHolder = (ViewHolder) convertView.getTag();
        MatchStatus matchStatus = mDayFilteredData.AllMatchStatus.get(position);
        mHolder.teamA.setText(matchStatus.teamA);
        mHolder.teamB.setText(matchStatus.teamB);
        if (!TextUtils.isEmpty(matchStatus.battingTeam)) {
            mHolder.battingTeamScore.setText(matchStatus.getBattingTeamScore() + "");
            mHolder.overs.setText("->  In " + matchStatus.overs);
            mHolder.battingTeam.setText(matchStatus.battingTeam + context.getString(R.string.label_isbattingat));
            mHolder.matchStatus.setText(matchStatus.getMatchStatus());
            mHolder.batsman1.setText(matchStatus.batsman1);
            mHolder.batsman2.setText(matchStatus.batsman2);
            mHolder.bowler.setText(matchStatus.bowler);
            mHolder.startingTime.setVisibility(View.GONE);
            mHolder.battingWickets.setText("->  For " + matchStatus.getBattingTeamWickets() + " wickets");
            setVisibility(mHolder, View.VISIBLE);
            if (matchStatus.target != -1) {
                mHolder.target.setText(matchStatus.target + "");
            } else {
                mHolder.label_need.setVisibility(View.INVISIBLE);
                mHolder.target.setVisibility(View.INVISIBLE);
                mHolder.label_to_win.setVisibility(View.INVISIBLE);
            }
        } else {
            setVisibility(mHolder, View.INVISIBLE);
            mHolder.startingTime.setText(matchStatus.de);
            mHolder.startingTime.setVisibility(View.VISIBLE);
            mHolder.matchStatus.setText(matchStatus.getMatchStatus());
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public void setData(CurrentData currentData) {
        mCurrentData = currentData;
        Collections.sort(mCurrentData.AllMatchStatus, COMPARATOR);
    }

    public void TypeFilter(int position) {
        switch (position) {
            case 0:
                mTypeFilteredData = mCurrentData;
                break;
            case 1:
            case 4:
                mTypeFilteredData = new CurrentData();
                for (MatchStatus matchStatus :
                        mCurrentData.AllMatchStatus) {
                    if ((TextUtils.isEmpty(matchStatus.matchOverStatement) && position == 1)
                            || (!TextUtils.isEmpty(matchStatus.matchOverStatement) && position == 4)) {
                        mTypeFilteredData.AllMatchStatus.add(matchStatus);
                    }
                }
                break;
            case 2:
            case 3:
                mTypeFilteredData = new CurrentData();
                for (MatchStatus matchStatus :
                        mCurrentData.AllMatchStatus) {
                    if ((isInternationalMatch(matchStatus) && position == 2)
                            || (!isInternationalMatch(matchStatus) && position == 3)) {
                        mTypeFilteredData.AllMatchStatus.add(matchStatus);
                    }
                }
                break;
        }
        notifyDataSetChanged();
    }

    public void dayFilter(int position) {
        switch (position) {
            case 0:
                mDayFilteredData = new CurrentData();
                for (MatchStatus matchStatus : mTypeFilteredData.AllMatchStatus) {
                    if (!matchStatus.getMatchStatus()
                            .equalsIgnoreCase(context.getString(R.string.msg_not_yet_started))) {
                        mDayFilteredData.AllMatchStatus.add(matchStatus);
                    }
                }
                break;
            case 1:
                mDayFilteredData = new CurrentData();
                for (MatchStatus matchStatus : mTypeFilteredData.AllMatchStatus) {
                    if (matchStatus.getMatchStatus()
                            .equalsIgnoreCase(context.getString(R.string.msg_not_yet_started))) {
                        mDayFilteredData.AllMatchStatus.add(matchStatus);
                    }
                }
                break;
            case 2:
                mDayFilteredData = mTypeFilteredData;
        }
        notifyDataSetChanged();
    }

    public boolean isInternationalMatch(MatchStatus matchStatus) {
        if (countries.size() != 0) {
            if (countries.contains(matchStatus.teamA.trim().toLowerCase(Locale.US))
                    || countries.contains(matchStatus.teamB.trim().toLowerCase(Locale.US))) {
                return true;
            }
        }
        return false;
    }


    public static final Comparator<MatchStatus> COMPARATOR = new Comparator<MatchStatus>() {
        @Override
        public int compare(MatchStatus lhs, MatchStatus rhs) {
            if (TextUtils.isEmpty(lhs.matchOverStatement) && !TextUtils.isEmpty(rhs.matchOverStatement)) {
                return -1;
            } else if ((!TextUtils.isEmpty(lhs.matchOverStatement) && !TextUtils.isEmpty(rhs.matchOverStatement))
                    || (TextUtils.isEmpty(lhs.matchOverStatement) && TextUtils.isEmpty(rhs.matchOverStatement))) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    public class ViewHolder {
        public final TextView teamA;
        public final TextView teamB;
        public final TextView battingTeam;
        public final TextView battingTeamScore;
        public final TextView overs;
        public final TextView matchStatus;
        public final TextView startingTime;
        public final TextView label_batsman;
        public final TextView batsman1;
        public final TextView batsman2;
        public final TextView label_bowler;
        public final TextView bowler;
        public final TextView label_need;
        public final TextView label_to_win;
        public final TextView target;
        public final TextView battingWickets;
        public final View separator1;
        public final View separator3;
        public final View separator4;

        public ViewHolder(View view) {
            teamA = (TextView) view.findViewById(R.id.teamA);
            Typeface teamATypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            teamA.setTypeface(teamATypeFace);

            teamB = (TextView) view.findViewById(R.id.teamB);
            Typeface teamBTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            teamB.setTypeface(teamBTypeFace);

            battingTeam = (TextView) view.findViewById(R.id.battingTeam);
            Typeface battingTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_century-schoolbook-bold.ttf");
            battingTeam.setTypeface(battingTypeFace);

            battingTeamScore = (TextView) view.findViewById(R.id.battingScore);
            Typeface battingScoreTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_century-schoolbook-bold.ttf");
            battingTeamScore.setTypeface(battingScoreTypeFace);

            overs = (TextView) view.findViewById(R.id.overs);
            Typeface oversTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_century-gothic.ttf");
            overs.setTypeface(oversTypeFace);

            matchStatus = (TextView) view.findViewById(R.id.matchStatus);
            Typeface matchStatusTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_century-gothic.ttf");
            matchStatus.setTypeface(matchStatusTypeFace);

            startingTime = (TextView) view.findViewById(R.id.startingTime);
            Typeface label_bowlerTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_century-gothic.ttf");
            startingTime.setTypeface(label_bowlerTypeFace);

            label_batsman = (TextView) view.findViewById(R.id.label_batsman);
            Typeface label_batsmanTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            label_batsman.setTypeface(label_batsmanTypeFace);

            batsman1 = (TextView) view.findViewById(R.id.batsman1);
            Typeface batsman1TypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            batsman1.setTypeface(batsman1TypeFace);

            batsman2 = (TextView) view.findViewById(R.id.batsman2);
            Typeface batsman2TypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            batsman2.setTypeface(batsman2TypeFace);

            label_bowler = (TextView) view.findViewById(R.id.label_bowlers);
            Typeface label_bowleTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            label_bowler.setTypeface(label_bowleTypeFace);

            bowler = (TextView) view.findViewById(R.id.bowler);
            Typeface bowlerTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            bowler.setTypeface(bowlerTypeFace);

            label_need = (TextView) view.findViewById(R.id.label_need);
            Typeface labelTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            label_need.setTypeface(labelTypeFace);

            label_to_win = (TextView) view.findViewById(R.id.label_to_win);
            Typeface label_to_winTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
            label_to_win.setTypeface(label_to_winTypeFace);

            target = (TextView) view.findViewById(R.id.target);
            Typeface targetTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_century-schoolbook-bold.ttf");
            target.setTypeface(targetTypeFace);

            battingWickets = (TextView) view.findViewById(R.id.labelfor);
            Typeface battingWicketsTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/ufonts.com_century-gothic.ttf");
            battingWickets.setTypeface(battingWicketsTypeFace);

            separator1 = view.findViewById(R.id.separator1);
            separator3 = view.findViewById(R.id.separator3);
            separator4 = view.findViewById(R.id.separator4);
        }

    }

    public void setVisibility(ViewHolder mHolder, int visibility) {
        mHolder.battingTeam.setVisibility(visibility);
        mHolder.battingTeamScore.setVisibility(visibility);
        mHolder.overs.setVisibility(visibility);
        mHolder.battingWickets.setVisibility(visibility);
        mHolder.label_batsman.setVisibility(visibility);
        mHolder.label_bowler.setVisibility(visibility);
        mHolder.batsman1.setVisibility(visibility);
        mHolder.batsman2.setVisibility(visibility);
        mHolder.bowler.setVisibility(visibility);
        mHolder.label_need.setVisibility(visibility);
        mHolder.label_to_win.setVisibility(visibility);
        mHolder.target.setVisibility(visibility);
        mHolder.separator1.setVisibility(visibility);
        mHolder.separator3.setVisibility(visibility);
        mHolder.separator4.setVisibility(visibility);
    }
}
