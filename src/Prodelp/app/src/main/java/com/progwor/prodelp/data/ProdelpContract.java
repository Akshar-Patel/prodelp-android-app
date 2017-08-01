package com.progwor.prodelp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

//Defines table and column names for the prodelp database.
public class ProdelpContract {

    //Name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.progwor.prodelp";

    //Base URI which apps will use to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Possible paths appended to base content URI for possible URI's
    public static final String PDROUTINE_PATH = "pdroutine";
    public static final String PDACTIVITY_PATH = "pdactivity";
    public static final String PDGOAL_PATH = "pdgoal";
    public static final String PDPROGRESS_PDACTIVITY_PATH = "pdprogress_pdactivity";

    // Inner class that defines the routine table.
    public static final class PdroutineEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PDROUTINE_PATH).build();
        //Columns name
        public static final String NAME_COL = "name";
        public static final String START_DATE_COL = "start_date";
        public static final String END_DATE_COL = "end_date";
        //Columns index
        public static final int ID_COL_INDEX = 0;
        public static final int NAME_COL_INDEX = 1;
        public static final int START_DATE_COL_INDEX = 2;
        public static final int END_DATE_COL_INDEX = 3;

        //Builds the uri using id.
        public static Uri buildRoutineUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = PDROUTINE_PATH;

        //Defines the content type.
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/" + PDROUTINE_PATH;

    }

    // Inner class that defines the activity table.
    public static final class PdactivityEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PDACTIVITY_PATH).build();
        // Columns name
        public static final String NAME_COL = "name";
        public static final String START_TIME_COL = "start_time";
        public static final String END_TIME_COL = "end_time";
        public static final String PDROUTINE_ID_COL = "pdroutine_id";
        public static final String NOTIFY_COL = "notify";

        // Columns index
        public static final int ID_COL_INDEX = 0;
        public static final int NAME_COL_INDEX = 1;
        public static final int START_TIME_COL_INDEX = 2;
        public static final int END_TIME_COL_INDEX = 3;
        public static final int PDROUTINE_ID_COL_INDEX = 4;
        public static final int NOTIFY_COL_INDEX = 5;

        //Builds the uri using id.
        public static Uri buildActivityUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = PDACTIVITY_PATH;

        //Defines the content type.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PDACTIVITY_PATH;

    }

    // Inner class that defines the goal table.

    public static final class PdgoalEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PDGOAL_PATH).build();

        // Columns name
        public static final String NAME_COL = "name";
        public static final String DUE_DATE_COL = "due_date";
        public static final String COMPLETED_DATE_COL = "completed_date";
        public static final String PDACTIVITY_ID_COL = "pdactivity_id";

        // Columns index
        public static final int ID_COL_INDEX = 0;
        public static final int NAME_COL_INDEX = 1;
        public static final int DUE_DATE_COL_INDEX = 2;
        public static final int COMPLETED_DATE_COL_INDEX = 3;
        public static final int PDACTIVITY_ID_COL_INDEX = 4;

        //Builds the uri using id.
        public static Uri buildGoalUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = PDGOAL_PATH;

        //Defines the content type.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PDGOAL_PATH;

    }

    // Inner class that defines the activity table.
    public static final class PdprogressPdactivityEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PDPROGRESS_PDACTIVITY_PATH).build();
        // Columns name
        public static final String DATE_COL = "date";
        public static final String RATE_COL = "rate";
        // Columns index
        public static final int ID_COL_INDEX = 0;
        public static final int RATE_COL_INDEX = 1;
        public static final int DATE_COL_INDEX = 2;

        //Builds the uri using id.
        public static Uri buildPdprogressPdactivityUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = PDPROGRESS_PDACTIVITY_PATH;

        //Defines the content type.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PDPROGRESS_PDACTIVITY_PATH;


    }

}
