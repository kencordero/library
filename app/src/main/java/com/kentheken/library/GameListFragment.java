package com.kentheken.library;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kentheken.library.models.Game;
import com.kentheken.library.models.GameCollection;

import java.util.ArrayList;

/**
 * Created by kcordero on 6/18/2014.
 */
public class GameListFragment extends ListFragment {
    private static final String TAG = "GameListFragment";
    private ArrayList<Game> mGames;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = super.onCreateView(inflater, parent, savedInstanceState);
        ListView listView = (ListView)view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setHasOptionsMenu(true);
        setRetainInstance(true);
        
        getActivity().setTitle(R.string.games_title);
        mGames = GameCollection.get(getActivity()).getGames();
        GameAdapter adapter = new GameAdapter(mGames);
    }

    private class GameAdapter extends ArrayAdapter<Game> {
        public GameAdapter(ArrayList<Game> games) {
            super(getActivity(), 0, games);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_game, null);
            Game game = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.game_list_itemTitle);
            titleTextView.setText(game.getTitle());

            return convertView;
        }
    }
}
