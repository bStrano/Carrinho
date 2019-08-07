package br.com.stralom.compras.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.compras.entities.SimpleItem;

/**
 * Created by Bruno on 05/02/2018.
 */

public class SimpleItemDAO extends GenericDAO {

    public SimpleItemDAO(Context context) {
        super(context, DBHelper.TABLE_SIMPLEITEM);
    }


    public Long add(SimpleItem simpleItem) {
        return super.add(getContentValues(simpleItem));
    }

    public List<SimpleItem> getAll(Long cartId){
        db = dbHelper.getReadableDatabase();
        ArrayList<SimpleItem> items = new ArrayList<>();
        String sql = "SELECT * FROM " + DBHelper.TABLE_SIMPLEITEM + " WHERE " + DBHelper.COLUMN_SIMPLEITEM_CART + " = ?";
        Cursor c = db.rawQuery(sql,new String[] {cartId.toString()});
        if( c != null) {
            while(c.moveToNext()){
                long id = c.getLong(c.getColumnIndex(DBHelper.COLUMN_SIMPLEITEM_ID));
                String name = c.getString(c.getColumnIndex(DBHelper.COLUMN_SIMPLEITEM_NAME));
                int amount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_SIMPLEITEM_AMOUNT));

                SimpleItem simpleItem = new SimpleItem(name,amount);
                simpleItem.setId(id);
                items.add(simpleItem);
            }
            c.close();
        }

        return items;

    }

    public ContentValues getContentValues(SimpleItem simpleItem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_SIMPLEITEM_NAME,simpleItem.getName());
        contentValues.put(DBHelper.COLUMN_SIMPLEITEM_AMOUNT,simpleItem.getAmount());
        contentValues.put(DBHelper.COLUMN_SIMPLEITEM_CART,simpleItem.getCart().getId());
        return contentValues;
    }
}
