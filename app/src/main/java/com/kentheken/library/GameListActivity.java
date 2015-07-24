package com.kentheken.library;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.kentheken.library.models.Game;

public class GameListActivity extends SingleFragmentActivity implements GameListFragment.Callbacks, GameFragment.Callbacks {
    private static final String TAG = "GameListActivity";

    @Override
    protected Fragment createFragment() {
        return new GameListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onGameSelected(Game game) {
        Log.i(TAG, "onGameSelected");
        if (findViewById(R.id.detailFragmentContainer) == null) {
            Log.i(TAG, "phone interface");
            Intent intent = new Intent(this, GamePagerActivity.class);
            intent.putExtra(GameFragment.EXTRA_GAME_ID, game.getUuid());
            startActivity(intent);
        }
        else {
            Log.i(TAG, "tablet interface");
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = GameFragment.newInstance(game.getUuid());

            if (oldDetail != null) {
                ft.remove(oldDetail);
            }

            ft.add(R.id.detailFragmentContainer, newDetail).commit();
        }
    }

    @Override
    public void onGameUpdated(Game game) {
        ((GameListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer))
                .updateUI();
    }
}
