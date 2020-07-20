package com.tyl.quickmath.fragments;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tyl.quickmath.GlobalClass;
import com.tyl.quickmath.R;
import com.tyl.quickmath.databinding.ActivityFragmentViewPagerBinding;

import java.util.Locale;

public class FragmentViewPagerActivity extends AppCompatActivity {

    ActivityFragmentViewPagerBinding binding;
    GlobalClass global;
    SharedPreferences sharedPreferences;
    MediaPlayer mediaPlayer;
    String appLanguage;

    // tab titles
    private String[] titleHe = new String[]{"קל", "בינוני", "קשה"};
    private String[] titleEn = new String[] {"Easy",  "Medium", "Hard"};


    int intentFragment;
    boolean fromMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appLanguage = Locale.getDefault().getLanguage();
        fromMain = getIntent().getExtras().getBoolean("fromMain");
        binding = ActivityFragmentViewPagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        global = GlobalClass.getInstance();
        global.initializeMediaPlayer(this);
        sharedPreferences = this.getSharedPreferences("sound",this.MODE_PRIVATE);
        global.muteBackgroudMusic(sharedPreferences.getBoolean("mute_sound", false));
        intentFragment = getIntent().getExtras().getInt("frgToLoad");
        init();

    }

    private void init() {
        // removing toolbar elevation
        getSupportActionBar().setElevation(0);

        binding.viewPager.setAdapter(new ViewPagerFragmentAdapter(this));
        binding.viewPager.setCurrentItem(intentFragment);
        // attaching tab mediator
        if (appLanguage.equals("en")){
            new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                    (tab, position) -> tab.setText(titleEn[position])).attach();
        }
        else{
            new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                    (tab, position) -> tab.setText(titleHe[position])).attach();
        }
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
            if (appLanguage.equals("en")){
                return titleEn.length;
            }
            else{
                return titleHe.length;
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","in");
        global.playSound(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        global.pauseSong();
    }
}
