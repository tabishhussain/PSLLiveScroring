package com.android.tabishhussain.pslInfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tabishhussain.pslInfo.Constants;
import com.android.tabishhussain.pslInfo.DataClasses.PslSchedule;
import com.android.tabishhussain.pslInfo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tabish on 1/21/16.
 */
public class PslScheduleFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ScheduleAdapter adapter = new ScheduleAdapter(Constants.allMatchesSchedule, getActivity());
        getListView().setDividerHeight(0);
        getListView().setAdapter(adapter);
        setListShown(true);
    }

    public class ScheduleAdapter extends BaseAdapter{

        List<PslSchedule> scheduleList = new ArrayList<>();
        Context context;
        ViewHolder viewHolder;

        public ScheduleAdapter(List<PslSchedule> scheduleList, Context context) {
            this.scheduleList = scheduleList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return scheduleList.size();
        }

        @Override
        public Object getItem(int position) {
            return scheduleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.row_psl_schedule,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            PslSchedule pslSchedule =  scheduleList.get(position);
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.team1.setImageResource(pslSchedule.team1thumbnail);
            viewHolder.team2.setImageResource(pslSchedule.team2thumbnail);
            viewHolder.place.setText("At " + pslSchedule.place);
            viewHolder.date.setText(pslSchedule.getDate());
            return convertView;
        }

        public class ViewHolder{
            public final ImageView team1;
            public final ImageView team2;
            public final TextView place;
            public final TextView date;

            public ViewHolder(View view){
                team1 = (ImageView)view.findViewById(R.id.team1);
                team2 = (ImageView)view.findViewById(R.id.team2);
                place = (TextView)view.findViewById(R.id.place);
                date = (TextView)view.findViewById(R.id.matchTime);
            }
        }

    }
}
