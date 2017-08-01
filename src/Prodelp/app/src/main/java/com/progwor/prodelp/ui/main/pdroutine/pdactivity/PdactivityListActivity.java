package com.progwor.prodelp.ui.main.pdroutine.pdactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Pdroutine;
import com.progwor.prodelp.core.Prodelp;

public class PdactivityListActivity extends ActionBarActivity {

    PdactivityListFragment mPdactivityListFragment;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdactivity_list_activity);
        bundle = new Bundle();


        //Toast.makeText(this,pdroutineId+"",Toast.LENGTH_LONG).show();
        bundle.putLong(Pdroutine.ID_STRING, getIntent().getLongExtra(Pdroutine.ID_STRING, 0));
        if (savedInstanceState == null) {
            mPdactivityListFragment = new PdactivityListFragment();
            mPdactivityListFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mPdactivityListFragment)
                    .commit();
        }

        Prodelp.setUpToolbar(this,R.string.pdactivity_list_activity_title);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setTitle(R.string.pdactivity_list_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pdactivity_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_accept) {
            Intent intent = new Intent(this, AddPdactivityActivity.class);
            intent.putExtra(Pdroutine.ID_STRING, getIntent().getLongExtra(Pdroutine.ID_STRING, 0));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
