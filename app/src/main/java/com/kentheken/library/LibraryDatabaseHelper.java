package com.kentheken.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kentheken.library.models.Game;
import com.kentheken.library.models.GameCollection;
import com.kentheken.library.models.Platform;
import com.kentheken.library.models.PlatformCollection;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import static com.kentheken.library.models.Game.FLAG.*;

public class LibraryDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = LibraryDatabaseHelper.class.getSimpleName();
    private static final int DB_VERSION = 1;

    private static final String COL_ID = "_id";
    private static final String COL_UUID = "uuid";
    private static final String COL_TITLE = "title";
    private static final String COL_NAME = "name";
    private static final String COL_GAME_ID = "game_id";
    private static final String COL_PLATFORM_ID = "platform_id";
    private static final String TABLE_GAME = "games";
    private static final String TABLE_PLATFORM = "platforms";
    private static final String TABLE_GAME_PLATFORM = "game_platforms";

    private static LibraryDatabaseHelper sHelper;
    private static SQLiteDatabase mDatabase;
    private final Context mContext;
    private final String mDbName;
    private String mPath;

    public SQLiteDatabase getDb() {
        return mDatabase;
    }

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context activity or application context
     * @param dbName filename for database
     */
    private LibraryDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
        Log.i(TAG, "init");
        mDbName = dbName;
        mContext = context;
        openDatabase();
    }

    public static LibraryDatabaseHelper get(Context context, String dbName) {
        if (sHelper == null) {
            sHelper = new LibraryDatabaseHelper(context, dbName);
        }
        return sHelper;
    }
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDatabase() {
        Log.i(TAG, "createDatabase");
        boolean dbExists = checkDatabase();

        if (!dbExists) {
            createNewDatabase();
        }
    }

    private void createNewDatabase() {
        // By calling this method, an empty database will be created into the default system path
        // of your application so we are able to overwrite that database with ours.
        getReadableDatabase();
        try {
            copyDatabase();
        } catch (IOException e) {
            Log.e(TAG, "Couldn't copy database", e);
            throw new Error("Error copying database");
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDatabase() {
        Log.i(TAG, "checkDatabase");
        SQLiteDatabase database = null;

        try {
            database = SQLiteDatabase.openDatabase(getPath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e) {
            Log.e(TAG, "Error while checking db");                
        }

        if (database != null)
            database.close();

        return database != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring bytestream.
     * */
    private void copyDatabase() throws IOException {
        Log.i(TAG, "copyDatabase");
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(mDbName);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(getPath());

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private String getPath() {
        if (mPath == null) {
            mPath = mContext.getDatabasePath(mDbName).getPath();
            Log.i(TAG, "Path: " + mPath);
        }
        return mPath;
    }

    public SQLiteDatabase openDatabase() throws SQLException {
        Log.i(TAG, "openDatabase");
        //Open the database          
        if (mDatabase == null) {
            createDatabase();
            mDatabase = SQLiteDatabase.openDatabase(getPath(), null, SQLiteDatabase.OPEN_READONLY);
        }
        return mDatabase;
    }

    @Override
    public synchronized void close() {
            Log.i(TAG, "close");
        if (mDatabase != null)
            mDatabase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
        createNewDatabase();
    }


    // public helper methods to access and get content from the database.
    public ArrayList<Game> loadGames() {
        Log.i(TAG, "loadGames");
        ArrayList<Game> collection = new ArrayList<Game>();
        Cursor cursor = mDatabase.query(TABLE_GAME, null, null, null, null, null, COL_TITLE + " COLLATE NOCASE");
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                do {
                    UUID gameUuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(COL_UUID)));
                    String gameTitle = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                    int gameId = cursor.getInt(cursor.getColumnIndex(COL_ID));
                    collection.add(new Game(gameUuid, gameTitle, UNMODIFIED, gameId));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return collection;
    }

    public void saveGame(Game game) {
        Log.i(TAG, "saveGame");
        if (game.getTitle().length() == 0) return; // won't save blank game or overwrite game with no title
        ContentValues values = new ContentValues();

        values.put(COL_TITLE, game.getTitle());
        switch (game.getFlag()) {
            case NEW:
                values.put(COL_UUID, game.getUuid().toString());
                Log.i(TAG, "saveGame: insert " + game.getTitle());
                getWritableDatabase().insert(TABLE_GAME, null, values);
                break;
            case MODIFIED:
                Log.i(TAG, "saveGame: update " + game.getTitle());
                getWritableDatabase().update(TABLE_GAME, values, COL_UUID + " = ?", new String[]{game.getUuid().toString()});
                break;
        }
        game.setFlag(UNMODIFIED);
        saveGamePlatformIDs(game);
    }

    public ArrayList<Platform> loadPlatforms() {
        Log.i(TAG, "loadPlatforms");
        ArrayList<Platform> collection = new ArrayList<Platform>();
        Cursor cursor = mDatabase.query(TABLE_PLATFORM, null, null, null, null, null, COL_NAME + " COLLATE NOCASE");
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                do {
                    int platformId = cursor.getInt(cursor.getColumnIndex(COL_ID));
                    String platformName = cursor.getString(cursor.getColumnIndex(COL_NAME));
                    collection.add(new Platform(platformId, platformName));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return collection;
    }

    public ArrayList<Integer> getGamePlatformIDs(Game game) {
        Log.i(TAG, "getGamePlatformIDs");
        ArrayList<Integer> platformIDs = new ArrayList<Integer>();
        Cursor cursor = mDatabase.query(TABLE_GAME_PLATFORM, null, COL_GAME_ID + " = ?", new String[] {game.getUuid().toString()}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                do {
                    platformIDs.add(cursor.getInt(cursor.getColumnIndex(COL_PLATFORM_ID)));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return platformIDs;
    }

    public void saveGamePlatformIDs(Game game) {
        Log.i(TAG, "saveGamePlatformIDs");
        int idx = 0;
        for (Platform platform : PlatformCollection.get(mContext).getPlatforms()) {
            if (GameCollection.get(mContext).getPlatformSelections(game)[idx++]) {
                ContentValues values = new ContentValues();
                Log.i(TAG, game.getTitle() + ":" + platform.getName());
                values.put(COL_GAME_ID, game.getUuid().toString());
                values.put(COL_PLATFORM_ID, platform.getId());
                getWritableDatabase().insert(TABLE_GAME_PLATFORM, null, values);
            }
        }
        //TODO: remove ones that weren't in latest updates
        //getWritableDatabase().delete(TABLE_GAME_PLATFORM, COL_GAME_ID + " = ? AND " + COL_PLATFORM_ID + " NOT IN (?)", new String[] {});
    }
}