package com.progwor.prodelp.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.progwor.prodelp.data.ProdelpContract.PdroutineEntry;

/**
 * Created by akshar on 6/3/15.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
This helper function deletes all records from both database tables using the ContentProvider.
It also queries the ContentProvider to make sure that the database has been successfully
deleted, so it cannot be used until the Query and Delete functions have been written
in the ContentProvider.
Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
the delete functionality in the ContentProvider.
*/
    public void deleteAllRecordsFromProvider() {

        mContext.getContentResolver().delete(
                PdroutineEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                PdroutineEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();

    }

    /*
Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
you have implemented delete functionality there.
*/
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }
    // Since we want each test to start with a clean slate, run deleteAllRecords
// in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }
    /*
    This test checks to make sure that the content provider is registered correctly.
    Students: Uncomment this test to make sure you've correctly registered the ProdelpProvider.
    */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
// We define the component name based on the package name from the context and the
// ProdelpProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                ProdelpProvider.class.getName());
        try {
// Fetch the provider info using the component name from the PackageManager
// This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
// Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: ProdelpProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + ProdelpContract.CONTENT_AUTHORITY,
                    providerInfo.authority, ProdelpContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
// I guess the provider isn't registered correctly.
            assertTrue("Error: ProdelpProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }
    /*
    This test doesn't touch the database. It verifies that the ContentProvider returns
    the correct type for each type of URI that it can handle.
    Students: Uncomment this test to verify that your implementation of GetType is
    functioning correctly.
    */
    public void testGetType() {
// content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(ProdelpContract.PdroutineEntry.CONTENT_URI);
// vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the PdroutineEntry CONTENT_URI should return PdroutineEntry.CONTENT_TYPE",
                PdroutineEntry.CONTENT_TYPE, type);
    }

    public void testBasicRoutineQuery() {
// insert our test records into the database
        ProdelpDbHelper dbHelper = new ProdelpDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createRoutineValues();
        long routineRowId = TestUtilities.insertRoutineValues(mContext);
        assertTrue("Unable to Insert WeatherEntry into the Database", routineRowId != -1);
        db.close();
// Test the basic content provider query
        Cursor routineCursor = mContext.getContentResolver().query(
                ProdelpContract.PdroutineEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
// Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherQuery", routineCursor, testValues);
    }

}
