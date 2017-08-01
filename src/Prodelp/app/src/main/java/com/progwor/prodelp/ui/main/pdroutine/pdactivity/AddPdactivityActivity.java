package com.progwor.prodelp.ui.main.pdroutine.pdactivity;

import android.app.Service;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Prodelp;

import java.util.Calendar;

public class AddPdactivityActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdactivity_add_pdactivity_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddPdactivityFragment())
                    .commit();
        }

        /*Toolbar mToolbar = (Toolbar) findViewById(R.id.common_toolbar);
        mToolbar.setTitle(R.string.add_pdactivity_activity_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Prodelp.setUpToolbar(this, R.string.add_pdactivity_activity_title);
       }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pdactivity_add_pdactivity_menu, menu);
        return true;
    }

}
