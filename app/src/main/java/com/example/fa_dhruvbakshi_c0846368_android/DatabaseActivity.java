package com.example.fa_dhruvbakshi_c0846368_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseActivity extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME="Locationdb";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="favouriteloc";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String LAT="lati";
    public static final String LONG="lng";

    public DatabaseActivity (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql =
                "CREATE TABLE IF NOT EXISTS favouriteloc("+
                        "id INTEGER CONSTRAINT pk PRIMARY KEY AUTOINCREMENT,"+
                        "name VARCHAR(20) NOT NULL,"+
                        "lati DOUBLE,"+
                        "long DOUBLE NOT NULL);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }
    void addItem(String name ,
                 double lati, double lng )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(NAME,name);
        cv.put(LAT,lati);
        cv.put(LONG,lng);
        long result = db.insert(TABLE_NAME,null,cv);
        if (result == -1)
        {
            Toast.makeText(context, "Adding Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, "Location Added To Fav List", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readData()
    {
        String sql="SELECT * FROM favouriteloc";
        SQLiteDatabase sqLiteDatabase= this.getReadableDatabase();
        Cursor cursor=null;
        if(sqLiteDatabase!=null)
        {
            cursor = sqLiteDatabase.rawQuery(sql,null);
        }
        return cursor;
    }
    void updateData(String id1,String name, double lati, double lng)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(NAME,name);
        cv.put(LAT,lati);
        cv.put(LONG,lng);
        long result = db.update("favouriteloc",cv,"id=?",new String[]{id1});
        if (result == -1)
        {
            Toast.makeText(context, "Updating Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }

    }
    void onDelete(String id1)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result =db.delete(TABLE_NAME,"id=?",new String[]{id1});
        if (result == -1)
        {
            Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
