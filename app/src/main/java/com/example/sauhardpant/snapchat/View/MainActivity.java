package com.example.sauhardpant.snapchat.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sauhardpant.snapchat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  ViewPagerAdapter viewPagerAdapter;
  @BindView(R.id.activity_main_viewpager)
  ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    viewPager.setCurrentItem(1);
  }

  private class ViewPagerAdapter extends FragmentPagerAdapter {

    private ViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      switch (i) {
        case 0:
          return new ChatFragment();
        case 1:
          return new CameraFragment();
        case 2:
          return new StoryFragment();
        default:
          return new CameraFragment();
      }
    }

    @Override
    public int getCount() {
      return 3;
    }
  }
}
