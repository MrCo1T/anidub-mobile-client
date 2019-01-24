package ru.mrcolt.anidubmobile.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.mrcolt.anidubmobile.fragments.DemoFragment;
import ru.mrcolt.anidubmobile.fragments.MediaDetailsInfoFragment;
import ru.mrcolt.anidubmobile.fragments.MediaEpisodesListFragment;

public class MediaDetailsFragmentViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MediaDetailsFragmentViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MediaDetailsInfoFragment.newInstance();
            case 1:
                return MediaEpisodesListFragment.newInstance();
            default:
                return DemoFragment.newInstance("DEBUG");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Описание";
            case 1:
                return "Эпизоды";
            default:
                return "Описание";
        }
    }

}