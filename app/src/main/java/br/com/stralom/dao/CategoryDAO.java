package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import br.com.stralom.entities.Category;

public class CategoryDAO extends GenericDAO {
    private static final String TAG = "CategoryDAO";

    public CategoryDAO(Context context) {
        super(context, DBHelper.TABLE_CATEGORY);
    }

    public ArrayList<Category> getAll(){
        ArrayList<Category> categories = new ArrayList<>();

        db = dbHelper.getReadableDatabase();

        try(Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CATEGORY, null)){
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAME));
                String nameInternacional = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAMEINTERNACIONAL));
                int isDefault = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_DEFAULT));
                int iconPath = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_ICON));

                Category category = new Category(name,nameInternacional,iconPath);
                category.setDefault(isDefault);

                categories.add(category);
            }
            cursor.close();
        } catch (NullPointerException e) {
            Log.e(TAG,"[Null Cursor] No category found.");
        }
        return categories;
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
