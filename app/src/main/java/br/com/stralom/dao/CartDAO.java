package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import br.com.stralom.entities.Cart;

/**
 * Created by Bruno Strano on 04/01/2018.
 */

public class CartDAO extends GenericDAO {



    public CartDAO(Context context) {
        super(context, DBHelper.TABLE_ITEMCART);
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
    private ContentValues getContentValues(Cart cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_CART_TOTAL,cart.getTotal());
        return contentValues;
    }


}
