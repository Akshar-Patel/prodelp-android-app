package com.progwor.prodelp.ui.drawer;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Prodelp;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private DrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;


    //Layout for drawer
    private DrawerLayout mDrawerLayout;

    //Container for fragment for drawer
    private View mContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private String[] mDrawerItem;
    private int[] mDrawerItemIcon;
    private ListView mDrawerListView;
    private ListView mDrawerBottomListView;

    private CharSequence mTitle;

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        //If savedInstance present, use previously selected position..

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        //Get names for drawer list
        mDrawerItem = getResources().getStringArray(R.array.drawer_list);

        //Get icons for drawer list
        mDrawerItemIcon = new int[]{
                R.drawable.pdroutine,
                R.drawable.pdgoal,
                R.drawable.pdprogress,
                R.drawable.pdreview,
        };

        //Select default or previously selected item
        selectItem(mCurrentSelectedPosition);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //Inflate view for drawer
        View view = inflater.inflate(R.layout.drawer_fragment, container, false);
        // Inflate the layout for this fragment
        mDrawerListView = (ListView) view.findViewById(R.id.drawer_listview);
        // Set item click listener
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        //Set the adapter for drawer list
        DrawerAdapter drawerAdapter = new DrawerAdapter(getActivity(), mDrawerItem, mDrawerItemIcon);
        mDrawerListView.setAdapter(drawerAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

       /* mDrawerBottomListView = (ListView) view.findViewById(R.id.drawer_listview1);
        // Set item click listener
        mDrawerBottomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        //Set the adapter for drawer list
        DrawerAdapter drawerAdapter1 = new DrawerAdapter(getActivity(), mDrawerItem, mDrawerItemIcon);
        mDrawerBottomListView.setAdapter(drawerAdapter);
        mDrawerBottomListView.setItemChecked(mCurrentSelectedPosition, true);
            */

        return view;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        /**
         * Container View for Fragment of Drawer
         */
        mContainerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                toolbar,             /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
                if (!isAdded()) {
                    return;
                }
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Prodelp");
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Prodelp");
            mDrawerLayout.openDrawer(mContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        //Set toggle listener for drawer
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    //Select item in drawer list
    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        switch (position) {
            case Prodelp.PDROUTINE:
                mTitle = mDrawerItem[Prodelp.PDROUTINE];
                break;
            case Prodelp.PDGOAL:
                mTitle = mDrawerItem[Prodelp.PDGOAL];
                break;
            case Prodelp.PDREVIEW:
                mTitle = mDrawerItem[Prodelp.PDREVIEW];
                break;
            case Prodelp.PDPROGRESS:
                mTitle = mDrawerItem[Prodelp.PDPROGRESS];
                break;
        }
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    // Called when a fragment is first attached to its activity.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (DrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement DrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface DrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

}