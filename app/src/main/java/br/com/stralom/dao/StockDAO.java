package br.com.stralom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.stralom.entities.Stock;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class StockDAO extends GenericDAO {

    private static final String TAG = "StockDAO";

    public StockDAO(Context context) {
        super(context, DBHelper.TABLE_STOCK);
    }

    public Stock findById(Long id){

        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABLE_STOCK + " WHERE " + DBHelper.COLUMN_STOCK_ID + " = ? ";
        Cursor c = db.rawQuery(sql, new String[] {id.toString()});
        Stock stock = null ;
        try {
            while(c.moveToNext()){
                int ingredientsCount = c.getInt(c.getColumnIndex(DBHelper.COLUMN_STOCK_PRODUCTSCOUNT));

                stock = new Stock(id);
                stock.setProductCount(ingredientsCount);
            }
        } catch (NullPointerException e) {
//            if(id == Long.valueOf(1)){
//                stock = new Stock(id);
//                stock.setProductCount(0);
//
//                this.add(getContentValues(stock));
//            }
            Log.i(TAG,"[NullPointerException] Stock not found");
        } finally {
            c.close();
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
