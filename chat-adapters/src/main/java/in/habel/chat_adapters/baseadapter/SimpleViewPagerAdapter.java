package in.habel.chat_adapters.baseadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by instio on 26/4/17.
 */

public class SimpleViewPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;
    private String[] titles;

    SimpleViewPagerAdapter(FragmentManager fm, Fragment[] fragments) throws Exception {
        super(fm);
        if (fragments == null) {
            throw new Exception("Fragments cannot be null");
        }
        this.fragments = fragments;
    }

    SimpleViewPagerAdapter(FragmentManager fm, Fragment[] fragments, String titles[]) throws Exception {
        this(fm, fragments);
        if (titles != null && titles.length != fragments.length) {
            throw new Exception("Fragment and title size doesn't match");
        }
        this.titles = titles;
    }

    SimpleViewPagerAdapter(ViewPager viewpager, FragmentManager fm, Fragment[] fragments, String titles[]) throws Exception {
        this(fm, fragments, titles);
        viewpager.setAdapter(this);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}