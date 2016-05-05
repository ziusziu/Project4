package siu.example.com.headingout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends BaseActivity {
    private static Toolbar mToolBar;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainFragment mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_container, mainFragment);
        fragmentTransaction.commit();

        Fragment fragment = fragmentManager.findFragmentById(R.id.home_fragment_container);
//        if(fragment.getClass().getSimpleName().equals("DetailFragment")){
//            initToolBar();
//        }

    }

    private void initToolBar(){
        mToolBar = (Toolbar)findViewById(R.id.detail_toolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected int getDrawerLayoutResource() {
        return R.id.home_drawer_layout;
    }

    @Override
    protected int getToolBarResource() {
        return R.id.home_toolBar;
    }

    @Override
    protected int getNavViewResource() {
        return R.id.home_nav_view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }

    @Override
    protected String getToolBarTitle() {

        Fragment fragment = new MainFragment();
        //Fragment fragment = fragmentManager.findFragmentById(R.id.home_fragment_container);
        return fragment.getClass().getSimpleName();
    }
}
