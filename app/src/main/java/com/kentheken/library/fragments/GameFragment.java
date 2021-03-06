package com.kentheken.library.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kentheken.library.R;
import com.kentheken.library.models.Game;
import com.kentheken.library.models.GameCollection;
import com.kentheken.library.models.Platform;
import com.kentheken.library.models.PlatformCollection;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class GameFragment extends Fragment {
    private static final String TAG = GameFragment.class.getSimpleName();
    public static final String EXTRA_GAME_ID = "com.kentheken.library.game_id";
    private static final String DIALOG_PLATFORM = "platform";
    private static final int REQUEST_PLATFORM_LIST = 0;
    private Callbacks mCallbacks;

    private TextView mPlatformListText;
    private Game mGame;

    public interface Callbacks {
        void onGameUpdated(Game game);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static GameFragment newInstance(UUID gameIdx) {
        Log.i(TAG, "newInstance");
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_GAME_ID, gameIdx);

        GameFragment fragment = new GameFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setRetainInstance(true);

        UUID gameIdx = (UUID) getArguments().getSerializable(EXTRA_GAME_ID);
        Log.i(TAG, "Game ID: " + gameIdx);
        mGame = GameCollection.get(getActivity()).getGame(gameIdx);
        Log.i(TAG, "Game:" + mGame.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_game, parent, false);
        EditText titleField = (EditText) view.findViewById(R.id.fragment_game_titleField);
        titleField.setText(mGame.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mGame.setTitle(s.toString());
                    if (mGame.getFlag() == Game.Flag.UNMODIFIED) {
                        mGame.setFlag(Game.Flag.MODIFIED);
                    }
                    mCallbacks.onGameUpdated(mGame);
                    getActivity().setTitle(mGame.getTitle());
                }
            }

            public void afterTextChanged(Editable s) {
            }
        });

        Button platformSelectButton = (Button) view.findViewById(R.id.fragment_game_platform_select);
        platformSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                PlatformSelectFragment dialog = PlatformSelectFragment.newInstance(
                        PlatformCollection.get(getActivity()).getPlatformList(),
                        GameCollection.get(getActivity()).getPlatformSelections(mGame));
                dialog.setTargetFragment(GameFragment.this, REQUEST_PLATFORM_LIST);
                dialog.show(fm, DIALOG_PLATFORM);
            }
        });

        mPlatformListText = (TextView) view.findViewById(R.id.fragment_game_platform_list);
        updatePlatformList();

        return view;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if (mGame.getFlag() != Game.Flag.UNMODIFIED) {
            Log.i(TAG, "save changes");
            GameCollection.get(getActivity()).saveGame(mGame);
        } else {
            Log.i(TAG, "unmodified; no need to save " + mGame.getTitle());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_PLATFORM_LIST) {
            boolean[] selections = data.getBooleanArrayExtra(PlatformSelectFragment.EXTRA_SELECTION);
            mGame.setPlatformSelections(selections);
            if (mGame.getFlag() == Game.Flag.UNMODIFIED) mGame.setFlag(Game.Flag.MODIFIED);
            updatePlatformList();
        }
    }

    private void updatePlatformList() {
        StringBuilder builder = new StringBuilder();
        if (GameCollection.get(getActivity()).getPlatformSelections(mGame) != null) {
            int idx = 0;
            for (Platform platform : PlatformCollection.get(getActivity()).getPlatforms()) {
                if (mGame.getPlatformSelections()[idx++]) {
                    if (builder.length() != 0) {
                        builder.append(", ");
                    }
                    builder.append(platform.getName());
                }
            }
        }
        if (builder.length() != 0) {
            mPlatformListText.setTypeface(null, Typeface.NORMAL);
            mPlatformListText.setText(builder.toString());
        }
        else {
            mPlatformListText.setTypeface(null, Typeface.ITALIC);
            mPlatformListText.setText(R.string.empty_platform_list);
        }
    }
}