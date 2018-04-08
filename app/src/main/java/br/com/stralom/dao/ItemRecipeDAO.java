package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 15/01/2018.
 */

public class ItemRecipeDAO extends GenericDAO {
    private static final String TAG = "ItemRecipeDAO";


    public ItemRecipeDAO(Context context) {
        super(context,DBHelper.TABLE_ITEMRECIPE);
    }


    public void deleteAllFromRecipe(Long idRecipe){
        db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_ITEMRECIPE,DBHelper.COLUMN_ITEMRECIPE_RECIPE + " = ?" , new String[] {idRecipe.toString()});
    }

    public List<ItemRecipe> getAllByRecipe(Long recipeId){
        db = dbHelper.getReadableDatabase();
        ArrayList<ItemRecipe>  itemRecipeList = new ArrayList<>();
        String sql = "SELECT ir." + DBHelper.COLUMN_ITEMRECIPE_ID + ", ir." + DBHelper.COLUMN_ITEMRECIPE_PRODUCT
                + " , ir." + DBHelper.COLUMN_ITEMRECIPE_TOTAL + " , ir." + DBHelper.COLUMN_ITEMRECIPE_AMOUNT
                + " , p." + DBHelper.COLUMN_PRODUCT_NAME + " ,p." + DBHelper.COLUMN_PRODUCT_PRICE + " , p." + DBHelper.COLUMN_PRODUCT_CATEGORY +
                " FROM " + DBHelper.TABLE_ITEMRECIPE + " ir " +
                " JOIN " + DBHelper.TABLE_PRODUCT + " p " +
                " ON ir." + DBHelper.COLUMN_ITEMRECIPE_PRODUCT + " = p." + DBHelper.COLUMN_PRODUCT_ID +
                " WHERE ir." + DBHelper.COLUMN_ITEMRECIPE_RECIPE + " =?";

        try (Cursor c = db.rawQuery(sql, new String[]{recipeId.toString()})) {
            while (c.moveToNext()) {
                // Product
                Long idProduct = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_PRODUCT));
                String name = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
                double price = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_PRICE));
                String category = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_CATEGORY));
                Product product = new Product(idProduct, name, price, category);
                // Item Recipe
                Long id = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_ID));
                int amount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_AMOUNT));
                double total = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_TOTAL));
                Recipe recipe = new Recipe();
                recipe.setId(recipeId);
                ItemRecipe itemRecipe = new ItemRecipe(amount, product, recipe);
                itemRecipe.setId(id);
                itemRecipe.setTotal(total);

                itemRecipeList.add(itemRecipe);

            }
        } catch (NullPointerException e) {
            Log.e(TAG, "[NullPointerException] Ingredients not found.");
        }
        return itemRecipeList;
    }


    public ContentValues getContentValues(ItemRecipe itemRecipe){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_ID,itemRecipe.getId());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_AMOUNT,itemRecipe.getAmount());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_TOTAL,itemRecipe.getTotal());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_PRODUCT,itemRecipe.getProduct().getId());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_RECIPE, itemRecipe.getRecipe().getId());

        return contentValues;
    }





}
