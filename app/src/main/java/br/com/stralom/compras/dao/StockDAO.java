package br.com.stralom.compras.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.stralom.compras.entities.Stock;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class StockDAO extends GenericDAO {

    private static final String TAG = "StockDAO";

    public StockDAO(Context context) {
        super(context, DBHelper.TABLE_STOCK);
    }

    public long add(Stock stock){
        return super.add(getContentValues(stock));
    }

    public Stock findById(Long id){

        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABLE_STOCK + " WHERE " + DBHelper.COLUMN_STOCK_ID + " = ? ";
        Stock stock = null ;
        try (Cursor c = db.rawQuery(sql, new String[]{id.toString()})) {
            while (c.moveToNext()) {
                int ingredientsCount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_STOCK_PRODUCTSCOUNT));

                stock = new Stock(id);
                stock.setProductCount(ingredientsCount);
            }
            c.close();
        } catch (NullPointerException e) {
//            if(id == Long.valueOf(1)){
//                stock = new Stock(id);
//                stock.setProductCount(0);
//
//                this.add(getContentValues(stock));
//            }
            Log.i(TAG, "[NullPointerException] Stock not found");
        }
        return stock;
    }

    public ContentValues getContentValues(Stock stock){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_STOCK_ID, stock.getId());
        contentValues.put(DBHelper.COLUMN_STOCK_PRODUCTSCOUNT,stock.getProductCount());
        return  contentValues;
    }
}
