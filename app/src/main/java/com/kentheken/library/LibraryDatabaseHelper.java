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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import static com.kentheken.library.models.Game.FLAG.*;

public class LibraryDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "LibraryDatabaseHelper";
    private static final int DB_VERSION = 1;

    private static final String ID_COLUMN_NAME = "uuid";
    private static final String TITLE_COLUMN_NAME = "title";
    private static final String GAME_TABLE_NAME = "games";

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
    public LibraryDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
        Log.i(TAG, "init");
        mDbName = dbName;
        mContext = context;
        openDatabase();
    }
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDatabase() {
        Log.i(TAG, "createDatabase");
        boolean dbExists = checkDatabase();
        //boolean dbExists = false; // force update while debugging

        if (!dbExists) {
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
    }


    // public helper methods to access and get content from the database.
    public ArrayList<Game> loadGames() {
        ArrayList<Game> collection = new ArrayList<Game>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM games ORDER BY title", null);
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                do {
                    UUID gameId = UUID.fromString(cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME)));
                    String gameTitle = cursor.getString(cursor.getColumnIndex(TITLE_COLUMN_NAME));
                    collection.add(new Game(gameId, gameTitle, UNMODIFIED));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return collection;
    }

    public void saveGame(Game game) {
        if (game.getTitle().length() == 0) return; // won't save blank game or overwrite game with no title
        ContentValues values = new ContentValues();

        values.put(ID_COLUMN_NAME, game.getId().toString());
        values.put(TITLE_COLUMN_NAME, game.getTitle());
        switch (game.getFlag()) {
            case NEW:
                Log.i(TAG, "saveGame: insert " + game.getTitle());
                getWritableDatabase().insert(GAME_TABLE_NAME, null, values);
                break;
            case MODIFIED:
                Log.i(TAG, "saveGame: update " + game.getTitle());
                getWritableDatabase().update(GAME_TABLE_NAME, values, "uuid=?", new String[]{game.getId().toString()});
                break;
        }
        game.setFlag(UNMODIFIED);
    }
}