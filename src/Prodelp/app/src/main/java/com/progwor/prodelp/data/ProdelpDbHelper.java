package com.progwor.prodelp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProdelpDbHelper extends SQLiteOpenHelper {

    //Defines the name of database file
    static final String DATABASE_NAME = "prodelp.db";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    //Default required constructor
    public ProdelpDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Defines create table statement
        final String SQL_CREATE_PDROUTINE_TABLE = "CREATE TABLE " +
                ProdelpContract.PdroutineEntry.TABLE_NAME + " (" +
                ProdelpContract.PdroutineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProdelpContract.PdroutineEntry.NAME_COL + " TEXT NOT NULL," +
                ProdelpContract.PdroutineEntry.START_DATE_COL + " TEXT NOT NULL," +
                ProdelpContract.PdroutineEntry.END_DATE_COL + " TEXT NOT NULL);";

        //Executes create table statemnt
        db.execSQL(SQL_CREATE_PDROUTINE_TABLE);

        //Defines create table statement
        final String SQL_CREATE_PDACTIVITY_TABLE = "CREATE TABLE " +
                ProdelpContract.PdactivityEntry.TABLE_NAME + " (" +
                ProdelpContract.PdactivityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProdelpContract.PdactivityEntry.NAME_COL + " TEXT NOT NULL," +
                ProdelpContract.PdactivityEntry.START_TIME_COL + " TEXT NOT NULL," +
                ProdelpContract.PdactivityEntry.END_TIME_COL + " TEXT NOT NULL," +
                ProdelpContract.PdactivityEntry.PDROUTINE_ID_COL + " INTEGER NOT NULL," +
                ProdelpContract.PdactivityEntry.NOTIFY_COL + " INTEGER," +
                " FOREIGN KEY (" + ProdelpContract.PdactivityEntry.PDROUTINE_ID_COL + ") REFERENCES " +
                ProdelpContract.PdroutineEntry.TABLE_NAME + " (" + ProdelpContract.PdroutineEntry._ID + ") ON DELETE CASCADE);";

        //Executes create table statemnt
        db.execSQL(SQL_CREATE_PDACTIVITY_TABLE);

        //Defines create table statement
        final String SQL_CREATE_PDGOAL_TABLE = "CREATE TABLE " +
                ProdelpContract.PdgoalEntry.TABLE_NAME + " (" +
                ProdelpContract.PdgoalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProdelpContract.PdgoalEntry.NAME_COL + " TEXT NOT NULL," +
                ProdelpContract.PdgoalEntry.DUE_DATE_COL + " TEXT NOT NULL," +
                ProdelpContract.PdgoalEntry.COMPLETED_DATE_COL + " TEXT," +
                ProdelpContract.PdgoalEntry.PDACTIVITY_ID_COL + " INTEGER," +
                " FOREIGN KEY (" + ProdelpContract.PdgoalEntry.PDACTIVITY_ID_COL + ") REFERENCES " +
                ProdelpContract.PdactivityEntry.TABLE_NAME + " (" + ProdelpContract.PdactivityEntry._ID + ") ON DELETE SET NULL);";

        //Executes create table statemnt
        db.execSQL(SQL_CREATE_PDGOAL_TABLE);

        //Defines create table statement
        final String SQL_CREATE_PDPROGRESS_PDACTIVITY_TABLE = "CREATE TABLE " +
                ProdelpContract.PdprogressPdactivityEntry.TABLE_NAME + " (" +
                ProdelpContract.PdprogressPdactivityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProdelpContract.PdprogressPdactivityEntry.RATE_COL + " INTEGER NOT NULL," +
                ProdelpContract.PdprogressPdactivityEntry.DATE_COL + " TEXT NOT NULL);";

        //Executes create table statemnt
        db.execSQL(SQL_CREATE_PDPROGRESS_PDACTIVITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Deletes all data on upgrade
        //db.execSQL("DROP TABLE IF EXISTS " + PdroutineEntry.TABLE_NAME);
        onCreate(db);
    }
}
