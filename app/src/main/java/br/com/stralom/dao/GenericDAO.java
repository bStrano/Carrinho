package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Bruno Strano on 04/01/2018.
 */

public abstract class GenericDAO  {
    SQLiteDatabase db;
    final DBHelper dbHelper;
    private final String tableName;


    GenericDAO(Context context, String tableName) {
        dbHelper = new DBHelper(context);
        this.tableName = tableName;
    }

    public Long add(ContentValues contentValues) {
        db = dbHelper.getWritableDatabase();
        return db.insertOrThrow(tableName,null,contentValues);
    }


    public void remove(Long id){
        db = dbHelper.getReadableDatabase();
        db.delete(tableName,"id = ?", new String[] {id.toString()});
    }

    public void update(String columnId, Long idObject, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(tableName,contentValues,columnId + " = ?",new String[] {idObject.toString()});
    }


}
