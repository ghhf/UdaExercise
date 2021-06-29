package cn.happy.miwork.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import cn.happy.miwork.ColorsFragment;
import cn.happy.miwork.FamilyFragment;
import cn.happy.miwork.NumbersFragment;
import cn.happy.miwork.PhrasesFragment;
import cn.happy.miwork.R;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private int tabTitles[] = new int[]{R.string.category_numbers,R.string.category_colors,
            R.string.category_family,R.string.category_phrases};

    private Context mContext;

    public MyFragmentPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new NumbersFragment();
        }else if(position == 1){
            return new ColorsFragment();
        }else if(position == 2){
            return new FamilyFragment();
        }else {
            return new PhrasesFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(tabTitles[position]);
    }


}
