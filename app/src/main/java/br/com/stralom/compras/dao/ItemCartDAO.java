package br.com.stralom.compras.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.util.Log;

import java.util.List;

import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.Product;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class ItemCartDAO extends GenericDAO  {
    private static final String TAG = "ItemCartDAO";
    private SimpleItemDAO simpleItemDAO;


    public ItemCartDAO(Context context) {
        super(context, DBHelper.TABLE_ITEMCART);
    }



    public Long add(ItemCart itemCart) {
        return add(getContentValues(itemCart));
    }


    public void remove(ItemCart itemCart){
        db = dbHelper.getReadableDatabase();
        if(itemCart.getConvertedId() != null){
            simpleItemDAO.remove(itemCart.getConvertedId());
        } else {
            db.delete(DBHelper.TABLE_ITEMCART, DBHelper.COLUMN_ITEMCART_ID + " = ?", new String[]{itemCart.getId().toString()});
            db.close();
        }
    }


    public void update(ItemCart item) {
        db = dbHelper.getWritableDatabase();
        db.update(DBHelper.TABLE_ITEMCART,getContentValues(item),DBHelper.COLUMN_ITEMCART_ID+ " = ?",new String[] {item.getId().toString()});
        db.close();
    }

    public ItemCart getByProductName(String name){
        db = dbHelper.getReadableDatabase();
        ItemCart itemCart = null;
        String sql = "SELECT i." + DBHelper.COLUMN_ITEMCART_ID + " , i." + DBHelper.COLUMN_ITEMCART_AMOUNT + " , i." + DBHelper.COLUMN_ITEMCART_CART + " , i." + DBHelper.COLUMN_ITEMCART_PRODUCT + " , i." + DBHelper.COLUMN_ITEMCART_TOTAL +
                ", p." + DBHelper.COLUMN_PRODUCT_NAME + " , p." + DBHelper.COLUMN_PRODUCT_PRICE +
                " FROM " + DBHelper.TABLE_ITEMCART + " i " +
                " JOIN " + DBHelper.TABLE_PRODUCT + " p " +
                " ON i." + DBHelper.COLUMN_ITEMCART_PRODUCT + " = p." + DBHelper.COLUMN_PRODUCT_ID +
                " WHERE p." + DBHelper.COLUMN_PRODUCT_NAME + " = ? ";


            Cursor c = db.rawQuery(sql,new String[] {name});
            if(c != null){
                while (c.moveToNext()) {
                    long itemCartId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_ID));
                    int amount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_AMOUNT));
                    Long cartId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_CART));
                    Long productId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_PRODUCT));
                    double total = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_TOTAL));
                    String productName = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
                    double productPrice = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_PRICE));
                    Product product = new Product();
                    product.setName(productName);
                    product.setId(productId);
                    product.setPrice(productPrice);


                    Cart cart = new Cart();
                    cart.setId(cartId);
                    itemCart = new ItemCart(itemCartId, product, amount, total, cart);
                    c.close();
                }
            }

            db.close();

        return itemCart;
    }

    public List<ItemCart> getAllOrderedByCategory(Long cartId){
        String extraConditions = " ORDER BY c." + DBHelper.COLUMN_CATEGORY_NAME;
        return getAll(" WHERE (i." + DBHelper.COLUMN_ITEMCART_CART  + " = ? )"  + extraConditions,new String[]{cartId.toString()});
    }



    public List<ItemCart> getAll(String whereQuery, String[] args ){
        db = dbHelper.getReadableDatabase();
        ObservableArrayList<ItemCart> items = new ObservableArrayList<>();
        try {
            //String sql = "SELECT * FROM " + DBHelper.TABLE_ITEMCART + " WHERE " + DBHelper.COLUMN_ITEMCART_CART + " = ?";
            String sql = "SELECT i." + DBHelper.COLUMN_ITEMCART_AMOUNT + ", i." + DBHelper.COLUMN_ITEMCART_TOTAL + ", i." + DBHelper.COLUMN_ITEMCART_CART + ", i." + DBHelper.COLUMN_ITEMCART_PRODUCT +
                    ", i." + DBHelper.COLUMN_ITEMCART_ID + ", i." +DBHelper.COLUMN_ITEMCART_UPDATESTOCK + ", p." + DBHelper.COLUMN_PRODUCT_NAME +  ", c." + DBHelper.COLUMN_CATEGORY_ICON + " , c." + DBHelper.COLUMN_CATEGORY_NAME +
                    " FROM " + DBHelper.TABLE_ITEMCART + " i " +
                    " JOIN " + DBHelper.TABLE_PRODUCT + " p " +
                    " ON i." + DBHelper.COLUMN_ITEMCART_PRODUCT + " = " + " p." + DBHelper.COLUMN_PRODUCT_ID +
                    " JOIN " + DBHelper.TABLE_CATEGORY + " c " +
                    " ON c." + DBHelper.COLUMN_CATEGORY_NAME + " = " + " p." + DBHelper.COLUMN_PRODUCT_CATEGORY +
                    whereQuery;
            Cursor c = db.rawQuery(sql, args);


        while (c.moveToNext()){
            Long id = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_ID));
            int amount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_AMOUNT));
            Long productId = c.getLong(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_PRODUCT));
            String name = c.getString(c.getColumnIndex(DBHelper.COLUMN_PRODUCT_NAME));
            int iconFlag = c.getInt(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_ICON));
            String categoryName = c.getString(c.getColumnIndex(DBHelper.COLUMN_CATEGORY_NAME));
            int updateStock = c.getInt(c.getColumnIndex(DBHelper.COLUMN_ITEMCART_UPDATESTOCK));
            long cartId = c.getLong((c.getColumnIndex(DBHelper.COLUMN_ITEMCART_CART )));

            Category category = new Category();
            category.setIconFlag(iconFlag);
            category.setName(categoryName);

            Product product = new Product();
            product.setName(name);
            product.setCategory(category);
            product.setId(productId);

            Cart cart = new Cart();
            cart.setId(cartId);

            ItemCart item = new ItemCart(product,amount,cart);
            item.setId(id);
            item.setUpdateStock(updateStock);
            items.add(item);

        }
        c.close();
      } catch (NullPointerException e) {
            Log.e(TAG,"[NullPointerException] Carrinho vazio.");
        }
        db.close();
        return items;
    }



    private ContentValues getContentValues(ItemCart item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_ITEMCART_AMOUNT, item.getAmount());
        contentValues.put(DBHelper.COLUMN_ITEMCART_TOTAL, item.getTotal());
        contentValues.put(DBHelper.COLUMN_ITEMCART_CART, item.getCart().getId());
        contentValues.put(DBHelper.COLUMN_ITEMCART_PRODUCT, item.getProduct().getId());
        if(item.isUpdateStock()){
            contentValues.put(DBHelper.COLUMN_ITEMCART_UPDATESTOCK,1);
        } else {
            contentValues.put(DBHelper.COLUMN_ITEMCART_UPDATESTOCK,0);
        }


        return contentValues;
    }


}
