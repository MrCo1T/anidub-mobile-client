package ru.mrcolt.anidub.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import ru.mrcolt.anidub.fragments.DetailsInfoFragment;
import ru.mrcolt.anidub.fragments.EpisodesListFragment;

public class DetailsViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public DetailsViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DetailsInfoFragment.newInstance();
            case 1:
                return EpisodesListFragment.newInstance();
            default:
                return DetailsInfoFragment.newInstance();
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
