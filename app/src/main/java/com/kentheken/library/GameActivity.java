package com.kentheken.library;

import android.support.v4.app.Fragment;

/**
 * Created by kcordero on 6/18/2014.
 */
public class GameActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance();
    }
}
