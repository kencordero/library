package com.kentheken.library;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kentheken.library.models.Game;

public class GameListActivity extends SingleFragmentActivity implements GameListFragment.Callbacks {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            Log.i(TAG, "Settings selected");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGameSelected(Game game) {
        Log.i(TAG, "onGameSelected");
        if (findViewById(R.id.detailFragmentContainer) == null) {
            Log.i(TAG, "phone interface");
            Intent intent = new Intent(this, GamePagerActivity.class);
            intent.putExtra(GameFragment.EXTRA_GAME_ID, game.getUUID());
            startActivity(intent);
        }
        else {
            Log.i(TAG, "tablet interface");
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = GameFragment.newInstance(game.getUUID());

            if (oldDetail != null) {
                ft.remove(oldDetail);
            }

            ft.add(R.id.detailFragmentContainer, newDetail).commit();
        }

    }
}
