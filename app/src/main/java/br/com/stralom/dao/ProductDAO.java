package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;
import br.com.stralom.util.Constants;

/**
 * Created by Bruno Strano on 30/12/2017.
 */

public class ProductDAO {
    private static final String TAG = "ProductDAO";

   private SQLiteDatabase db;
    private Context mContext;
    private DBHelper dbHelper;

    public ProductDAO(Context context) {
        this.mContext = context;
        dbHelper = new DBHelper(context);
    }



    public void add(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(product);
        db.insertOrThrow(DBHelper.TABLE_PRODUCT,null,contentValues);
    }


    public void remove(Long id){
        db = dbHelper.getReadableDatabase();
        db.delete(DBHelper.TABLE_PRODUCT,DBHelper.COLUMN_PRODUCT_ID + " = ?", new String[] {id.toString()});
    }

    public void update(Product product) {
        db = dbHelper.getWritableDatabase();
        db.update(DBHelper.TABLE_PRODUCT,getContentValues(product),DBHelper.COLUMN_PRODUCT_ID + " = ?",new String[] {product.getId().toString()});
    }

    public Product findById(Long id){
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABLE_PRODUCT + " WHERE " + DBHelper.COLUMN_PRODUCT_ID + " = ?";
        Cursor cursor = db.rawQuery(sql , new String[] {id.toString()});
        Product product = null;
        try {
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_PRICE));
                String category = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRODUCT_CATEGORY));

                product = new Product(id,name,price,category);
            }


        } catch (NullPointerException e){
            Log.e(TAG,"[NullPointException] Product not found.");
        }
        return product;
    }

    public List<Product> getAll(){

        db = dbHelper.getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();

        try {


        Cursor c = db.rawQuery("SELECT * FROM "+ DBHelper.TABLE_PRODUCT, null);
        while (c.moveToNext()){
            Long id = c.getLong(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_ID));
            String name = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
            double price = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_PRICE));
            String category = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_CATEGORY));
            Product product = new Product(id,name,price,category);
            products.add(product);
        }
        c.close();

        } catch (NullPointerException e ){
            Log.e(TAG,"[NullPointExcepetion] Products not found.");
        }
        return products;
    }

    @NonNull
    private ContentValues getContentValues(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_PRODUCT_NAME,product.getName());
        contentValues.put(DBHelper.COLUMN_PRODUCT_PRICE,product.getPrice());
        contentValues.put(DBHelper.COLUMN_PRODUCT_CATEGORY,product.getCategory());
        return contentValues;
    }

}
