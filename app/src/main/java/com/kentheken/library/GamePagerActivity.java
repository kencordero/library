package com.kentheken.library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kentheken.library.models.Game;
import com.kentheken.library.models.GameCollection;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kcordero on 6/25/2014.
 */
public class GamePagerActivity extends FragmentActivity implements GameFragment.Callbacks {
    private static final String TAG = "GamePagerActivity";
    private ViewPager mViewPager;
    private ArrayList<Game> mGames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
        mGames = GameCollection.get(this).getGames();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            public Fragment getItem(int position) {
                Log.i(TAG, "getItem, position: " + position);
                Game game = mGames.get(position);
                return GameFragment.newInstance(game.getUuid());
            }

            public int getCount() {
                return mGames.size();
            }
        });

        UUID uuid = (UUID)getIntent().getSerializableExtra(GameFragment.EXTRA_GAME_ID);
        for (int i = 0; i < mGames.size(); ++i) {
            if (mGames.get(i).getUuid().equals(uuid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fragment_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_save_and_add_another:
                Game game = new Game();
                GameCollection.get(this).addGame(game);
                mViewPager.getAdapter().notifyDataSetChanged();
                mViewPager.setCurrentItem(mGames.size() - 1, true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGameUpdated(Game game) { }
}
