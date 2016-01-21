package com.android.tabishhussain.pslInfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

    Spinner mSpinner;
    ScheduleAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar_actionbar);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.row_spinner_item, getResources().getStringArray(R.array.Spinnerfilter));
        TextView textView = (TextView)mToolBar.findViewById(R.id.title1);
        textView.setText("Match Schedule");
        mSpinner = (Spinner) mToolBar.findViewById(R.id.mSpinner);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                ((TextView) view).setGravity(Gravity.END);
                ((TextView) view).setTextColor(ContextCompat.
                        getColor(getActivity(), R.color.colorPrimaryDark));
                adapter.applyFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ScheduleAdapter(Constants.allMatchesSchedule, getActivity());
        getListView().setAdapter(adapter);
        mSpinner.setSelection(0);
        mSpinner.setVisibility(View.VISIBLE);
        setListShown(true);
    }

    public class ScheduleAdapter extends BaseAdapter{

        List<PslSchedule> filteredScheduleList = new ArrayList<>();
        List<PslSchedule> scheduleList = new ArrayList<>();
        Context context;
        ViewHolder viewHolder;

        public ScheduleAdapter(List<PslSchedule> scheduleList, Context context) {
            this.scheduleList = scheduleList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return filteredScheduleList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredScheduleList.get(position);
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
            PslSchedule pslSchedule =  filteredScheduleList.get(position);
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.team1.setImageResource(pslSchedule.team1thumbnail);
            viewHolder.team2.setImageResource(pslSchedule.team2thumbnail);
            viewHolder.place.setText("At " + pslSchedule.place);
            viewHolder.date.setText(pslSchedule.getDate());
            return convertView;
        }

        public void applyFilter(int team){
            String teamName = "";
            switch (team){
                case 0:
                    filteredScheduleList = scheduleList;
                    break;
                case 1:
                    teamName = "Lahore";
                    break;
                case 2:
                    teamName = "Karachi";
                    break;
                case 3:
                    teamName = "Quetta";
                    break;
                case 4:
                    teamName = "Peshawar";
                    break;
                case 5:
                    teamName = "Islamabad";
                    break;
            }
            if(team != 0){
                filteredScheduleList = new ArrayList<>();
                for (PslSchedule schedule : scheduleList) {
                  if(schedule.team1.equalsIgnoreCase(teamName)
                          || schedule.team2.equalsIgnoreCase(teamName)) {
                      filteredScheduleList.add(schedule);
                  }
                }
            }
            notifyDataSetChanged();
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
