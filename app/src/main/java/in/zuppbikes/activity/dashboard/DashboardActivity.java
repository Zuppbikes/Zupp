package in.zuppbikes.activity.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zupp.component.AppHelper;
import com.zupp.component.storage.AppUserSession;

import java.util.ArrayList;

import in.zuppbikes.R;
import in.zuppbikes.activity.customer.BookingsFragment;
import in.zuppbikes.activity.customer.CustomerLoginFragment;
import in.zuppbikes.activity.customer.GarageFragment;
import in.zuppbikes.activity.customer.SelectVehicleFragment;
import in.zuppbikes.activity.customer.ZuppReturnFragment;
import in.zuppbikes.activity.login.LoginActivity;
import in.zuppbikes.adapters.NavigationOptionsAdapter;
import in.zuppbikes.models.Navigation;

public class DashboardActivity extends AppCompatActivity implements NavigationOptionsAdapter.OnItemClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private boolean doubleBackToExitPressedOnce = false;
    private RecyclerView mRVNavigationOptions;
    private NavigationOptionsAdapter mAdapter;
    private ArrayList<Navigation> mOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initToolbar();
        initView();
        showLandingPage();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager() != null) {
                    getCurrentFragment().onResume();
                }
            }
        });
    }

    private void showLandingPage(){
        if (AppUserSession.getInstance().getCustomerDetail() != null) {
            if (!AppUserSession.getInstance().getCustomerDetail().hasBooking) {
                replaceFragment(SelectVehicleFragment.newInstance(AppUserSession.getInstance().getCustomerDetail()), true);
            } else {
                replaceFragment(ZuppReturnFragment.newInstance(AppUserSession.getInstance().getCustomerDetail().booking), true);
            }
        } else {
            replaceFragment(new CustomerLoginFragment(), true);
        }
    }

    private void initView() {
        initDrawerLayout();
        mRVNavigationOptions = findViewById(R.id.rvNavigationOptions);
        mAdapter = new NavigationOptionsAdapter(this, R.layout.row_options, getOptions());
        mRVNavigationOptions.setLayoutManager(new LinearLayoutManager(this));
        mRVNavigationOptions.setAdapter(mAdapter);
        setListeners();
    }

    private void setListeners() {
        mAdapter.setOnItemClickListener(this);
    }

    private void initDrawerLayout() {
        mDrawer = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    /**
     * Toolbar initialization and customization
     */
    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * Sets toolbar title
     *
     * @param iTitle
     */
    public void setToolBarTitle(String iTitle) {
        ((TextView) mToolbar.findViewById(R.id.toolbar_title)).setText(iTitle);
    }

    /**
     * Replaces fragment in the frame container
     *
     * @param iFragment Fragment
     */
    public void replaceFragment(Fragment iFragment) {
        FragmentTransaction aTransaction = getSupportFragmentManager().beginTransaction();
        aTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        aTransaction.replace(R.id.frameContainer, iFragment);
        aTransaction.commitAllowingStateLoss();
    }

    /**
     * Replaces fragment in the frame container
     *
     * @param iFragment Fragment
     */
    public void replaceFragment(Fragment iFragment, boolean clear) {
        if (clear) {
            for (int i = 0; i <= getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        FragmentTransaction aTransaction = getSupportFragmentManager().beginTransaction();
        aTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        aTransaction.add(R.id.frameContainer, iFragment);
        aTransaction.addToBackStack(null);
        aTransaction.commit();
    }

    /**
     * Replaces fragment in the frame container
     *
     * @param iFragment Fragment
     */
    public void popAndReplace(Fragment iFragment, boolean pop) {
        if (pop) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        FragmentTransaction aTransaction = getSupportFragmentManager().beginTransaction();
        aTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        aTransaction.add(R.id.frameContainer, iFragment);
        aTransaction.addToBackStack(null);
        aTransaction.commit();
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frameContainer);
    }

    private ArrayList<Navigation> getOptions() {
        mOptions.clear();
        mOptions.add(new Navigation(1, 0, getString(R.string.garage)));
        mOptions.add(new Navigation(2, 0, getString(R.string.bookings)));
        mOptions.add(new Navigation(3, 0, getString(R.string.logout)));
        return mOptions;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
//        else if (!(getCurrentFragment() instanceof SelectVehicleFragment) || !(getCurrentFragment() instanceof ZuppReturnFragment)) {
//            showLandingPage();
//        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else if (doubleBackToExitPressedOnce) {
            AppHelper.getInstance().setSessionListener(null);
            finish();
            return;
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.msg_click_back_again_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_garage) {
//            if (!(getCurrentFragment() instanceof GarageFragment)) {
//                replaceFragment(new GarageFragment(), false);
//            }
//        } else if (id == R.id.nav_logout) {
//            AppUserSession.getInstance().clearSavedData();
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return false;
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null && ev.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onItemClick(final Navigation iItem) {
        mDrawer.closeDrawer(GravityCompat.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (iItem.id) {
                    case 1:
                        if (!(getCurrentFragment() instanceof GarageFragment)) {
                            replaceFragment(new GarageFragment(), false);
                        }
                        break;

                    case 2:
                        if (!(getCurrentFragment() instanceof BookingsFragment)) {
                            replaceFragment(new BookingsFragment(), false);
                        }
                        break;
                    case 3:
                        AppUserSession.getInstance().clearSavedData();
                        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
            }
        }, 300);
    }


}
