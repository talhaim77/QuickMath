package com.tyl.quickmath.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tyl.quickmath.databinding.ActivityFragmentViewPagerBinding;

public class FragmentViewPagerActivity extends AppCompatActivity {

    ActivityFragmentViewPagerBinding binding;

    // tab titles
    private String[] titles = new String[]{"Easy", "Medium", "Hard"};
    int intentFragment;
    boolean fromMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromMain = getIntent().getExtras().getBoolean("fromMain");
        binding = ActivityFragmentViewPagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intentFragment = getIntent().getExtras().getInt("frgToLoad");
        init();

    }

    private void init() {
        // removing toolbar elevation
        getSupportActionBar().setElevation(0);

        binding.viewPager.setAdapter(new ViewPagerFragmentAdapter(this));
        binding.viewPager.setCurrentItem(intentFragment);
        // attaching tab mediator
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();
    }

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new EasyFragment();
                case 1:
                    return new MediumFragment();
                case 2:
                    return new HardFragment();
            }
            return new EasyFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}
