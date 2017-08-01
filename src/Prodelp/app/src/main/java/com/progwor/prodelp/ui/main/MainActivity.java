package com.progwor.prodelp.ui.main;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdactivity;
import com.progwor.prodelp.core.Prodelp;
import com.progwor.prodelp.data.ProdelpContract;
import com.progwor.prodelp.ui.drawer.DrawerFragment;


public class MainActivity extends ActionBarActivity implements DrawerFragment.DrawerCallbacks,
        ContainerFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
   public DrawerFragment mDrawerFragment;
    private String[] mDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mDrawerItems = getResources().getStringArray(R.array.drawer_list);

        //Default selection in drawer
        setTitle(mDrawerItems[Prodelp.PDROUTINE]);

        //Set toolbar as actionbar
        Toolbar mToolbarMain = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbarMain);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set up the drawer.
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
        mDrawerFragment.setUp(R.id.drawer_fragment, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbarMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
            //FetchTask fetchTask = new FetchTask(this);
            //fetchTask.execute("Wake up");
            //Cursor cursor = getContentResolver()
              //      .query(ProdelpContract.PdactivityEntry.CONTENT_URI, Pdactivity.COLS, null, null, null);
            //cursor.moveToFirst();
            //Toast.makeText(this, cursor.getCount() + "", Toast.LENGTH_LONG).show();
       //     return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ContainerFragment.newInstance(position))
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
