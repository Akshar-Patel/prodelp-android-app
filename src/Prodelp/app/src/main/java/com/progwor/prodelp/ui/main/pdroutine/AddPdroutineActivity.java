package com.progwor.prodelp.ui.main.pdroutine;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import com.progwor.prodelp.R;
import com.progwor.prodelp.core.Prodelp;

public class AddPdroutineActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private EditText pdroutineNameEditText;
    private Button startDateButton, endDateButton;
    private CharSequence mPdroutineName;
    private CharSequence startDate = "";
    private CharSequence endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdroutine_add_pdroutine_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddPdroutineFragment())
                    .commit();
        }

       /* toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Prodelp.setUpToolbar(this,R.string.add_pdroutine_activity_title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pdroutine_add_pdroutine_menu, menu);
        return true;
    }

}
