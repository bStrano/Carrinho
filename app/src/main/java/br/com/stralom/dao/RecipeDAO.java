package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 17/01/2018.
 */

public class RecipeDAO extends GenericDAO {
    private static final String TAG = "RecipeDAO";
    public ItemRecipeDAO itemRecipeDAO;

    public RecipeDAO(Context context) {
        super(context, DBHelper.TABLE_RECIPE);
        itemRecipeDAO = new ItemRecipeDAO(context);
    }

    public ContentValues getContentValues(Recipe recipe){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_RECIPE_NAME,recipe.getName());
        contentValues.put(DBHelper.COLUMN_RECIPE_INGREDIENTCOUNT,recipe.getIgredientCount());
        contentValues.put(DBHelper.COLUMN_RECIPE_TOTAL,recipe.getId());
        contentValues.put(DBHelper.COLUMN_RECIPE_IMAGEPATH, recipe.getImagePath());
        contentValues.put(DBHelper.COLUMN_RECIPE_TOTAL,recipe.getTotal());
        return contentValues;
    }

    @Override
    public void remove(Long id) {
        itemRecipeDAO.deleteAllFromRecipe(id);
        super.remove(id);
    }

    public ArrayList<Recipe> getAll() {
        db = dbHelper.getReadableDatabase();
        ArrayList<Recipe> recipes = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_RECIPE,null);
        try {
            while(cursor.moveToNext()){
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_ID)));
                recipe.setImagePath(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_IMAGEPATH)));
                recipe.setIgredientCount(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_INGREDIENTCOUNT)));
                recipe.setTotal(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_TOTAL)));
                recipe.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_NAME)));

                recipes.add(recipe);
            }

        } catch (NullPointerException e){
            Log.e(TAG,"[NullPointerException] Empty recipe list");
        } finally {
            cursor.close();
        }


        return recipes;
    }
}
