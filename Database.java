package you.yourproject; //Change to match your own package

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Database {

    Context context;
    Database(Context context){
        this.context = context;
    }

    String DBNAME = "test.db";
    int initialVersion = 1;

    public boolean isDBLocal(){
       return isDBLocal(DBNAME);
    }

    public boolean isDBLocal(String checkDBName){
        File deviceDB = context.getFileStreamPath(DBNAME);
        return deviceDB.isFile();
    }

    public Pair getLocalVersion(){
        return this.getLocalVersion(DBNAME);
    }

    public Pair getLocalVersion(String dbName){
        SQLiteDatabase db = this.getDB(dbName);
        Cursor cursor = db.rawQuery("SELECT * FROM versions ORDER BY verID DESC LIMIT 1", null);

        cursor.moveToFirst();
        String verNum = cursor.getString(cursor.getColumnIndex("verNum"));
        String verNotes = cursor.getString(cursor.getColumnIndex("verNotes"));

        Pair info = new Pair(verNum, verNotes);

        db.close();
        return info;
    }

    public boolean updateVersion(int newVersion, String verNotes){
        boolean is;
        SQLiteDatabase db = this.getDB();

        db.execSQL("INSERT INTO versions (verNum, verNotes) VALUES('"+newVersion+"', '" + verNotes + "') ");
        is = true;
        db.close();
        return is;
    }

    public boolean makeLocal(){
        boolean is = this.makeLocal(DBNAME);
        return is;
    }

    public boolean makeLocal(String dbName){
        boolean is;
        try{
            AssetManager am = context.getAssets();
            InputStream dbFile = am.open(dbName);
            FileOutputStream outStream = context.openFileOutput(dbName, Context.MODE_PRIVATE);


            byte[] fileBuffer = new byte[1024];
            int read;
            while((read = dbFile.read(fileBuffer)) != -1){
                outStream.write(fileBuffer, 0, read);
            }

            SQLiteDatabase db = this.getDB();
            db.execSQL("CREATE TABLE versions (" +
                    "verID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "verNum INTEGER," +
                    "verNotes TEXT)" );

            this.updateVersion(initialVersion, "New Local DB Installation");



            Log.d("DB", "DB was copied to local storage");
            dbFile.close();
            outStream.close();
            is = true;
        }catch(Exception e){
            Log.d("DB", "ERROR " + e.toString());
            is = false;
        }
        return is;
    }


    public SQLiteDatabase getDB(){
        return this.getDB(DBNAME);
    }

    public SQLiteDatabase getDB(String dbName){
        String dbPath = context.getFilesDir().toString() + "/" + dbName;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, 0);
        return db;
    }

    public void close(SQLiteDatabase db){
        db.close();
    }

} //End of Database class
