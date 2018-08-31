package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import br.com.stralom.entities.Category;

public class CategoryDAO extends GenericDAO {
    private static final String TAG = "CategoryDAO";

    public CategoryDAO(Context context) {
        super(context, DBHelper.TABLE_CATEGORY);
    }


    public Category findByName(String categoryName) {
         return getOne(DBHelper.COLUMN_CATEGORY_NAME + " = ?", new String[]{categoryName} );
    }

    public Category getOne(String whereCondition, String[] args) {
        Category category = null;

        db = dbHelper.getReadableDatabase();


        String sql = "SELECT * FROM " + DBHelper.TABLE_CATEGORY;
        if(whereCondition != null){
            sql = sql + " WHERE " + whereCondition;
        }

        Cursor cursor = db.rawQuery(sql, args);

        if(cursor != null){
            cursor.moveToFirst();
            category = getCategory(cursor);
            cursor.close();
        }

        db.close();

        return category;

    }

    public ArrayList<Category> getAll(String whereSQL, String[] args){
        ArrayList<Category> categories = new ArrayList<>();

        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        if(whereSQL == null){
            cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CATEGORY , null);
        } else {
            cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CATEGORY + whereSQL, args);
        }

        try{
            while(cursor.moveToNext()){
                Category category = getCategory(cursor);

                categories.add(category);
            }
            cursor.close();
        } catch (NullPointerException e) {
            Log.d(TAG,"[Null Cursor] No category found.");
        }
        db.close();
        return categories;
    }

    public ArrayList<Category> getAll() {
        return getAll(" WHERE " + DBHelper.COLUMN_CATEGORY_NAME + " != ?" ,new String[]{DBHelper.CATEGORY_TEMPORARY_PRODUCT});
    }
    @NonNull
    private Category getCategory(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAME));
        String nameInternational = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAMEINTERNACIONAL));
        int isDefault = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_DEFAULT));
        int iconPath = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_ICON));

        Category category = new Category(name,nameInternational,iconPath);
        category.setDefault(isDefault);
        return category;
    }

    public void add(Category category){
        super.add(getContentValues(category));
    }

    public Category add(String name, String nameInternational, int icon){
        Category category = new Category(name, nameInternational, icon);
        this.add(category);
        return category;
    }

    public ContentValues getContentValues(Category category){
        ContentValues contentValues = new ContentValues();
        Log.e(TAG, category.toString());
        contentValues.put(DBHelper.COLUMN_CATEGORY_NAME, category.getName());
        contentValues.put(DBHelper.COLUMN_CATEGORY_ICON, category.getIconFlag());
        contentValues.put(DBHelper.COLUMN_CATEGORY_NAMEINTERNACIONAL, category.getNameInternacional());
        return  contentValues;
    }



}
