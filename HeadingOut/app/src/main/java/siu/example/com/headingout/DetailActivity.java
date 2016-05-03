package siu.example.com.headingout;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class DetailActivity extends AppCompatActivity {

    private static Toolbar mToolBar;
    public static String POSITION = "POSITION";
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolBar = (Toolbar)findViewById(R.id.detail_toolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager)findViewById(R.id.detail_viewPager);
        mViewPager.setAdapter(new DetailTabsFragmentPagerAdapter(getSupportFragmentManager()));

        mTabLayout = (TabLayout)findViewById(R.id.detail_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    // Save and restore last known tab position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
