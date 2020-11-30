package com.ftninformatika.favoritesmovies;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ftninformatika.favoritesmovies.ORMLite.DatabaseHelper;
import com.ftninformatika.favoritesmovies.adapters.DrawerListViewAdapter;
import com.ftninformatika.favoritesmovies.fragments.DetailsFragment;
import com.ftninformatika.favoritesmovies.fragments.SearchFragment;
import com.ftninformatika.favoritesmovies.fragments.SettingsFragment;
import com.ftninformatika.favoritesmovies.model.NavigationItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchFragment.onListItemClickListener {

    private boolean searchShown = false;
    private boolean settingsShown = false;
    private boolean detailsShown = false;

    //setupDrawerItems()
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    //onDrawerOpen and onDraweClosed = setUpDrawer
    private CharSequence drawerTitle;
    private CharSequence title;

    private DatabaseHelper databaseHelper;

    private final ArrayList<NavigationItem> navigationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();
        showSearchFragment();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
    }

    private void setupDrawer() {
        setupDrawerNavigationItems();
        title = drawerTitle = getTitle();
        setupDrawerItems();
        setupToolbar();
    }

    private void setupDrawerNavigationItems() {
        navigationItems.add(new NavigationItem(getString(R.string.drawer_favorites_title), getString(R.string.drawer_favorites_subtitle), R.drawable.star_icon));
        navigationItems.add(new NavigationItem(getString(R.string.drawer_settings_title), getString(R.string.drawer_settings_subtitle), R.drawable.settings_icon));
    }

    private void showSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        transaction.replace(R.id.root, searchFragment);
        transaction.commit();

        searchShown = true;
        settingsShown = false;
        detailsShown = false;

    }

    private void showDetailsFragment(String ImdbID) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setImdbID(ImdbID);
        transaction.replace(R.id.root, detailsFragment);
        transaction.commit();

        searchShown = false;
        settingsShown = false;
        detailsShown = true;

    }

    private void showSettingsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        transaction.replace(R.id.root, settingsFragment);
        transaction.commit();

        searchShown = false;
        settingsShown = true;
        detailsShown = false;
    }

    private void setupDrawerItems() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.leftDrawer);

        DrawerListViewAdapter adapter = new DrawerListViewAdapter(navigationItems, this);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showSearchFragment();
                        break;
                    case 1:
                        showSettingsFragment();
                        break;
                }
                drawerLayout.closeDrawer(drawerList);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchShown) {
            finish();
        } else if (settingsShown) {
            showSearchFragment();
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onListItemClicked(String ImdbID) {
        showDetailsFragment(ImdbID);

    }
}