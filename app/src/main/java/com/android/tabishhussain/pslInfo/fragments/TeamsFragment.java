package com.android.tabishhussain.pslInfo.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tabishhussain.pslInfo.DataClasses.PlayerInfo;
import com.android.tabishhussain.pslInfo.R;
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
import java.util.List;

/**
 * Created by tabish on 1/20/16.
 * Display all the teams along with the team members and there portrait image
 */
public class TeamsFragment extends Fragment {

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
        images.recycle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teams, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<PlayerInfo>>() {
        }.getType();
        List<PlayerInfo> playerInfoList = gson.fromJson(getTeamSquad(fileName), type);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(playerInfoList, imageIds);
        recyclerView.setAdapter(myRecyclerAdapter);
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
        private List<PlayerInfo> feedItemList;
        private int[] imageIds;

        public MyRecyclerAdapter(List<PlayerInfo> feedItemList, int[] imageIDs) {
            this.feedItemList = feedItemList;
            this.imageIds = imageIDs;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_player_list, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder customViewHolder, int i) {
            PlayerInfo feedItem = feedItemList.get(i);
            customViewHolder.name.setText(feedItem.name);
            customViewHolder.description.setText(feedItem.description);
            customViewHolder.thumbnail.setImageResource(imageIds[i]);
        }

        @Override
        public int getItemCount() {
            return (null != feedItemList ? feedItemList.size() : 0);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public final TextView name;
            public final TextView description;
            public final ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
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
