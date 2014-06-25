package com.kentheken.library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kcordero on 6/17/2014.
 */
public class GameFragment extends Fragment {
    private static final String TAG = "GameFragment";
    public static final String EXTRA_GAME_ID = "com.kentheken.library.game_id";

    public static GameFragment newInstance(int gameId) {
        Log.i(TAG, "newInstance");
        return new GameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, parent, false);

        return view;
    }
}
