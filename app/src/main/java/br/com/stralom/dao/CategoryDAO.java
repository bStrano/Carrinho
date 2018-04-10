package br.com.stralom.dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        } catch (NullPointerException e) {
            Log.e(TAG,"[Null Cursor] No category found.");
        }
        return categories;
    }
}
