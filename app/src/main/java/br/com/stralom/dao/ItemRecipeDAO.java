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
    private ProductDAO productDAO;


    public ItemRecipeDAO(Context context) {
        super(context,DBHelper.TABLE_ITEMRECIPE);
        this.productDAO = new ProductDAO(context);
    }

    @Override
    public Long add(ContentValues contentValues) {
        return super.add(contentValues);
    }

    public void deleteAllFromRecipe(Long idRecipe){
        db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_ITEMRECIPE,DBHelper.COLUMN_ITEMRECIPE_RECIPE + " = ?" , new String[] {idRecipe.toString()});
    }

    public List<ItemRecipe> getAll(Long recipeId){
        db = dbHelper.getReadableDatabase();
        ArrayList<ItemRecipe>  ingredients = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + DBHelper.TABLE_ITEMRECIPE + " WHERE " + DBHelper.COLUMN_ITEMRECIPE_RECIPE + " = ?";
            Cursor c = db.rawQuery(sql, new String[] {recipeId.toString()});


            while (c.moveToNext()){
                Long id = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_ID));
                int amount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_AMOUNT));
                double total = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_TOTAL));
                Long productId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_PRODUCT));


                Product product = productDAO.findById(productId);
                Recipe recipe = new Recipe();
                recipe.setId(recipeId);
                ItemRecipe ingredient = new ItemRecipe(amount,product,recipe);
                ingredient.setId(id);
                ingredients.add(ingredient);
                Log.i(TAG,"ITEM: " + ingredient.toString());
                Log.i(TAG, String.valueOf(c.getCount()));
            }
            c.close();
        } catch (NullPointerException e) {
            Log.e(TAG,"[NullPointerException] Ingredients not found.");
        }
        return ingredients;
    }


//    public ArrayList<ItemRecipe> getAllFromRecipe(Long recipeId) {
//        db = dbHelper.getReadableDatabase();
//        ArrayList<ItemRecipe> items = new ArrayList<>();
//
//        String sql = "SELECT ir." + DBHelper.COLUMN_ITEMRECIPE_AMOUNT + ", ir." + ", ir." + DBHelper.COLUMN_ITEMRECIPE_RECIPE + ", ir." + DBHelper.COLUMN_ITEMRECIPE_ID +
//                ", ir." + DBHelper.COLUMN_ITEMRECIPE_PRODUCT + ", ir."
//                ", p." + DBHelper.COLUMN_PRODUCT_NAME +
//                " FROM " + DBHelper.TABLE_ITEMRECIPE + " ir " +
//                " JOIN " + DBHelper.TABLE_PRODUCT + " p " +
//                " ON ir." + DBHelper.COLUMN_ITEMRECIPE_PRODUCT + " = " + DBHelper.COLUMN_PRODUCT_ID +
//                " WHERE ir." + DBHelper.COLUMN_ITEMRECIPE_RECIPE + " = ?";
//        Cursor c = db.rawQuery(sql, new String[] {recipeId.toString()});
//        try {
//            while (c.moveToNext()) {
//                int amout = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMRECIPE_AMOUNT));
//                String product_name = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
//                Product product = new Product();
//                product.setName(product_name);
//                ItemRecipe itemRecipe = new ItemRecipe();
//                itemRecipe.setAmount(amout);
//                itemRecipe.setProduct(product);
//
//                items.add(itemRecipe);
//            }
//        }catch (NullPointerException e) {
//            Log.e(TAG,"[NullPointException] Items not found.");
//        } finally {
//            c.close();
//        }
//
//        return items;
//    }

    public ContentValues getContentValues(ItemRecipe itemRecipe){
        ContentValues contentValues = new ContentValues();
        //contentValues.put(DBHelper.COLUMN_ITEMRECIPE_ID,itemRecipe.getId());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_AMOUNT,itemRecipe.getAmount());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_TOTAL,itemRecipe.getTotal());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_PRODUCT,itemRecipe.getProduct().getId());
        contentValues.put(DBHelper.COLUMN_ITEMRECIPE_RECIPE, itemRecipe.getRecipe().getId());

        return contentValues;
    }





}
