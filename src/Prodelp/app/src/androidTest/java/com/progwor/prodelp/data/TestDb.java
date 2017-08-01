package com.progwor.prodelp.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by akshar on 6/3/15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(ProdelpDbHelper.DATABASE_NAME);
    }

    /*
    This function gets called before each test is executed to delete the database. This makes
    sure that we always have a clean test.
    */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
    Students: Uncomment this test once you've written the code to create the routine
    table. Note that you will have to have chosen the same column names that I did in
    my solution for this test to compile, so if you haven't yet done that, this is
    a good time to change your column names to match mine.
    Note that this only tests that the routine table has the correct columns, since we
    give you the code for the weather table. This test does not look at the
    */
    public void testCreateDb() throws Throwable {
// build a HashSet of all of the table names we wish to look for
// Note that there will be another table in the DB that stores the
// Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(ProdelpContract.PdroutineEntry.TABLE_NAME);

        mContext.deleteDatabase(ProdelpDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new ProdelpDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

// have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

// verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

// if this fails, it means that your database doesn't contain both the routine entry
// and weather entry tables
        assertTrue("Error: Your database was created without the routine entry table",
                tableNameHashSet.isEmpty());

// now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + ProdelpContract.PdroutineEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

// Build a HashSet of all of the column names we want to look for
        final HashSet<String> routineColumnHashSet = new HashSet<String>();
        routineColumnHashSet.add(ProdelpContract.PdroutineEntry._ID);
        routineColumnHashSet.add(ProdelpContract.PdroutineEntry.NAME_COL);
        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            routineColumnHashSet.remove(columnName);
        } while (c.moveToNext());

// if this fails, it means that your database doesn't contain all of the required routine
// entry columns
        assertTrue("Error: The database doesn't contain all of the required routine entry columns",
                routineColumnHashSet.isEmpty());
        db.close();
    }

    /*
    Here is where you will build code to test that we can insert and query the
    prodelp database.
    */


    public void testRoutineTable() {

    }

    public long insertRoutine() {
        return -1L;
    }
}
