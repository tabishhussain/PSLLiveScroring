package com.android.tabishhussain.psllivescoring.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tabishhussain.psllivescoring.DataClasses.PlayerInfo;
import com.android.tabishhussain.psllivescoring.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tabish on 1/20/16.
 */
public class TeamsFragment extends ListFragment {

    public TeamsFragment() {

    }

    String fileName;
    int[] imageIds = new int[16];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileName = getArguments().getString(getActivity().getString(R.string.key_filename));
        TypedArray images = getResources().obtainTypedArray(getArguments().
                getInt(getActivity().getString(R.string.key_imageIds)));
        for (int i = 0; i < images.length(); i++) {
            imageIds[i] = images.getResourceId(i, 0);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<PlayerInfo>>() {
        }.getType();
        List<PlayerInfo> playerInfoList = gson.fromJson(getTeamSquad(fileName), type);
        PlayersListAdapter playersListAdapter = new PlayersListAdapter(getActivity(),
                playerInfoList, imageIds);
        getListView().setAdapter(playersListAdapter);
        setListShown(true);
    }

    public class PlayersListAdapter extends BaseAdapter {

        List<PlayerInfo> teamMembers = new ArrayList<>();
        int[] imageIds;
        Context context;
        ViewHolder viewHolder;

        public PlayersListAdapter(Context context, List<PlayerInfo> teamMembers, int[] imageIds) {
            this.teamMembers = teamMembers;
            this.imageIds = imageIds;
            this.context = context;
        }

        @Override
        public int getCount() {
            return teamMembers.size();
        }

        @Override
        public Object getItem(int position) {
            return teamMembers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_player_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.name.setText(teamMembers.get(position).name);
            viewHolder.description.setText(teamMembers.get(position).description);
            if (imageIds[position] != 0) {
                viewHolder.thumbnail.setImageResource(imageIds[position]);
            } else {
                viewHolder.thumbnail.setImageResource(0);
                viewHolder.thumbnail.setBackgroundColor(ContextCompat.
                        getColor(getActivity(),android.R.color.black));
            }
            return convertView;
        }

        public class ViewHolder {
            public final TextView name;
            public final TextView description;
            public final ImageView thumbnail;

            public ViewHolder(View view) {
                name = (TextView) view.findViewById(R.id.name);
                description = (TextView) view.findViewById(R.id.description);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            }
        }
    }

    public String getTeamSquad(String fileName) {
        StringBuilder inputLine = new StringBuilder();
        String squad = "";
        try {
            InputStream inputStream = getActivity().getResources().openRawResource(
                    getActivity().getResources().getIdentifier("raw/" + fileName,
                            "raw", getActivity().getPackageName()));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                inputLine.append(line);
            bufferedReader.close();
            JSONObject jsonObject = new JSONObject(inputLine.toString());
            squad = jsonObject.getJSONArray("squad").toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return squad;
    }
}
