# AndroidDBM
Android Database Manager - A Simple Database Management Class for Android Development

The first time I used the SQLite libraries in Android Studio didn't go so well. Exceptions left right and center and code I didn't understand. What I needed was a simple way to manage the copying of a packaged database file to the local device and retrieving a valid SQLiteDatabase object from that file. The solution was AndroidDBM. Written in a few hours it is extremely basic. Its features are:
+ Copy a database file from the asset manager directory (assets) to the device/emulator
+ Confirm or check whether a database file exists locally 
+ Automatically add a version history to the database (somewhat unused)
+ Get a valid SQLiteDatabase object to perform queries with
+ Support for multiple database files/databases

Future ideas include:
* Full version management

# How to Use

Using Android Studio:

* Switch from Android to Project view in the tree browser on the left of AS
* Expand | YOUR_PROJECT > app  > src > main | and make a new | Android > Assets folder |
* Create a database file using external tools (SQLiteMan) and drag into the assets folder
* In the Java folder under YOU.YOURAPP make a new Java file called Database
* Copy and paste the contents of the DBMan.java file into your new Java file
* Change the package name on Line 1 to reflect your own package (can be found on your other Java files)
* Change default values within the Database file such as DBNAME and initialVersion
* Call the appropriate methods as below

# Methods

Initiate the Database class with a valid Context by calling it like so *within an existing "onCreate" method*: 
```Java
        Context context = getApplicationContext();
        Database database = new Database(context);
```

You may now use any of the following methods. Note that each method takes an optional dbName argument as a String. This is the name of your database *file* for example "mydatabase.db". You may leave this blank and the Database class will use the default database name as defined by *DBNAME = "test.db";*.
- *isDBLocal([String dbName])* Checks if the given database (or the default one as described by DBNAME in Database.java) has already been copied to the local device. Returns "true" if the DB is already on the device or "false" if not.
- *makeLocal([String dbName])* Takes the given dbName (or default), finds the file in the ASSETS folder and copies it to the local storage. Returns true on success. Used after checking with isDBLocal() to "install" the database. Generates error/success logs in console.
- *getLocalVersion([String dbName])* Retrieves the latest "verNum" and "verNote" data from the automatically generated "version" table to allow version tracking, returns a Pair as *Pair(String verNum, String verNote)*
- *updateversion(int newVersion, String verNotes)* Adds a new record to the version table representing the new version number (integer) and notes (String) for version tracking
- *getDB([String dbName])* Fetches an SQLiteDatabase object which provides access to full querying and SQLite functions. Called after ensuring the database is made local this will allow you to execute queries using Cursor objects.
- *close(SQLiteDatabase db)* called after database operations are finished. This takes a full SQLiteDatabase object, you should first call *SQLiteDatabase = Database.getDB;* perform your operations then call *Database.close(db);* to end the transaction and clear system resources.


# License
You are free to use, adapt, copy and distribute this code. Please be aware that the SQLite library provides a more in depth class at https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html 

*This Class is intended for simple database management for learning Android Development on in-house or private apps. I accept no liability for it's use.*



