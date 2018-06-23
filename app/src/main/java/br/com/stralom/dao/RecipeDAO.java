package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import br.com.stralom.entities.Item;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 17/01/2018.
 */

public class RecipeDAO extends GenericDAO {
    private static final String TAG = "RecipeDAO";
    private final ItemRecipeDAO itemRecipeDAO;

    public RecipeDAO(Context context) {
        super(context, DBHelper.TABLE_RECIPE);
        itemRecipeDAO = new ItemRecipeDAO(context);
    }

    public Long add(Recipe recipe){
        return super.add(getContentValues(recipe));
    }

    public Recipe add(String name, ArrayList<ItemRecipe> items, String imagePath){
        Recipe recipe = new Recipe(name,items, null);
        Long recipeId = add(recipe);
        Log.e(TAG, String.valueOf(recipeId));
        recipe.setId(recipeId);
        return recipe;
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

    public void update(Recipe recipe){
        super.update(DBHelper.COLUMN_RECIPE_ID, recipe.getId(),getContentValues(recipe));
    }

    @Override
    public void remove(Long id) {
        itemRecipeDAO.deleteAllFromRecipe(id);
        super.remove(id);
    }

     public Recipe findByName(String name){
        db = dbHelper.getReadableDatabase();
        Recipe recipe = null;

        String sql = "SELECT * FROM " + DBHelper.TABLE_RECIPE + " WHERE " +
                DBHelper.COLUMN_RECIPE_NAME + " = ?";

        Cursor c = db.rawQuery(sql,new String[] {name});

        if(c != null  && c.moveToFirst()){
            recipe = getRecipe(c);
            c.close();
        }

        return  recipe;
     }

    public ArrayList<Recipe> getAll() {
        db = dbHelper.getReadableDatabase();
        ArrayList<Recipe> recipes = new ArrayList<>();

        try (Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_RECIPE, null)) {
            while (cursor.moveToNext()) {
                Recipe recipe = getRecipe(cursor);

                recipes.add(recipe);
            }
            cursor.close();
        } catch (NullPointerException e) {
            Log.e(TAG, "[NullPointerException] Empty recipe list");
        }



        return recipes;
    }

    private Recipe getRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_ID)));
        recipe.setImagePath(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_IMAGEPATH)));
        recipe.setIgredientCount(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_INGREDIENTCOUNT)));
        recipe.setTotal(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_TOTAL)));
        recipe.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RECIPE_NAME)));
        return recipe;
    }
}
