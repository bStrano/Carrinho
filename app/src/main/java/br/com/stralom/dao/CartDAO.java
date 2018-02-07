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

import br.com.stralom.entities.Cart;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.Product;
import br.com.stralom.util.Constants;

/**
 * Created by Bruno Strano on 04/01/2018.
 */

public class CartDAO  {
    private static final String TAG = "CartDAO";
    private ItemCartDAO itemDAO;
    private DBHelper dbHelper;
    private Context context;
    private  SQLiteDatabase db;


    public CartDAO(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }


    public Cart findById(Long id){
        db = dbHelper.getReadableDatabase();
        Cart cart = new Cart();
        String sql = "SELECT * FROM " + DBHelper.TABLE_CART + " WHERE " + DBHelper.COLUMN_CART_ID + " = ?";
        Cursor c  = db.rawQuery(sql , new String[] {id.toString()});
        if(c.getCount() == 0 ){
            throw new NullPointerException("Product not found.");
        }
        while(c.moveToNext()){
            int idCart = c.getInt(c.getColumnIndex(DBHelper.COLUMN_CART_ID));
            double total = c.getDouble(c.getColumnIndex(DBHelper.COLUMN_CART_TOTAL));
            //List<ItemCart> cartItens = itemDAO.getAll((long) idCart);

            cart.setId((long) idCart);
            cart.setTotal(total);
            //cart.setListItemCart(cartItens);

        }
        c.close();
        return cart;
    }


    @NonNull
    public ContentValues getContentValues(Cart cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_CART_TOTAL,cart.getTotal());
        return contentValues;
    }

    public Long add(Cart cart) {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(cart);
        return db.insert(DBHelper.TABLE_CART,null,contentValues);

    }
}
