package com.florianmski.tracktoid.data.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;
import com.florianmski.tracktoid.utils.Utils;
import com.florianmski.tracktoid.data.provider.TraktoidContent.Episode;
import com.florianmski.tracktoid.data.provider.TraktoidContent.Movie;
import com.florianmski.tracktoid.data.provider.TraktoidContent.Season;
import com.florianmski.tracktoid.data.provider.TraktoidContent.Show;

import java.util.ArrayList;

/**
 * This class was generated by the ContentProviderCodeGenerator project made by Foxykeep
 * <p>
 * (More information available https://github.com/foxykeep/ContentProviderCodeGenerator)
 */
public final class TraktoidProviderOld extends ContentProvider {

    private static final String LOG_TAG = TraktoidProviderOld.class.getSimpleName();

    private static final boolean ACTIVATE_ALL_LOGS = false;

    protected static final String DATABASE_NAME = "TraktoidProvider.db";

    public static final String AUTHORITY = "com.florianmski.tracktoid.provider.TraktoidProvider";

    public static final Uri INTEGRITY_CHECK_URI = Uri.parse("content://" + AUTHORITY
            + "/integrityCheck");

    // Version 1 : Creation of the database
    public static final int DATABASE_VERSION = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private enum UriType {
        SHOW            (Show.TABLE_NAME, Show.TABLE_NAME, Show.TYPE_DIR_TYPE),
        SHOW_ID         (Show.TABLE_NAME + "/#", Show.TABLE_NAME, Show.TYPE_ELEM_TYPE),
        SEASON          (Show.TABLE_NAME + "/#/" + Season.TABLE_NAME, Season.TABLE_NAME, Season.TYPE_DIR_TYPE),
        SEASON_ID       (Show.TABLE_NAME + "/#/" + Season.TABLE_NAME + "/#", Season.TABLE_NAME, Season.TYPE_ELEM_TYPE),
        EPISODE         (Show.TABLE_NAME + "/#/" + Episode.TABLE_NAME, Episode.TABLE_NAME, Episode.TYPE_DIR_TYPE),
        EPISODE_ID      (Show.TABLE_NAME + "/#/" + Episode.TABLE_NAME + "/#", Episode.TABLE_NAME, Episode.TYPE_ELEM_TYPE),
        EPISODE_AIRED   (Episode.TABLE_NAME + Episode.AIRED, Episode.TABLE_NAME, Episode.TYPE_DIR_TYPE),
        MOVIE           (Movie.TABLE_NAME, Movie.TABLE_NAME, Movie.TYPE_DIR_TYPE),
        MOVIE_ID        (Movie.TABLE_NAME + "/*", Movie.TABLE_NAME, Movie.TYPE_ELEM_TYPE);

        private String mTableName;
        private String mType;

        UriType(String matchPath, String tableName, String type) {
            mTableName = tableName;
            mType = type;
            sUriMatcher.addURI(AUTHORITY, matchPath, ordinal());
        }

        String getTableName() {
            return mTableName;
        }

        String getType() {
            return mType;
        }
    }

    static {
        // Ensures UriType is initialized
        UriType.values();
    }

    private static UriType matchUri(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match < 0) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return UriType.class.getEnumConstants()[match];
    }

    private SQLiteDatabase mDatabase;

    @SuppressWarnings("deprecation")
    public synchronized SQLiteDatabase getDatabase(Context context) {
        // Always return the cached database, if we've got one
        if (mDatabase == null || !mDatabase.isOpen()) {
            DatabaseHelper helper = new DatabaseHelper(context, DATABASE_NAME);
            mDatabase = helper.getWritableDatabase();
            if (mDatabase != null) {
                mDatabase.setLockingEnabled(true);
            }
        }

        return mDatabase;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String name) {
            super(context, name, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "Creating TraktoidProvider database");

            // Create all tables here; each class has its own method
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Show | createTable start");
            }
            Show.createTable(db);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Show | createTable end");
            }
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Season | createTable start");
            }
            Season.createTable(db);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Season | createTable end");
            }
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Episode | createTable start");
            }
            Episode.createTable(db);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Episode | createTable end");
            }
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Movie | createTable start");
            }
            Movie.createTable(db);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Movie | createTable end");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // Upgrade all tables here; each class has its own method
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Show | upgradeTable start");
            }
            Show.upgradeTable(db, oldVersion, newVersion);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Show | upgradeTable end");
            }
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Season | upgradeTable start");
            }
            Season.upgradeTable(db, oldVersion, newVersion);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Season | upgradeTable end");
            }
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Episode | upgradeTable start");
            }
            Episode.upgradeTable(db, oldVersion, newVersion);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Episode | upgradeTable end");
            }
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Movie | upgradeTable start");
            }
            Movie.upgradeTable(db, oldVersion, newVersion);
            if (ACTIVATE_ALL_LOGS) {
                Log.d(LOG_TAG, "Movie | upgradeTable end");
            }
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        UriType uriType = matchUri(uri);
        Context context = getContext();

        // Pick the correct database for this operation
        SQLiteDatabase db = getDatabase(context);
        String id = "0";

        if (ACTIVATE_ALL_LOGS) {
            Log.d(LOG_TAG, "delete: uri=" + uri + ", match is " + uriType.name());
        }

        int result = -1;

        switch (uriType) {
            case SHOW_ID:
            case SEASON_ID:
            case EPISODE_ID:
            case MOVIE_ID:
            case SEASON:
            case EPISODE:
                result = db.delete(uriType.getTableName(), whereWithId(uri, uriType, selection),
                        selectionArgs);
                break;

            case SHOW:
            case MOVIE:
                result = db.delete(uriType.getTableName(), selection, selectionArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public String getType(Uri uri) {
        return matchUri(uri).getType();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        UriType uriType = matchUri(uri);
        Context context = getContext();

        // Pick the correct database for this operation
        SQLiteDatabase db = getDatabase(context);
        long id;

        if (ACTIVATE_ALL_LOGS) {
            Log.d(LOG_TAG, "insert: uri=" + uri + ", match is " + uriType.name());
        }

        Uri resultUri = null;

        switch (uriType) {
            case SHOW:
            case SEASON:
            case EPISODE:
            case MOVIE:
                id = db.insert(uriType.getTableName(), "foo", values);
                resultUri = id == -1 ? null : ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Notify with the base uri, not the new uri (nobody is watching a new
        // record)
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        SQLiteDatabase db = getDatabase(getContext());
        db.beginTransaction();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
                db.yieldIfContendedSafely();
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        UriType uriType = matchUri(uri);
        Context context = getContext();

        // Pick the correct database for this operation
        SQLiteDatabase db = getDatabase(context);

        if (ACTIVATE_ALL_LOGS) {
            Log.d(LOG_TAG, "bulkInsert: uri=" + uri + ", match is " + uriType.name());
        }

        int numberInserted = 0;
        SQLiteStatement insertStmt;

        db.beginTransaction();
        try {
            switch (uriType) {
                case SHOW:
                    insertStmt = db.compileStatement(Show.getBulkInsertString());
                    for (ContentValues value : values) {
                        Show.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (ACTIVATE_ALL_LOGS) {
                        Log.d(LOG_TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                case SEASON:
                    insertStmt = db.compileStatement(Season.getBulkInsertString());
                    for (ContentValues value : values) {
                        Season.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (ACTIVATE_ALL_LOGS) {
                        Log.d(LOG_TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                case EPISODE:
                    insertStmt = db.compileStatement(Episode.getBulkInsertString());
                    for (ContentValues value : values) {
                        Episode.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (ACTIVATE_ALL_LOGS) {
                        Log.d(LOG_TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                case MOVIE:
                    insertStmt = db.compileStatement(Movie.getBulkInsertString());
                    for (ContentValues value : values) {
                        Movie.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (ACTIVATE_ALL_LOGS) {
                        Log.d(LOG_TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
        } finally {
            db.endTransaction();
        }

        // Notify with the base uri, not the new uri (nobody is watching a new
        // record)
        context.getContentResolver().notifyChange(uri, null);
        return numberInserted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor c = null;
        Uri notificationUri = TraktoidContent.CONTENT_URI;
        UriType uriType = matchUri(uri);
        Context context = getContext();
        // Pick the correct database for this operation
        SQLiteDatabase db = getDatabase(context);

        if (ACTIVATE_ALL_LOGS) {
            Log.d(LOG_TAG, "query: uri=" + uri + ", match is " + uriType.name());
        }

        switch (uriType) {
            case SHOW_ID:
            case SEASON_ID:
            case EPISODE_ID:
            case MOVIE_ID:
            case SEASON:
            case EPISODE:
                c = db.query(uriType.getTableName(), projection, whereWithId(uri, uriType, selection),
                        selectionArgs, null, null, sortOrder);
                break;
            case SHOW:
            case MOVIE:
                c = db.query(uriType.getTableName(), projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
        }

        if ((c != null) && !isTemporary()) {
//            c.setNotificationUri(getContext().getContentResolver(), notificationUri);
            // TODO see if it change something when opening the app for the first time (do not flood with event)
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    private String whereWithId(Uri uri, UriType uriType, String selection) {
        StringBuilder sb = new StringBuilder(256);

        switch(uriType)
        {
            case SHOW_ID:
                String id = uri.getPathSegments().get(1);
                sb.append(Show.Columns.TVDB_ID.getName());
                sb.append(" = ");
                sb.append(id);
                break;
            case SEASON:
            case SEASON_ID:
                String showId = uri.getPathSegments().get(1);

                sb.append(Season.Columns.SHOW_TVDB_ID.getName());
                sb.append(" = ");
                sb.append(showId);

                if(uri.getPathSegments().size() > 3)
                {
                    String seasonId = uri.getPathSegments().get(3);
                    sb.append(" AND ");

                    sb.append(Season.Columns.SEASON.getName());
                    sb.append(" = ");
                    sb.append(seasonId);
                }
                break;
            case EPISODE:
            case EPISODE_ID:
                showId = uri.getPathSegments().get(1);

                sb.append(Episode.Columns.SHOW_TVDB_ID.getName());
                sb.append(" = ");
                sb.append(showId);

                if(uri.getPathSegments().size() > 3)
                {
                    String episodeId = uri.getPathSegments().get(3);
                    sb.append(" AND ");

                    sb.append(Episode.Columns.TVDB_ID.getName());
                    sb.append(" = ");
                    sb.append(episodeId);
                }
                break;
            case MOVIE_ID:
                id = uri.getPathSegments().get(1);
                sb.append(Movie.Columns.IMDB_ID.getName());
                sb.append(" = ");
                // because imdb_id is a string and not an int we have to do 'imdb_id'
                sb.append("'");
                sb.append(id);
                sb.append("'");
                break;
        }

        if (selection != null) {
            sb.append(" AND (");
            sb.append(selection);
            sb.append(')');
        }
        return sb.toString();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        UriType uriType = matchUri(uri);
        Context context = getContext();

        // Pick the correct database for this operation
        SQLiteDatabase db = getDatabase(context);

        if (ACTIVATE_ALL_LOGS) {
            Log.d(LOG_TAG, "update: uri=" + uri + ", match is " + uriType.name());
        }

        int result = -1;

        switch (uriType) {
            case SHOW_ID:
            case SEASON_ID:
            case EPISODE_ID:
            case MOVIE_ID:
            case SEASON:
            case EPISODE:
                result = db.update(uriType.getTableName(), values, whereWithId(uri, uriType, selection),
                        selectionArgs);
                break;
            case SHOW:
            case MOVIE:
                result = db.update(uriType.getTableName(), values, selection, selectionArgs);
                break;
            case EPISODE_AIRED:
                // in selectionArgs[0] we have the timestamp of the lastSync so we don't have to count all aired episodes
                // each time but just sum the new ones
                String select =
                        "(" + "SELECT COUNT(*) " +
                                "FROM " + TraktoidContent.Episode.TABLE_NAME + " " +
                                "WHERE " + TraktoidContent.Episode.Columns.FIRST_AIRED.getName() + ">" + selectionArgs[0] + " " + // unknown aired date (probably TBA)
                                "AND " + TraktoidContent.Episode.Columns.FIRST_AIRED.getName() + "<=" + Utils.getPSTTimestamp() + " " + // aired date should be less than right now
                                "AND " + TraktoidContent.Episode.TABLE_NAME + "." + TraktoidContent.Episode.Columns.SEASON.getName() + "=" + TraktoidContent.Season.TABLE_NAME + "." + TraktoidContent.Season.Columns.SEASON.getName() + ")";

                String update =
                        "UPDATE " + TraktoidContent.Season.TABLE_NAME + " " +
                                "SET " + Season.Columns.EPISODES_AIRED.getName() + "=" + Season.Columns.EPISODES_AIRED.getName() + "+" + select;

                db.execSQL(update);

                // we'd like to notify seasons and shows because this update some of their fields
                // TODO
//                getContext().getContentResolver().notifyChange(Season.CONTENT_URI, null);
                getContext().getContentResolver().notifyChange(Show.createUri(), null);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public boolean onCreate() {
        return true;
    }
}
