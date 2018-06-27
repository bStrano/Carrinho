package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Stock;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class ItemStockDAO extends GenericDAO {
    private static final String TAG = "ItemStockDAO";


    public ItemStockDAO(Context context) {
        super(context, DBHelper.TABLE_ITEMSTOCK);
    }

    public void update(ItemStock itemStock){
        super.update(DBHelper.COLUMN_ITEMSTOCK_ID, itemStock.getId(),getContentValues(itemStock));
    }

    public List<ItemStock> getAll(Long idStock){
        db = dbHelper.getReadableDatabase();

        ObservableArrayList<ItemStock> products = new ObservableArrayList<>();
//        String sql = "SELECT * FROM " + DBHelper.TABLE_ITEMSTOCK + " INNER JOIN " + DBHelper.TABLE_PRODUCT +
//                " ON " + DBHelper.TABLE_ITEMSTOCK.concat("."+ DBHelper.COLUMN_ITEMSTOCK_PRODUCT) + " = " + DBHelper.TABLE_PRODUCT.concat("." + DBHelper.COLUMN_PRODUCT_ID)
//                +" WHERE " + DBHelper.TABLE_ITEMSTOCK.concat("."+ DBHelper.COLUMN_ITEMSTOCK_STOCK)+ " = ? ";
        String sql = "SELECT s." + DBHelper.COLUMN_ITEMSTOCK_STOCK + ", s." + DBHelper.COLUMN_ITEMSTOCK_PRODUCT + ", s." + DBHelper.COLUMN_ITEMSTOCK_ACTUALAMOUNT + ", s." + DBHelper.COLUMN_ITEMSTOCK_STATUS
                + ", s." + DBHelper.COLUMN_ITEMSTOCK_STOCKPERCENTAGE + ", s." + DBHelper.COLUMN_ITEMSTOCK_TOTAL + ", s." + DBHelper.COLUMN_ITEMSTOCK_AMOUNT + ", s." + DBHelper.COLUMN_ITEMSTOCK_ID
                + ", p." + DBHelper.COLUMN_PRODUCT_NAME +
                " FROM " + DBHelper.TABLE_ITEMSTOCK + " s " +
                " JOIN " + DBHelper.TABLE_PRODUCT + " p" +
                " ON s." + DBHelper.COLUMN_ITEMSTOCK_PRODUCT + " = p." + DBHelper.COLUMN_PRODUCT_ID +
                " WHERE s." + DBHelper.COLUMN_ITEMSTOCK_STOCK + " = ?";


        try (Cursor cursor = db.rawQuery(sql, new String[]{idStock.toString()})) {
            while (cursor.moveToNext()) {
                String productName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
                ItemStock itemStock = getItemStock(cursor);
                itemStock.getProduct().setName(productName);
                products.add(itemStock);
            }
        } catch (NullPointerException e) {
            Log.i(TAG, "[NullPointerException] Products not found.");
        }
        return  products;
    }

    @NonNull
    private ItemStock getItemStock(Cursor cursor) {
        Long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_ID));
        int amount = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_AMOUNT));
        double total = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_TOTAL));
        int stockPercentage = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_STOCKPERCENTAGE));
        ItemStock.Status status = ItemStock.Status.valueOf(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_STATUS)));
        int atualAmount = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_ACTUALAMOUNT));
        Long productId = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_PRODUCT));
        Long stockId = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ITEMSTOCK_STOCK));
        //String productName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
        Product product = new Product();
        product.setId(productId);
       // product.setName(productName);
        Stock stock = new Stock();
        stock.setId(stockId);

        return new ItemStock(id, amount, total, product, stockPercentage, status, atualAmount, stock);
    }

    public ItemStock findByProductId(  long productId) {
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABLE_ITEMSTOCK + " WHERE " + DBHelper.COLUMN_ITEMSTOCK_PRODUCT + " = ?";


        Cursor cursor = db.rawQuery(sql,new String[] { Long.toString(productId)});
        ItemStock itemStock =  find(cursor);
        cursor.close();
        return itemStock;
    }

    public ItemStock findByProductName(String name){
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT i." + DBHelper.COLUMN_ITEMSTOCK_PRODUCT + " ,i." + DBHelper.COLUMN_ITEMSTOCK_ACTUALAMOUNT + " ,i." + DBHelper.COLUMN_ITEMSTOCK_AMOUNT +
                 " ,i." + DBHelper.COLUMN_ITEMSTOCK_ID + " ,i." + DBHelper.COLUMN_ITEMSTOCK_STATUS + " ,i." + DBHelper.COLUMN_ITEMSTOCK_STOCK + " ,i." + DBHelper.COLUMN_ITEMSTOCK_STOCKPERCENTAGE +
                 " ,i." + DBHelper.COLUMN_ITEMSTOCK_TOTAL + " ,p." + DBHelper.COLUMN_PRODUCT_NAME +
                 " FROM " + DBHelper.TABLE_ITEMSTOCK + " i " +
                 " JOIN " + DBHelper.TABLE_PRODUCT + " p " +
                 " ON i." + DBHelper.COLUMN_ITEMSTOCK_PRODUCT + " = p." + DBHelper.COLUMN_PRODUCT_ID +
                 " WHERE p." + DBHelper.COLUMN_PRODUCT_NAME  + " =  ? ";


        Cursor cursor = db.rawQuery(sql, new String [] { name});

        ItemStock itemStock =  find(cursor);
        if(itemStock == null) {
            return null;
        }
        itemStock.getProduct().setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME)));
        cursor.close();
        return itemStock;
    }

    private ItemStock find(Cursor cursor){
        ItemStock itemStock = null;
        if(cursor != null && cursor.moveToFirst()){
            itemStock = getItemStock(cursor);
        }
        return  itemStock;
    }

    public ContentValues getContentValues(ItemStock itemStock){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_ID, itemStock.getId());
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_AMOUNT, itemStock.getAmount());
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_TOTAL, itemStock.getTotal());
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_STOCKPERCENTAGE, itemStock.getStockPercentage());
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_STATUS,itemStock.getStatus().toString());
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_ACTUALAMOUNT, itemStock.getActualAmount());
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_PRODUCT, itemStock.getProduct().getId());
        contentValues.put(DBHelper.COLUMN_ITEMSTOCK_STOCK, itemStock.getStock().getId());


        return contentValues;
    }


}
