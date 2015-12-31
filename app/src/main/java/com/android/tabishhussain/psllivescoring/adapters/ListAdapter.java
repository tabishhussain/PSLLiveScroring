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
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater.from(context));
            convertView = inflater.inflate(R.layout.row_match_fragment, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }
        mHolder = (ViewHolder) convertView.getTag();
        MatchStatus matchStatus = mCurrentData.AllMatchStatus.get(position);
        mHolder.teamA.setText(matchStatus.teamA);
        mHolder.teamB.setText(matchStatus.teamB);
        if(!TextUtils.isEmpty(matchStatus.battingTeam)) {
            mHolder.battingTeamScore.setText(matchStatus.getBattingTeamScore() + "");
            mHolder.battingTeamWickets.setText(matchStatus.getBattingTeamWickets() + "");
            mHolder.battingTeam.setText(matchStatus.battingTeam + "is batting at");
            mHolder.matchStatus.setText(matchStatus.getMatchStatus());

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

        public ViewHolder(View view) {
            teamA = (TextView) view.findViewById(R.id.teamA);
            teamB = (TextView) view.findViewById(R.id.teamB);
            battingTeam = (TextView) view.findViewById(R.id.battingTeam);
            battingTeamScore = (TextView) view.findViewById(R.id.battingScore);
            battingTeamWickets = (TextView) view.findViewById(R.id.battingWickets);
            matchStatus = (TextView) view.findViewById(R.id.matchStatus);
        }
    }
}
