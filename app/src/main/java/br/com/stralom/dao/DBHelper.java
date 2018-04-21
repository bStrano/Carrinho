package br.com.stralom.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.stralom.compras.R;

/**
 * Created by Bruno Strano on 05/01/2018.
 */

class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "Stralom_Compras";
    private static final int DATABASE_VERSION = 1;

    // Category
    public static final String TABLE_CATEGORY = "tb_category";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_NAMEINTERNACIONAL = "nameEnglish";
    public static final String COLUMN_CATEGORY_DEFAULT = "isDefault";
    public static final String COLUMN_CATEGORY_ICON = "icon_flag";
    private static final String SQL_CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "( " +
            COLUMN_CATEGORY_NAME + " TEXT PRIMARY KEY, " +
            COLUMN_CATEGORY_NAMEINTERNACIONAL + " TEXT, " +
            COLUMN_CATEGORY_DEFAULT + " INTEGER DEFAULT 0, " +
            COLUMN_CATEGORY_ICON + " INTEGER" +
            ");";
    // Product
    public static final String TABLE_PRODUCT = "tb_product";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";

    private static final String SQL_CREATE_TABLE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + "( " +
            COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PRODUCT_NAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PRODUCT_PRICE + " REAL , " +
            COLUMN_PRODUCT_CATEGORY + " TEXT NOT NULL,  " +
            " FOREIGN KEY(" + COLUMN_PRODUCT_CATEGORY + " ) REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_NAME + ")" +
            ");";
    //public static final String SQL_DROP_TABLE_PRODUCT = "DROP TABLE IF EXISTS " + TABLE_PRODUCT;
    // Cart
    public static final String TABLE_CART = "tb_cart";
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_TOTAL = "total";
    private static final String SQL_CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "( " +
            COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CART_TOTAL + " REAL NOT NULL " +
            ");";
    //public static final String SQL_DROP_TABLE_CART = "DROP TABLE IF EXISTS " + TABLE_CART;
    // ItemCart
    public static final String TABLE_ITEMCART = "tb_itemCart";
    public static final String COLUMN_ITEMCART_ID = "id";
    public static final String COLUMN_ITEMCART_AMOUNT = "amount";
    public static final String COLUMN_ITEMCART_TOTAL = "total";
    public static final String COLUMN_ITEMCART_CART = "cart_id";
    public static final String COLUMN_ITEMCART_PRODUCT = "product_id";
    public static final String COLUMN_ITEMCART_UPDATESTOCK = "update_stock";
    private static final String SQL_CREATE_TABLE_ITEMCART = "CREATE TABLE " + TABLE_ITEMCART + "(  " +
            COLUMN_ITEMCART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ITEMCART_AMOUNT + " INTEGER NOT NULL, " +
            COLUMN_ITEMCART_TOTAL + " REAL NOT NULL, " +
            COLUMN_ITEMCART_CART + " INTEGER NOT NULL, " +
            COLUMN_ITEMCART_PRODUCT + " INTEGER UNIQUE NOT NULL, " +
            COLUMN_ITEMCART_UPDATESTOCK + " INTEGER DEFAULT 0, " +
            "FOREIGN KEY("+ COLUMN_ITEMCART_CART + ") REFERENCES " + TABLE_CART + "(" + COLUMN_CART_ID + "), " +
            "FOREIGN KEY("+ COLUMN_ITEMCART_PRODUCT + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ")" +
            ");";
    //public static final String SQL_DROP_TABLE_ITEMCART = "DROP TABLE IF EXISTS " + TABLE_ITEMCART;
    // RECIPE
    public static final String TABLE_RECIPE = "tb_recipe";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_TOTAL = "total";
    public static final String COLUMN_RECIPE_INGREDIENTCOUNT = "ingredientCount";
    public static final String COLUMN_RECIPE_IMAGEPATH = "imagePath";
    private static final String SQL_CREATE_TABLE_RECIPE = "CREATE TABLE " + TABLE_RECIPE + "( " +
            COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
            COLUMN_RECIPE_INGREDIENTCOUNT + " INTEGER NOT NULL, " +
            COLUMN_RECIPE_TOTAL + " REAL NOT NULL, " +
            COLUMN_RECIPE_IMAGEPATH + " TEXT " +
            ");";
    //ItemRecipe
    public static final String TABLE_ITEMRECIPE  = "tb_itemRecipe";
    public static final String COLUMN_ITEMRECIPE_ID = "id";
    public static final String COLUMN_ITEMRECIPE_AMOUNT = "amount";
    public static final String COLUMN_ITEMRECIPE_TOTAL = "total";
    public static final String COLUMN_ITEMRECIPE_PRODUCT = "product_id";
    public static final String COLUMN_ITEMRECIPE_RECIPE = "recipe_id";
    private static final String SQL_CREATE_TABLE_ITEMRECIPE = "CREATE TABLE " + TABLE_ITEMRECIPE + "( " +
            COLUMN_ITEMRECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             COLUMN_ITEMRECIPE_AMOUNT + " INTEGER NOT NULL, " +
            COLUMN_ITEMRECIPE_TOTAL + " REAL NOT NULL, " +
            COLUMN_ITEMRECIPE_PRODUCT + " INTEGER NOT NULL, " +
            COLUMN_ITEMRECIPE_RECIPE  + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_ITEMRECIPE_PRODUCT + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + "), " +
            "FOREIGN KEY(" + COLUMN_ITEMRECIPE_RECIPE + ") REFERENCES " + TABLE_RECIPE + "(" + COLUMN_RECIPE_ID + ") " +
            ");";
    // Stock
    public static final String TABLE_STOCK = "tb_stock";
    public static final String COLUMN_STOCK_ID = "id";
    public static final String COLUMN_STOCK_PRODUCTSCOUNT = "productsCount";
    private static final String SQL_CREATE_TABLE_STOCK = "CREATE TABLE " + TABLE_STOCK  + " ( " +
            COLUMN_STOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_STOCK_PRODUCTSCOUNT + " INTEGER " +
            ");";
    // ITEMSTOCK
    public static final String TABLE_ITEMSTOCK = "tb_itemStock";
    public static final String COLUMN_ITEMSTOCK_ID = "id";
    public static final String COLUMN_ITEMSTOCK_AMOUNT = "amount";
    public static final String COLUMN_ITEMSTOCK_TOTAL = "total";
    public static final String COLUMN_ITEMSTOCK_STOCKPERCENTAGE = "stockPercentage";
    public static final String COLUMN_ITEMSTOCK_STATUS = "status";
    public static final String COLUMN_ITEMSTOCK_ACTUALAMOUNT = "actualAmount";
    public static final String COLUMN_ITEMSTOCK_PRODUCT = "product_id";
    public static final String COLUMN_ITEMSTOCK_STOCK = "stock_id";
    private static final String SQL_CREATE_TABLE_ITEMSTOCK = "CREATE TABLE " + TABLE_ITEMSTOCK + " ( " +
            COLUMN_ITEMSTOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ITEMSTOCK_AMOUNT + " INTEGER NOT NULL, " +
            COLUMN_ITEMSTOCK_TOTAL + " INTEGER NOT NULL, " +
            COLUMN_ITEMSTOCK_STOCKPERCENTAGE + " INTEGER NOT NULL, " +
            COLUMN_ITEMSTOCK_STATUS + " TEXT NOT NULL, " +
            COLUMN_ITEMSTOCK_ACTUALAMOUNT + " INTEGER NOT NULL, " +
            COLUMN_ITEMSTOCK_PRODUCT + " INTEGER NOT NULL, " +
            COLUMN_ITEMSTOCK_STOCK + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_ITEMSTOCK_PRODUCT + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + "), " +
            "FOREIGN KEY(" + COLUMN_ITEMSTOCK_STOCK + ") REFERENCES " + TABLE_STOCK + "(" + COLUMN_STOCK_ID + ") " +
            ");";
    // SIMPLEITEM
    public static final String TABLE_SIMPLEITEM = "tb_simpleItem";
    private static final String COLUMN_SIMPLEITEM_ID = "id";
    public static final String COLUMN_SIMPLEITEM_NAME = "name";
    public static final String COLUMN_SIMPLEITEM_AMOUNT = "amount";
    public static final String COLUMN_SIMPLEITEM_CART = "cart_id";
    private static final String SQL_CREATE_TABLE_SIMPLEITEM = "CREATE TABLE " + TABLE_SIMPLEITEM + "( " +
            COLUMN_SIMPLEITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SIMPLEITEM_NAME + " TEXT NOT NULL, " +
            COLUMN_SIMPLEITEM_AMOUNT + " INTEGER, " +
            COLUMN_SIMPLEITEM_CART + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_SIMPLEITEM_CART + ") REFERENCES " + TABLE_CART + "(" + COLUMN_CART_ID + ") " +
            ");";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // private String getDropTableString(String tableName){
    //    return "DROP TABLE IF EXISTS " + tableName;
    //}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CATEGORY);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PRODUCT);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CART);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ITEMCART);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_RECIPE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ITEMRECIPE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STOCK);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ITEMSTOCK);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_SIMPLEITEM);

        insertDefaultCategories(sqLiteDatabase);
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_CART + "(" + COLUMN_CART_TOTAL + ") VALUES('0')");
        //sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ", " + COLUMN_PRODUCT_NAME + ") VALUES('-1','SimpleProduct')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 3:
                sqLiteDatabase.execSQL("ALTER TABLE " + DBHelper.TABLE_RECIPE + " ADD COLUMN " + DBHelper.COLUMN_RECIPE_IMAGEPATH + " TEXT");
                Log.i(TAG,"Updated to version 4" );
        }
//        sqLiteDatabase.execSQL(SQL_DROP_TABLE_ITEMCART);
//        sqLiteDatabase.execSQL(SQL_DROP_TABLE_CART);
//        sqLiteDatabase.execSQL(SQL_DROP_TABLE_PRODUCT);
//        sqLiteDatabase.execSQL(getDropTableString(TABLE_ITEMRECIPE));
//        sqLiteDatabase.execSQL(getDropTableString(TABLE_RECIPE));
//        onCreate(sqLiteDatabase);

    }

    private void insertDefaultCategories(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_NAME + "," + COLUMN_CATEGORY_DEFAULT + "," + COLUMN_CATEGORY_ICON + ")"
            + " VALUES ('Carnes',1, " + R.drawable.meat + ")," +
                "('Frutas',1," + R.drawable.cherries + " );");
    }
}
