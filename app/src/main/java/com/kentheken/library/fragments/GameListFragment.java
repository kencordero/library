package com.kentheken.library.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kentheken.library.R;
import com.kentheken.library.models.Game;
import com.kentheken.library.models.GameCollection;
import com.kentheken.library.models.PlatformCollection;

import java.util.ArrayList;

/**
 * Created by kcordero on 6/18/2014.
 */
public class GameListFragment extends ListFragment {
    private static final String TAG = GameListFragment.class.getSimpleName();
    private static final int REQUEST_PLATFORM = 0;
    private static final String DIALOG_PLATFORM = "platform";
    private ArrayList<Game> mGames;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onGameSelected(Game game);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach");
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
        mCallbacks = null;
    }

    public void updateUI() {
        ((GameAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = super.onCreateView(inflater, parent, savedInstanceState);
        //ListView listView = (ListView)view.findViewById(android.R.id.list);
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
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.i(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.fragment_item_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.menu_item_new_item:
                Game game = new Game();
                GameCollection.get(getActivity()).addGame(game);
                updateUI();
                mCallbacks.onGameSelected(game);
                return true;
            case R.id.menu_item_filter_by_platform:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PlatformFilterFragment dialog = PlatformFilterFragment.newInstance(PlatformCollection.get(getActivity()).getPlatformList());
                dialog.setTargetFragment(GameListFragment.this, REQUEST_PLATFORM);
                dialog.show(fm, DIALOG_PLATFORM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView lv, View v, int idx, long id)
    {
        Game game = ((GameAdapter)getListAdapter()).getItem(idx);
        mCallbacks.onGameSelected(game);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_PLATFORM) {
            String platformName = data.getStringExtra(PlatformFilterFragment.EXTRA_SELECTION);
            Log.i(TAG, "Platform: " + platformName);
            long platformId = PlatformCollection.get(getActivity()).getPlatformId(platformName);
            mGames = GameCollection.get(getActivity()).getGamesFilteredByPlatform(platformId);
        }
    }

}
