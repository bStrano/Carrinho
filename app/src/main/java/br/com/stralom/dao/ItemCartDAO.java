package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.entities.Cart;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class ItemCartDAO extends GenericDAO  {
    private static final String TAG = "ItemCartDAO";


    public ItemCartDAO(Context context) {
        super(context, DBHelper.TABLE_ITEMCART);
    }


    public Long add(ItemCart itemCart) {
        return add(getContentValues(itemCart));
    }

    public void remove(Long id){
        db = dbHelper.getReadableDatabase();
        db.delete(DBHelper.TABLE_ITEMCART,DBHelper.COLUMN_ITEMCART_ID + " = ?", new String[] {id.toString()});
    }

    public void update(ItemCart item) {
        db = dbHelper.getWritableDatabase();
        db.update(DBHelper.TABLE_ITEMCART,getContentValues(item),DBHelper.COLUMN_ITEMCART_ID+ " = ?",new String[] {item.getId().toString()});
    }

    public ItemCart getByProductId(Long id){
        ItemCart itemCart = new ItemCart();
        String sql = "SELECT i." + DBHelper.COLUMN_ITEMCART_ID + ", i." + DBHelper.COLUMN_ITEMCART_AMOUNT + ", i." + DBHelper.COLUMN_ITEMCART_CART + ", i." + DBHelper.COLUMN_ITEMCART_PRODUCT + ", i." + DBHelper.COLUMN_ITEMCART_TOTAL +
                ", p." + DBHelper.COLUMN_PRODUCT_NAME +
                " FROM " + DBHelper.TABLE_ITEMCART + " i " +
                " JOIN " + DBHelper.TABLE_PRODUCT + " p " +
                " ON i." + DBHelper.COLUMN_ITEMCART_PRODUCT + " = p." + DBHelper.COLUMN_PRODUCT_ID +
                " WHERE i." + DBHelper.COLUMN_PRODUCT_ID + " = ? ";

        Cursor c = db.rawQuery(sql,new String[] {id.toString()});
        if (c != null) {
            while(c.moveToNext()){
                int itemCartId = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_ID));
                int amount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_AMOUNT));
                Long cartId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_CART));
                Long productId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_PRODUCT));
                double total = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_TOTAL));
                String productName = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));

                Product product = new Product();
                product.setName(productName);
                product.setId(productId);

                Cart cart = new Cart();
                cart.setId(cartId);
                itemCart = new ItemCart(id,product,amount,total,cart);
            }
            c.close();
            return itemCart;
        }


        return itemCart;
    }

    public List<ItemCart> getAll(Long cartId){
        db = dbHelper.getReadableDatabase();
        ArrayList<ItemCart> items = new ArrayList<>();
        try {
            //String sql = "SELECT * FROM " + DBHelper.TABLE_ITEMCART + " WHERE " + DBHelper.COLUMN_ITEMCART_CART + " = ?";
            String sql = "SELECT i." + DBHelper.COLUMN_ITEMCART_AMOUNT + ", i." + DBHelper.COLUMN_ITEMCART_TOTAL + ", i." + DBHelper.COLUMN_ITEMCART_CART + ", i." + DBHelper.COLUMN_ITEMCART_PRODUCT +
                    ", i." + DBHelper.COLUMN_ITEMCART_ID +
                    ", p." + DBHelper.COLUMN_PRODUCT_NAME +
                     " FROM " + DBHelper.TABLE_ITEMCART + " i " +
                    " JOIN " + DBHelper.TABLE_PRODUCT + " p " +
                    " ON i." + DBHelper.COLUMN_ITEMCART_PRODUCT + " = " + " p." + DBHelper.COLUMN_PRODUCT_ID +
                            " WHERE i." + DBHelper.COLUMN_ITEMCART_CART + " = ?";
            Cursor c = db.rawQuery(sql, new String[] {cartId.toString()});


        while (c.moveToNext()){
            Long id = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_ID));
            int amount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_AMOUNT));
            double total = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_TOTAL));
            Long productId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_PRODUCT));
            String name = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));

            Product product = new Product();
            product.setName(name);
            product.setId(productId);

            Cart cart = new Cart();
            cart.setId(cartId);

            ItemCart item = new ItemCart(product,amount,cart);
            item.setId(id);
            items.add(item);

        }
        c.close();
      } catch (NullPointerException e) {
            Log.e(TAG,"[NullPointerException] Carrinho vazio.");
        }
        return items;
    }



    private ContentValues getContentValues(ItemCart item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_ITEMCART_AMOUNT, item.getAmount());
        contentValues.put(DBHelper.COLUMN_ITEMCART_TOTAL, item.getTotal());
        contentValues.put(DBHelper.COLUMN_ITEMCART_CART, item.getCart().getId());
        contentValues.put(DBHelper.COLUMN_ITEMCART_PRODUCT, item.getProduct().getId());

        return contentValues;
    }

}
