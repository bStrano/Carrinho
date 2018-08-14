package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.entities.Cart;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 30/12/2017.
 */

public class ProductDAO extends GenericDAO{
    private static final String TAG = "ProductDAO";

    private final DBHelper dbHelper;

    public ProductDAO(Context context) {
        super(context,DBHelper.TABLE_PRODUCT);
        dbHelper = new DBHelper(context);
    }



    public Long add(Product product) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(product);
        return db.insertOrThrow(DBHelper.TABLE_PRODUCT,null,contentValues);
    }

    public Product add(String name, double price, Category category){
        Product product = new Product(name,price,category );
        Long id = add(product);
        product.setId(id);
        return product;
    }


    public void remove(Long id){
        db = dbHelper.getReadableDatabase();
        db.delete(DBHelper.TABLE_PRODUCT,DBHelper.COLUMN_PRODUCT_ID + " = ?", new String[] {id.toString()});
    }


    public Product getByName(String name){
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_PRODUCT + " WHERE  " + DBHelper.COLUMN_PRODUCT_NAME+ " = ?",new String[] {name} );
        if(cursor != null){
            cursor.moveToFirst();
            return getProduct(cursor);
        } else {
            return null;
        }

    }

    public ArrayList<Product> getAllProductsNotInsertedInTheCart(Cart cart){

        db = dbHelper.getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();


        // SELECT  * FROM tb_product LEFT JOIN tb_itemCart ON tb_itemCart.product_id == tb_product.id  LEFT JOIN tb_category ON tb_category.category_name = tb_product.category WHERE tb_itemCart.product_id IS NULL
        String sql =
                "SELECT  p." + DBHelper.COLUMN_PRODUCT_ID + " , p." + DBHelper.COLUMN_PRODUCT_NAME + " , p." + DBHelper.COLUMN_PRODUCT_PRICE + " , p." + DBHelper.COLUMN_PRODUCT_CATEGORY +
                        " , c." + DBHelper.COLUMN_CATEGORY_NAMEINTERNACIONAL + " , c." + DBHelper.COLUMN_CATEGORY_ICON + " , c." + DBHelper.COLUMN_CATEGORY_DEFAULT + " , c." + DBHelper.COLUMN_CATEGORY_NAME +
                        " FROM " + DBHelper.TABLE_PRODUCT + " p " +
                        " LEFT JOIN " + DBHelper.TABLE_ITEMCART + " ic " +
                        " ON p." + DBHelper.COLUMN_PRODUCT_ID + " = ic." + DBHelper.COLUMN_ITEMCART_PRODUCT + " AND ic." + DBHelper.COLUMN_ITEMCART_CART + " = ?" +
                        " JOIN " + DBHelper.TABLE_CATEGORY  + " c " +
                        "  ON c. " + DBHelper.COLUMN_CATEGORY_NAME + " = p." + DBHelper.COLUMN_PRODUCT_CATEGORY +
                        " WHERE ic." + DBHelper.COLUMN_ITEMCART_PRODUCT + " IS NULL ";

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(cart.getId())});
        if(cursor != null){
            while (cursor.moveToNext()){
                Product product = getProduct(cursor);

                products.add(product);
            }

            cursor.close();
        }
        return products;

    }


    public List<Product> getAll(){
        return getAll(null);
    }

    public List<Product> getAllOrderedByName(){
        return getAll(DBHelper.COLUMN_PRODUCT_NAME);
    }

    private List<Product> getAll(String order_colName){

        db = dbHelper.getReadableDatabase();
        ObservableArrayList<Product> products = new ObservableArrayList<>();


        String sql = "SELECT p." + DBHelper.COLUMN_PRODUCT_ID + " , p." + DBHelper.COLUMN_PRODUCT_NAME + " , p." + DBHelper.COLUMN_PRODUCT_PRICE + " , p." + DBHelper.COLUMN_PRODUCT_CATEGORY +
                " , c." + DBHelper.COLUMN_CATEGORY_NAMEINTERNACIONAL + " , c." + DBHelper.COLUMN_CATEGORY_ICON + " , c." + DBHelper.COLUMN_CATEGORY_DEFAULT + " , c." + DBHelper.COLUMN_CATEGORY_NAME +
                " FROM " + DBHelper.TABLE_PRODUCT + " p " +
                " LEFT JOIN " + DBHelper.TABLE_CATEGORY + " c " +
                " ON p." + DBHelper.COLUMN_PRODUCT_CATEGORY + " = c." + DBHelper.COLUMN_CATEGORY_NAME ;

        if(order_colName != null){
            sql = sql + " ORDER BY " + order_colName;
        }
        try {


        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            Product product = getProduct(cursor);

            products.add(product);
        }
        cursor.close();

        } catch (NullPointerException e ){
            Log.e(TAG,"[NullPointExcepetion] Products not found.");
        }
        return products;
    }

    @NonNull
    private Product getProduct(Cursor c) {
        // Category
        String categoryName = c.getString(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAME));
        String categoryInternacional = c.getString(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAMEINTERNACIONAL));
        int categoryIcon = c.getInt(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_ICON));
        int isDefault = c.getInt(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_DEFAULT));
        Category category = new Category(categoryName,categoryInternacional,categoryIcon);
        category.setDefault(isDefault);

        // Product
        Long id = c.getLong(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_ID));
        String name = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
        double price = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_PRICE));

        return new Product(id,name,price,category);
    }



    @NonNull
    private ContentValues getContentValues(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_PRODUCT_NAME,product.getName());
        contentValues.put(DBHelper.COLUMN_PRODUCT_PRICE,product.getPrice());
        contentValues.put(DBHelper.COLUMN_PRODUCT_CATEGORY,product.getCategory().getName());
        return contentValues;
    }

}
