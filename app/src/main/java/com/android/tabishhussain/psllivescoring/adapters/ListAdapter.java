package com.android.tabishhussain.psllivescoring.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.tabishhussain.psllivescoring.DataClasses.CurrentData;
import com.android.tabishhussain.psllivescoring.DataClasses.MatchStatus;
import com.android.tabishhussain.psllivescoring.R;

/**
 * Created by tabish on 12/31/15.
 */
public class ListAdapter extends BaseAdapter {

    CurrentData mCurrentData = new CurrentData();
    Context context;
    ViewHolder mHolder;

    public ListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mCurrentData.AllMatchStatus.size();
    }

    @Override
    public Object getItem(int position) {
        return mCurrentData.AllMatchStatus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater.from(context));
            convertView = inflater.inflate(R.layout.row_match_fragment, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }
        mHolder = (ViewHolder) convertView.getTag();
        MatchStatus matchStatus = mCurrentData.AllMatchStatus.get(position);
        mHolder.teamA.setText(matchStatus.teamA);
        mHolder.teamB.setText(matchStatus.teamB);
        if (!TextUtils.isEmpty(matchStatus.battingTeam)) {
            mHolder.battingTeamScore.setText(matchStatus.getBattingTeamScore() + "");
            mHolder.battingTeamWickets.setText(matchStatus.getBattingTeamWickets() + "");
            mHolder.battingTeam.setText(matchStatus.battingTeam + "is batting at");
            mHolder.matchStatus.setText(matchStatus.getMatchStatus());
            mHolder.batsman1.setText(matchStatus.batsman1);
            mHolder.batsman2.setText(matchStatus.batsman2);
            mHolder.bowler.setText(matchStatus.bowler);
            mHolder.target.setText(matchStatus.target);
            mHolder.startingTime.setVisibility(View.GONE);
            setVisibility(mHolder, View.VISIBLE);
        } else {
            setVisibility(mHolder, View.INVISIBLE);
            mHolder.startingTime.setText(matchStatus.de);
            mHolder.startingTime.setVisibility(View.VISIBLE);
            mHolder.matchStatus.setText("Match not yet started");
        }
        return convertView;
    }

    public void setData(CurrentData currentData) {
        mCurrentData = currentData;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public final TextView teamA;
        public final TextView teamB;
        public final TextView battingTeam;
        public final TextView battingTeamScore;
        public final TextView battingTeamWickets;
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
        public final TextView label_for;
        public final View separator1;
        public final View separator3;
        public final View separator4;

        public ViewHolder(View view) {
            teamA = (TextView) view.findViewById(R.id.teamA);
            teamB = (TextView) view.findViewById(R.id.teamB);
            battingTeam = (TextView) view.findViewById(R.id.battingTeam);
            battingTeamScore = (TextView) view.findViewById(R.id.battingScore);
            battingTeamWickets = (TextView) view.findViewById(R.id.battingWickets);
            matchStatus = (TextView) view.findViewById(R.id.matchStatus);
            startingTime = (TextView) view.findViewById(R.id.startingTime);
            label_batsman = (TextView) view.findViewById(R.id.label_batsman);
            batsman1 = (TextView) view.findViewById(R.id.batsman1);
            batsman2 = (TextView) view.findViewById(R.id.batsman2);
            label_bowler = (TextView) view.findViewById(R.id.label_bowlers);
            bowler = (TextView) view.findViewById(R.id.bowler);
            label_need = (TextView) view.findViewById(R.id.label_need);
            label_to_win = (TextView) view.findViewById(R.id.label_to_win);
            target = (TextView) view.findViewById(R.id.target);
            label_for = (TextView) view.findViewById(R.id.labelfor);
            separator1 = (View) view.findViewById(R.id.separator1);
            separator3 = (View) view.findViewById(R.id.separator3);
            separator4 = (View) view.findViewById(R.id.separator4);
        }
    }

    public void setVisibility(ViewHolder mHolder, int visibility) {
        mHolder.battingTeam.setVisibility(visibility);
        mHolder.battingTeamScore.setVisibility(visibility);
        mHolder.battingTeamWickets.setVisibility(visibility);
        mHolder.label_for.setVisibility(visibility);
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
